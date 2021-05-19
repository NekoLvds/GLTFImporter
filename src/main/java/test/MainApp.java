package test;

import com.sun.javafx.perf.PerformanceTracker;
import fx.FXglTFAsset;
import gltf.GLTFAsset;
import gltf.GLTFParseException;
import gltf.mesh.GLTFMesh;
import gltf.mesh.GLTFMeshPrimitive;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import jdk.jshell.spi.ExecutionControl;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainApp extends Application {

    public static int WIDTH_BASE = 16;
    public static int HEIGHT_BASER = 9;

    public static int SIZE_FACTOR = 100;

    public static float DEFAULT_ROT_X = 200;
    public static float DEFAULT_ROT_Y = 20;
    public static float DEFAULT_TRANS_X = 0;
    public static float DEFAULT_TRANS_Y = 40;
    public static float DEFAULT_TRANS_Z = 320;

    private long timestamp = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = this.createScene();

        primaryStage.setScene(scene);
        primaryStage.setHeight(getHeight());
        primaryStage.setWidth(getWidth());

        primaryStage.show();
    }

    private Scene createScene(){

        Camera cam3d = new PerspectiveCamera(true);
        cam3d.setFarClip(10000);

        Camera cam2d = new PerspectiveCamera();

        StackPane sceneRoot = new StackPane();
        Scene scene = new Scene(sceneRoot);

        PerformanceTracker tracker = PerformanceTracker.getSceneTracker(scene);

        Translate translate = new Translate();
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

        Group root3d = new Group();
        SubScene scene3d = new SubScene(root3d, getWidth(), getHeight());

        Group root2d = create2dElement(root3d, translate, rotateX, rotateY, scene3d, tracker);
        SubScene scene2d = new SubScene(root2d, getWidth(), getHeight());

        root3d.getTransforms().addAll(translate, rotateX, rotateY);

        sceneRoot.getChildren().addAll(scene3d, scene2d);

        scene3d.setCamera(cam3d);
        scene2d.setCamera(cam2d);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case W:
                    translate.setZ(translate.getZ() - 10);
                    break;
                case S:
                    translate.setZ(translate.getZ() + 10);
                    break;
                case A:
                    translate.setX(translate.getX() - 10);
                    break;
                case D:
                    translate.setX(translate.getX() + 10);
                    break;
                case Q:
                    translate.setY(translate.getY() - 10);
                    break;
                case E:
                    translate.setY(translate.getY() + 10);
                    break;
                case UP:
                    rotateX.setAngle(rotateX.getAngle() + 10);
                    break;
                case DOWN:
                    rotateX.setAngle(rotateX.getAngle() - 10);
                    break;
                case LEFT:
                    rotateY.setAngle(rotateY.getAngle() + 10);
                    break;
                case RIGHT:
                    rotateY.setAngle(rotateY.getAngle() - 10);
                    break;
            }

            event.consume();
        });
        return scene;
    }

    private Group create2dElement(Group ref3d, Translate translate, Rotate rotateX, Rotate rotateY, SubScene snapshotTarget, PerformanceTracker tracker){
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(20,10,10,10));

        Map<String, URI> data = gltfAssetMap();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(data.keySet());
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                translate.setX(DEFAULT_TRANS_X);
                translate.setY(DEFAULT_TRANS_Y);
                translate.setZ(DEFAULT_TRANS_Z);

                rotateX.setAngle(DEFAULT_ROT_X);
                rotateY.setAngle(DEFAULT_ROT_Y);

                ref3d.getChildren().clear();

                Exception exception = null;
                try {
                    GLTFAsset gltfAsset = new GLTFAsset(data.get(comboBox.getSelectionModel().getSelectedItem()));
                    FXglTFAsset fXglTFAsset = FXglTFAsset.fromGLTFAsset(gltfAsset);
                    ref3d.getChildren().addAll(fXglTFAsset);

                } catch (ExecutionControl.NotImplementedException e) {
                    exception = e;
                } catch (IOException e) {
                    exception = e;
                } catch (GLTFParseException e) {
                    exception = e;
                }

                if (exception != null){
                    TextArea area = new TextArea(exception.getMessage());
                    ref3d.getChildren().addAll(area);
                }
            }
        });

        Button screenshotButton = new Button("Take a pic");
        screenshotButton.setOnAction(event -> {
            SnapshotParameters parameters = new SnapshotParameters();
            WritableImage image = snapshotTarget.snapshot(null, null);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH-mm-ss");

            String downloadDir = System.getProperty("user.home") + "/Downloads/";
            String fileName = comboBox.getSelectionModel().getSelectedItem() + "_" + formatter.format(LocalDateTime.now()) + ".png";

            RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(renderedImage, "png", new File(downloadDir + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Label rotXLabel = new Label("rotX: ");
        Label rotXValue = new Label();
        rotXValue.textProperty().bind(rotateX.angleProperty().asString());
        VBox xRotBox = new VBox(rotXLabel, rotXValue);

        Label rotYLabel = new Label("rotY: ");
        Label rotYValue = new Label();
        rotYValue.textProperty().bind(rotateY.angleProperty().asString());
        VBox yRotBox = new VBox(rotYLabel, rotYValue);

        Label transXLabel = new Label("transX: ");
        Label transXValue = new Label();
        transXValue.textProperty().bind(translate.xProperty().asString());
        VBox transXBox = new VBox(transXLabel, transXValue);

        Label transYLabel = new Label("transY: ");
        Label transYValue = new Label();
        transYValue.textProperty().bind(translate.yProperty().asString());
        VBox transYBox = new VBox(transYLabel, transYValue);

        Label transZLabel = new Label("transZ:");
        Label transZValue = new Label();
        transZValue.textProperty().bind(translate.zProperty().asString());
        VBox transZBox = new VBox(transZLabel, transZValue);

        Label fpsLabel = new Label("fps: ");
        Label fpsValue = new Label();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                fpsValue.setText(String.format("%.3f",tracker.getAverageFPS()));

                long oneSecondAsNano = 1000000000;
                if ((now - timestamp) > oneSecondAsNano * 10){
                    timestamp = now;
                    tracker.resetAverageFPS();
                }
            }
        };
        timer.start();
        VBox fpsBox = new VBox(fpsLabel, fpsValue);


        HBox labelsBox = new HBox(xRotBox, yRotBox, transXBox, transYBox, transZBox, fpsBox);
        labelsBox.setSpacing(10);

        hBox.getChildren().addAll(comboBox, screenshotButton, labelsBox);

        return new Group(hBox);
    }

    private Map<String, URI> gltfAssetMap(){
        List<URI> uris = gltfAssetList();
        Map<String, URI> stringURIMap = new HashMap<>();

        for (URI uri: uris){
            File file = new File(uri);
            stringURIMap.put(file.getName(), uri);
        }

        return stringURIMap;
    }

    private List<URI> gltfAssetList(){
        File baseUri = new File("C:\\Users\\Lucas\\IdeaProjects\\GLTFImporter\\src\\main\\resources\\glTF-Sample-Models\\2.0\\");

        List<URI> uris = new LinkedList<>();

        if (baseUri.isDirectory() && baseUri.exists()){
            String[] assetDirs = baseUri.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return dir.isDirectory();
                }
            });

            for(String s : assetDirs){
                File file = new File(s);
                s += "\\glTF\\" + file.getName() + ".gltf";
                String endFile = baseUri.getPath() + "\\" + s;
                uris.add(new File(endFile).toURI());
            }
        }

        return uris;
    }

    private TriangleMesh fromPrimitive(GLTFMeshPrimitive primitive) throws GLTFParseException {

        TriangleMesh mesh = new TriangleMesh();
        mesh.getTexCoords().addAll(0,0);

        float[] data = primitive.getAttribute().getPosition().readDataAsFloats(); //All data NOT Vertices
        float[][] vertices = new float[data.length / 3][3]; //actually vertices

        for (int i = 0;i < vertices.length; i++){
            int dataOffset = i * 3;
            vertices[i] = new float[]{data[dataOffset], data[dataOffset+1], data[dataOffset+2]};
        }

        for (int i = 0;i < vertices.length; i+=3){
            mesh.getPoints().addAll(vertices[i]);
            mesh.getPoints().addAll(vertices[i+1]);
            mesh.getPoints().addAll(vertices[i+2]);
            //add 3 points

            mesh.getFaces().addAll(i,0,  i+1,0,  i+2,0 ); //Add those three points as face
        }
        return mesh;
    }

    private TriangleMesh[] fromGLTFMesh(GLTFMesh gltfMesh) throws GLTFParseException {

        TriangleMesh[] meshes = new TriangleMesh[gltfMesh.getPrimitives().length];

        for (int i = 0;i < meshes.length; i++) {
            TriangleMesh mesh = fromPrimitive(gltfMesh.getPrimitives()[i]);
            meshes[i] = mesh;
        }

        return meshes;
    }

    private Group fromGLTFMeshes(GLTFMesh gltfMesh) throws GLTFParseException {
        TriangleMesh[] meshes = fromGLTFMesh(gltfMesh);
        MeshView[] views = new MeshView[meshes.length];

        for (int i = 0;i < meshes.length; i++){
            views[i] = new MeshView(meshes[i]);
        }

        return new Group(views);
    }

    public static int getWidth(){
        return WIDTH_BASE * SIZE_FACTOR;
    }

    public static int getHeight(){
        return HEIGHT_BASER * SIZE_FACTOR;
    }

}
