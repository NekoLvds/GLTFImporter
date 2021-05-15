package test;

import fx.FXglTFAsset;
import gltf.GLTFAsset;
import gltf.GLTFParseException;
import gltf.mesh.GLTFMesh;
import gltf.mesh.GLTFMeshPrimitive;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.File;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File(
                "C:\\Users\\Lucas\\IdeaProjects\\GLTFImporter\\src\\main\\resources\\glTF-Sample-Models\\2.0\\Fox\\glTF\\Fox.gltf");
        GLTFAsset asset = new GLTFAsset(file.toURI());

        //Group root = test();
        Group root = FXglTFAsset.fromGLTFAsset(asset);
        Scene scene = new Scene(root);
        Camera camera = new PerspectiveCamera(true);
        camera.setFarClip(10000);

        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        root.getTransforms().addAll(rotateX, rotateY);

        root.setTranslateZ(80);


        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case W:
                    root.setTranslateZ(root.getTranslateZ() - 10);
                    break;
                case S:
                    root.setTranslateZ(root.getTranslateZ() + 10);
                    break;
                case A:
                    root.setTranslateX(root.getTranslateX() - 10);
                    break;
                case D:
                    root.setTranslateX(root.getTranslateX() + 10);
                    break;
                case Q:
                    root.setTranslateY(root.getTranslateY() - 10);
                    break;
                case E:
                    root.setTranslateY(root.getTranslateY() + 10);
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
        });
        
        scene.setCamera(camera);
        primaryStage.setScene(scene);
        primaryStage.setHeight(500);
        primaryStage.setWidth(800);

        primaryStage.show();
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

    private Group test(){
        TriangleMesh mesh = new TriangleMesh();

        mesh.getPoints().addAll(
                -10f,   0f, -10f, //0
                -10f,   0f,  10f, //1
                 10f,   0f,  10f, //2
                 10f,   0f, -10f, //3
                 0f ,  10f,   0f  //4
        );

        mesh.getFaces().addAll(
                0, 0,  3, 0,  1, 0,
                1, 0,  3, 0,  2, 0,
                0, 0,  1, 0,  4, 0,
                1, 0,  2, 0,  4, 0,
                2, 0,  3, 0,  4, 0,
                3, 0,  0, 0,  4, 0
        );

        mesh.getTexCoords().addAll(0,0);

        MeshView view = new MeshView(mesh);

        return new Group(view);
    }

}
