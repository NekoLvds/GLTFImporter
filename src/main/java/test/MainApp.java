package test;

import fx.FXGLTFAsset;
import fx.FXGLTFMesh;
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
        Group root = fromGLTFMeshes(asset.getMeshes()[0]);
        Scene scene = new Scene(root);
        Camera camera = new PerspectiveCamera(true);
        camera.setFarClip(10000);

        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        root.getTransforms().addAll(rotateX, rotateY);

        root.setTranslateZ(500);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case W:
                    root.setTranslateZ(root.getTranslateZ() - 100);
                    break;
                case S:
                    root.setTranslateZ(root.getTranslateZ() + 100);
                    break;
                case A:
                    root.setTranslateX(root.getTranslateX() - 100);
                    break;
                case D:
                    root.setTranslateX(root.getTranslateX() + 100);
                    break;
                case Q:
                    root.setTranslateY(root.getTranslateY() - 100);
                    break;
                case E:
                    root.setTranslateY(root.getTranslateY() + 100);
                    break;
                case UP:
                    rotateX.setAngle(rotateX.getAngle() + 20);
                    break;
                case DOWN:
                    rotateX.setAngle(rotateX.getAngle() - 20);
                    break;
                case LEFT:
                    rotateY.setAngle(rotateY.getAngle() + 20);
                    break;
                case RIGHT:
                    rotateY.setAngle(rotateY.getAngle() - 20);
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

        float[] vertices = primitive.getAttribute().getPosition().readDataAsFloats();
        int numOfFaces = vertices.length / 3;


        for (int i = 0;i < numOfFaces;i += 3){
            mesh.getPoints().addAll(vertices[i], vertices[i+1], vertices[i+2]);
            mesh.getFaces().addAll(i,0, i+1,0, i+2,0);
        }

        return mesh;
    }

    private TriangleMesh[] fromGLTFMesh(GLTFMesh gltfMesh) throws GLTFParseException {

        System.out.println(gltfMesh.getPrimitives().length);

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
                -10f,   0f, -10f,
                -10f,   0f,  10f,
                 10f,   0f,  10f,
                 10f,   0f, -10f,
                 0f ,  10f,   0f
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
