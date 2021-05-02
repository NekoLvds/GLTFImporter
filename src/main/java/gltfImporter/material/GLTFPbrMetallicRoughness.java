package gltfImporter.material;

import gltfImporter.GLTFParseException;
import org.json.JSONArray;
import org.json.JSONObject;

public class GLTFPbrMetallicRoughness {

    private final float[] baseColorFactor;
    private final GLTFTextureInfo baseColorTexture;
    private final float metallicFactor;
    private final float roughnessFactor;
    private final GLTFTextureInfo metallicRoughnessTexture;
    private final JSONObject extensions;
    private final JSONObject extras;

    public static final GLTFPbrMetallicRoughness DEFAULTPBRMETALLICROUGHNESS = new GLTFPbrMetallicRoughness(
            new float[]{1,1,1,1},
            null,
            1,
            1,
            null,
            null,
            null
    );

    public GLTFPbrMetallicRoughness(float[] baseColorFactor, GLTFTextureInfo baseColorTexture, float metallicFactor, float roughnessFactor, GLTFTextureInfo metallicRoughnessTexture, JSONObject extensions, JSONObject extras) {
        this.baseColorFactor = baseColorFactor;
        this.baseColorTexture = baseColorTexture;
        this.metallicFactor = metallicFactor;
        this.roughnessFactor = roughnessFactor;
        this.metallicRoughnessTexture = metallicRoughnessTexture;
        this.extensions = extensions;
        this.extras = extras;
    }

    public float[] getBaseColorFactor() {
        return baseColorFactor;
    }

    public GLTFTextureInfo getBaseColorTexture() {
        return baseColorTexture;
    }

    public float getMetallicFactor() {
        return metallicFactor;
    }

    public float getRoughnessFactor() {
        return roughnessFactor;
    }

    public GLTFTextureInfo getMetallicRoughnessTexture() {
        return metallicRoughnessTexture;
    }

    public JSONObject getExtensions() {
        return extensions;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public static GLTFPbrMetallicRoughness fromJSONObject(JSONObject obj, GLTFTexture[] textures) throws GLTFParseException {
        float[] baseColorFactor = new float[]{1,1,1,1};
        GLTFTextureInfo baseColorTexture = null;
        float metallicFactor = 1;
        float roughnessFactor = 1;
        GLTFTextureInfo metallicRoughnessTexture = null;
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("baseColorFactor")){
            JSONArray factors = obj.getJSONArray("baseColorFactors");

            if (factors.length() != 4){ //Length 4 is required by GLTF specification
                throw new GLTFParseException("'baseColorFactor' must be a number array of length 4. Length: " + factors.length() + " indicates a corrupted file.");
            }

            for(int i = 0;i < factors.length(); i++){
                baseColorFactor[i] = factors.getBigDecimal(i).floatValue();
            }
        }
        if (obj.has("baseColorTexture")){
            baseColorTexture = GLTFTextureInfo.fromJSONObject(obj.getJSONObject("baseColorTexture"), textures);
        }
        if (obj.has("metallicFactor")){
            metallicFactor = obj.getBigDecimal("metallicFactor").floatValue();
        }
        if (obj.has("roughnessFactor")){
            roughnessFactor = obj.getBigDecimal("roughnessFactor").floatValue();
        }
        if (obj.has("metallicRoughnessTexture")){
            metallicRoughnessTexture = GLTFTextureInfo.fromJSONObject(obj.getJSONObject("metallicroughnessTexture"), textures);
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFPbrMetallicRoughness(
                baseColorFactor,
                baseColorTexture,
                metallicFactor,
                roughnessFactor,
                metallicRoughnessTexture,
                extensions,
                extras
        );
    }
}
