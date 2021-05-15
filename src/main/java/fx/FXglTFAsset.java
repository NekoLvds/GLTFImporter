package fx;

import gltf.GLTFAsset;
import gltf.GLTFNode;
import gltf.GLTFParseException;
import javafx.scene.Group;
import javafx.scene.shape.TriangleMesh;

public class FXglTFAsset extends Group {



    public static FXglTFAsset fromGLTFAsset(GLTFAsset asset) throws GLTFParseException {
        FXglTFAsset fxAsset = new FXglTFAsset();

        for (GLTFNode node : asset.getNodes()){
            fxAsset.getChildren().add(FXglTFNode.fromGLTFNode(node));
        }
        return fxAsset;
    }
}
