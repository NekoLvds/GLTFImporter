package gltfImporter.material;

import gltfImporter.GLTFParseException;
import org.json.JSONObject;

public class GLTFNormalTextureInfo extends GLTFTextureInfo{

    private final float scale;

    public GLTFNormalTextureInfo(GLTFTexture texture, int texCoordIndex, JSONObject extensions, JSONObject extras, float scale) {
        super(texture, texCoordIndex, extensions, extras);
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public static GLTFNormalTextureInfo fromJSONObject(JSONObject obj, GLTFTexture[] textures) throws GLTFParseException {
        GLTFTextureInfo temp = GLTFTextureInfo.fromJSONObject(obj, textures);

        float scale = 1;


        if (obj.has("scale")){
            scale = obj.getBigDecimal("scale").floatValue();
        }

        return new GLTFNormalTextureInfo(
                temp.getTexture(),
                temp.getTexCoordIndex(),
                temp.getExtensions(),
                temp.getExtras(),
                scale
        );
    }
}
