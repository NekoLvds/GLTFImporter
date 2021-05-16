package fx;

import gltf.material.GLTFMaterial;
import gltf.material.GLTFPbrMetallicRoughness;
import gltf.material.GLTFTexture;
import gltf.material.GLTFTextureInfo;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.paint.PhongMaterial;

import java.awt.image.BufferedImage;

public class FXglTFMaterial extends PhongMaterial {

    public static FXglTFMaterial fromGLTFMaterial(GLTFMaterial material){
        FXglTFMaterial fxMaterial = new FXglTFMaterial();


        //FIXME texture isn't correctly read.
        fxMaterial.setDiffuseMap(SwingFXUtils.toFXImage(material.getPbrMetallicRoughness().getBaseColorTexture().getTexture().getImage().getBufferedImage(), null));

        return fxMaterial;
    }
}
