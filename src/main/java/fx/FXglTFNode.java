package fx;

import gltf.GLTFNode;
import gltf.GLTFParseException;
import javafx.scene.Group;

public class FXglTFNode extends Group {

    public static FXglTFNode fromGLTFNode(GLTFNode node) throws GLTFParseException {
        FXglTFNode fxNode = new FXglTFNode();

        if (node.getMesh() != null){
            FXglTFMesh mesh = FXglTFMesh.fromGLTFMesh(node.getMesh());
            fxNode.getChildren().addAll(mesh);
        }
        return fxNode;
    }
}
