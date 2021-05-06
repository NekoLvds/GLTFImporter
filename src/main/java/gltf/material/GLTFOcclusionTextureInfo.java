package gltf.material;

import gltf.GLTFParseException;
import org.json.JSONObject;

public class GLTFOcclusionTextureInfo extends GLTFTextureInfo{

    private final float strength;

    public GLTFOcclusionTextureInfo(GLTFTexture texture, int texCoordIndex, JSONObject extensions, JSONObject extras, float strength) {
        super(texture, texCoordIndex, extensions, extras);
        this.strength = strength;
    }

    public float getStrength() {
        return strength;
    }

    public static GLTFOcclusionTextureInfo fromJSONObject(JSONObject obj, GLTFTexture[] textures) throws GLTFParseException {
        GLTFTextureInfo temp = GLTFTextureInfo.fromJSONObject(obj, textures);

        float strength = 1;

        if (obj.has("strength")){
            strength = obj.getBigDecimal("strength").floatValue();
        }

        return new GLTFOcclusionTextureInfo(
                temp.getTexture(),
                temp.getTexCoordIndex(),
                temp.getExtensions(),
                temp.getExtras(),
                strength
        );
    }
}
