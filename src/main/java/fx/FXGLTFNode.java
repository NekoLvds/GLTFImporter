package fx;

import gltf.GLTFAsset;
import gltf.GLTFNode;
import javafx.scene.Group;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class FXGLTFNode extends Group {

    private MeshView meshView;

    public FXGLTFNode(GLTFAsset asset, GLTFNode self){
        FXGLTFMesh myMesh = new FXGLTFMesh(asset, self.getMesh());

        meshView = new MeshView(myMesh);

        this.getChildren().add(meshView);
    }


}
