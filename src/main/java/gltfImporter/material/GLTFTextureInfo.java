package gltfImporter.material;

import gltfImporter.GLTFParseException;
import org.json.JSONObject;

public class GLTFTextureInfo {

    private final GLTFTexture texture;
    private final int texCoordIndex;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFTextureInfo(GLTFTexture texture, int texCoordIndex, JSONObject extensions, JSONObject extras) {
        this.texture = texture;
        this.texCoordIndex = texCoordIndex;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFTexture getTexture() {
        return texture;
    }

    public int getTexCoordIndex() {
        return texCoordIndex;
    }

    public JSONObject getExtensions() {
        return extensions;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public static GLTFTextureInfo fromJSONObject(JSONObject obj, GLTFTexture[] textures) throws GLTFParseException {
        GLTFTexture texture;
        int texCoordIndex = 0;
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("index")){
            texture = textures[obj.getInt("index")];
        }else{
            //required field
            throw new GLTFParseException("Field 'index(=texture)' in 'TextureInfo' is required but not set");
        }
        if (obj.has("texCoord")){
            texCoordIndex = obj.getInt("texCoord");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFTextureInfo(
                texture,
                texCoordIndex,
                extensions,
                extras
        );
    }
}
