package test;

import fx.GLTFFXAsset;
import gltf.GLTFAsset;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File(
                "C:\\Users\\Lucas\\IdeaProjects\\GLTFImporter\\src\\main\\resources\\glTF-Sample-Models\\2.0\\Fox\\glTF\\Fox.gltf");
        GLTFAsset asset = new GLTFAsset(file.toURI());
        GLTFFXAsset fxAsset = new GLTFFXAsset(asset);

        Group root = fxAsset.getRoot();
        Scene scene = new Scene(root);
        Camera camera = new PerspectiveCamera(true);
        camera.setFarClip(10000);

        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        root.getTransforms().addAll(rotateX, rotateY);

        root.setTranslateZ(2500);

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

}
