package fx;

import gltf.GLTFParseException;
import gltf.mesh.GLTFMesh;
import gltf.mesh.GLTFMeshPrimitive;
import javafx.scene.Group;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class FXglTFMesh extends Group {

    public static FXglTFMesh fromGLTFMesh(GLTFMesh mesh) throws GLTFParseException {
        FXglTFMesh fxMesh = new FXglTFMesh();


        GLTFMeshPrimitive[] primitives = mesh.getPrimitives();
        MeshView[] meshViews = new MeshView[primitives.length];
        for (int i = 0;i < meshViews.length; i++){
            meshViews[i] = fromPrimitive(primitives[i]);
        }

        FXglTFMaterial material;

        fxMesh.getChildren().addAll(meshViews);
        return fxMesh;
    }

    //TODO implement other meshes e.g. include normals
    private static MeshView fromPrimitive(GLTFMeshPrimitive primitive) throws GLTFParseException {
        TriangleMesh mesh = new TriangleMesh();
        MeshView view = new MeshView(mesh);

        mesh.getTexCoords().addAll(0,0);

        //Parse the vertices and faces
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

        //Material
        FXglTFMaterial material = FXglTFMaterial.fromGLTFMaterial(primitive.getMaterial());
        view.setMaterial(material);
        return view;
    }
}
