package fx;

import gltf.GLTFParseException;
import gltf.mesh.GLTFMesh;
import gltf.mesh.GLTFMeshPrimitive;
import javafx.scene.Group;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.Arrays;

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

        //Reading texture coords
        float[][] texCoords = convertArrayToNested(2, primitive.getAttribute().getTexCoord_0().readDataAsFloats());
        System.out.println(texCoords.length);
        mesh.getTexCoords().addAll(primitive.getAttribute().getTexCoord_0().readDataAsFloats());

        //Parse the vertices and faces
        float[][] vertices = convertArrayToNested(3, primitive.getAttribute().getPosition().readDataAsFloats());

        for (int i = 0;i < vertices.length; i+=3){
            mesh.getPoints().addAll(vertices[i]);
            mesh.getPoints().addAll(vertices[i+1]);
            mesh.getPoints().addAll(vertices[i+2]);
            //add 3 points

            mesh.getFaces().addAll(i,i,  i+1,i+1,  i+2,i+2 ); //Add those three points as face
        }

        //Material
        FXglTFMaterial material = FXglTFMaterial.fromGLTFMaterial(primitive.getMaterial());
        view.setMaterial(material);
        return view;
    }

    private static float[][] convertArrayToNested(int factor, float[] array){
        float[][] floats = new float[array.length / factor][];

        for (int i = 0;i < floats.length; i++){
            int dataOffset = i * factor;
            floats[i] = Arrays.copyOfRange(array, dataOffset, dataOffset+factor);
        }

        return floats;
    }
}
