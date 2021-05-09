package fx;

import gltf.GLTFAsset;
import gltf.GLTFNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;

public class GLTFFXAsset {

    Group root;

    public GLTFFXAsset(GLTFAsset asset) {

        root = new Group();

        root.getChildren().addAll(createTestNodes(asset.getNodes()));

        root.setScaleZ(-10);
        root.setScaleY(-10);
        root.setScaleX(10);

        //root.getChildren().add(new Box(15, 15, 15));
    }

    private Node[] createTestNodes(GLTFNode[] nodes){
        Node[] fxNodes = new Node[nodes.length];

        for (int i = 0;i < fxNodes.length; i++){
            Sphere sphere = new Sphere();

            float[] trans = nodes[i].getTranslation();
            sphere.setTranslateX(trans[0]);
            sphere.setTranslateY(trans[1]);
            sphere.setTranslateZ(trans[2]);

            System.out.println("X: " + trans[0]);
            System.out.println("Y: " + trans[1]);
            System.out.println("Z: " + trans[2]);

            sphere.setRadius(1);

            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(Color.BLACK);
            material.setSpecularColor(Color.BLACK);

            sphere.setMaterial(material);

            fxNodes[i] = sphere;
        }

        System.out.println(fxNodes.length);

        return fxNodes;
    }

    public Group getRoot() {
        return root;
    }
}
