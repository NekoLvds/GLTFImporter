package fx;

import gltf.GLTFAsset;
import gltf.GLTFNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;

public class FXGLTFAsset {

    Group root;


    public FXGLTFAsset(GLTFAsset asset) {

    }



    public Group getRoot() {
        return root;
    }
}
