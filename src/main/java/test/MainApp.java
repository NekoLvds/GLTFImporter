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
import javafx.stage.Screen;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainApp extends Application {

    public static int WIDTH_BASE = 16;
    public static int HEIGHT_BASER = 9;

    public static int SIZE_FACTOR = 100;

    public static float DEFAULT_ROT_X = 200;
    public static float DEFAULT_ROT_Y = 20;
    public static float DEFAULT_TRANS_X = 0;
    public static float DEFAULT_TRANS_Y = 40;
    public static float DEFAULT_TRANS_Z = 320;

    public Map<String, Map<String, Float>> defaults;

    private long timestamp = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initDefaults();
        Scene scene = this.createScene();

        primaryStage.setScene(scene);
        primaryStage.setHeight(getHeight());
        primaryStage.setWidth(getWidth());

        primaryStage.show();
    }

    private void initDefaults(){
        defaults = new HashMap<>();
        //rotx, roty, transx,y,z, scale
        defaults.put("Fox.gltf", Stream.of(new Object[][]{
                {"rotX"     , 200f},
                {"rotY"     , 20f},
                {"transX"   , 0f},
                {"transY"   , 40f},
                {"transZ"   , 320f},
                {"scale"    , 1f}
        }).collect(Collectors.toMap(data -> (String)data[0], data -> (Float)data[1])));

        defaults.put("FlightHelmet.gltf", Stream.of(new Object[][]{
                {"rotX"     , -180f},
                {"rotY"     , 0f},
                {"transX"   , 0f},
                {"transY"   , 40f},
                {"transZ"   , 200f},
                {"scale"    , 100f}
        }).collect(Collectors.toMap(data -> (String)data[0], data -> (Float)data[1])));
    }

    private Scene createScene(){

        Camera cam3d = new PerspectiveCamera(true);

        Camera cam2d = new PerspectiveCamera();

        StackPane sceneRoot = new StackPane();

        Scene scene = new Scene(
                sceneRoot,
                Screen.getPrimary().getBounds().getWidth(),
                Screen.getPrimary().getBounds().getHeight(),
                true,
                SceneAntialiasing.BALANCED
                );

        PerformanceTracker tracker = PerformanceTracker.getSceneTracker(scene);

        Translate translate = new Translate();
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        Scale scale = new Scale();

        Group root3d = new Group();
        SubScene scene3d = new SubScene(root3d, getWidth(), getHeight());
        scene3d.setDepthTest(DepthTest.ENABLE);

        Group root2d = create2dElement(root3d, translate, rotateX, rotateY, scale, scene3d, tracker);
        SubScene scene2d = new SubScene(root2d, getWidth(), getHeight());

        root3d.getTransforms().addAll(translate, rotateX, rotateY, scale);

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

    private Group create2dElement(Group ref3d, Translate translate, Rotate rotateX, Rotate rotateY, Scale scale, SubScene snapshotTarget, PerformanceTracker tracker){
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(20,10,10,10));

        Map<String, URI> data = gltfAssetMap();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(data.keySet());
        comboBox.getItems().sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //todo
                Map<String, Float> values = defaults.get(comboBox.getSelectionModel().getSelectedItem());
                if (values != null){
                    translate.setX(values.get("transX"));
                    translate.setY(values.get("transY"));
                    translate.setZ(values.get("transZ"));

                    rotateX.setAngle(values.get("rotX"));
                    rotateY.setAngle(values.get("rotY"));

                    scale.setX(values.get("scale"));
                    scale.setY(values.get("scale"));
                    scale.setZ(values.get("scale"));
                }
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
        VBox fpsBox = new VBox(fpsLabel, fpsValue);


        GridPane memoryPane = new GridPane();
        memoryPane.setHgap(10);

        Label totalMemLabel = new Label();
        totalMemLabel.setText(Long.toString(Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB");
        memoryPane.addColumn(0, new Label("TotalMem: "), totalMemLabel);

        Label usedMemBytesLabel = new Label();
        memoryPane.addColumn(1, new Label("Used bytes: "), usedMemBytesLabel);


        long totalMem = Runtime.getRuntime().totalMemory();
        long oneSecondAsNano = 1000000000;
        AnimationTimer updater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                fpsValue.setText(String.format("%.3f",tracker.getAverageFPS()));

                long usedMem = totalMem - Runtime.getRuntime().freeMemory();

                usedMemBytesLabel.setText(Long.toString(usedMem / (1024 * 1024)) + " MB");

                if ((now - timestamp) > oneSecondAsNano * 2){
                    timestamp = now;
                    tracker.resetAverageFPS();
                }
            }
        };
        updater.start();

        HBox labelsBox = new HBox(xRotBox, yRotBox, transXBox, transYBox, transZBox, fpsBox, memoryPane);
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
        File baseUri = new File(System.getenv("APPDATA") + "\\gltfSamples\\");

        System.out.println(baseUri);
        System.out.println(baseUri.isDirectory());

        List<URI> uris = new LinkedList<>();

        if (baseUri.isDirectory() && baseUri.exists()){
            String[] assetDirs = baseUri.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return dir.isDirectory();
                }
            });

            for(String s : assetDirs){
                System.out.println(s);
                File file = new File(s);
                s += "\\glTF\\" + file.getName() + ".gltf";
                System.out.println(s);
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
