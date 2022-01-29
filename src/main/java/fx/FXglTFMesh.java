package fx;

import gltf.GLTFParseException;
import gltf.material.GLTFMaterial;
import gltf.mesh.GLTFMesh;
import gltf.mesh.GLTFMeshPrimitive;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.CheckedOutputStream;

public class FXglTFMesh extends Group {

    public static FXglTFMesh fromGLTFMesh(GLTFMesh mesh) throws GLTFParseException {
        FXglTFMesh fxMesh = new FXglTFMesh();

        GLTFMeshPrimitive[] primitives = mesh.getPrimitives();

        MeshView[] meshViews = new MeshView[primitives.length];
        for (int i = 0;i < meshViews.length; i++){
            meshViews[i] = fromPrimitive(primitives[i]);
        }

        fxMesh.getChildren().addAll(meshViews);
        return fxMesh;
    }

    private static MeshView fromPrimitive(GLTFMeshPrimitive primitive) throws GLTFParseException {
        TriangleMesh mesh = new TriangleMesh();
        mesh.getTexCoords().addAll(primitive.getAttribute().getTexCoord_0().readDataAsFloats());

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

            mesh.getFaces().addAll(i, i+1, i+2);
        }

        MeshView view = new MeshView(mesh);

        view.setMaterial(getMaterial(primitive.getMaterial()));
        return view;
    }



    private static Material getMaterial(GLTFMaterial material) {
        PhongMaterial phongMaterial = new PhongMaterial();

        if(material.getPbrMetallicRoughness() != null) {
            if (material.getPbrMetallicRoughness().getBaseColorTexture().getTexture() != null) {
                phongMaterial.setDiffuseMap(SwingFXUtils.toFXImage(material.getPbrMetallicRoughness().getBaseColorTexture().getTexture().getImage().getBufferedImage(), null));
            }
        }

        return phongMaterial;
    }
}
