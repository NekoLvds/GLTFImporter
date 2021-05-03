package gltf.material;

import gltf.GLTFParseException;
import gltf.constants.GLTFMaterialAlphaMode;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * GLTFMaterial represents a material defined in the gltf asset json and stores the associated binary data.
 */
public class GLTFMaterial {

    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;
    private final GLTFPbrMetallicRoughness pbrMetallicRoughness;
    private final GLTFNormalTextureInfo normalTexture;
    private final GLTFOcclusionTextureInfo occlusionTexture;
    private final GLTFTextureInfo emissiveTexture;
    private final float[] emissiveFactor;
    private final GLTFMaterialAlphaMode alphaMode;
    private final float alphaCutoff;
    private final boolean doubleSided;

    public GLTFMaterial(String name, JSONObject extensions, JSONObject extras, GLTFPbrMetallicRoughness pbrMetallicRoughness, GLTFNormalTextureInfo normalTexture, GLTFOcclusionTextureInfo occlusionTexture, GLTFTextureInfo emissiveTexture, float[] emissiveFactor, GLTFMaterialAlphaMode alphaMode, float alphaCutoff, boolean doubleSided) {
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
        this.pbrMetallicRoughness = pbrMetallicRoughness;
        this.normalTexture = normalTexture;
        this.occlusionTexture = occlusionTexture;
        this.emissiveTexture = emissiveTexture;
        this.emissiveFactor = emissiveFactor;
        this.alphaMode = alphaMode;
        this.alphaCutoff = alphaCutoff;
        this.doubleSided = doubleSided;
    }

    public String getName() {
        return name;
    }

    public JSONObject getExtensions() {
        return extensions;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public GLTFPbrMetallicRoughness getPbrMetallicRoughness() {
        return pbrMetallicRoughness;
    }

    public GLTFNormalTextureInfo getNormalTexture() {
        return normalTexture;
    }

    public GLTFOcclusionTextureInfo getOcclusionTexture() {
        return occlusionTexture;
    }

    public GLTFTextureInfo getEmissiveTexture() {
        return emissiveTexture;
    }

    public float[] getEmissiveFactor() {
        return emissiveFactor;
    }

    public GLTFMaterialAlphaMode getAlphaMode() {
        return alphaMode;
    }

    public float getAlphaCutoff() {
        return alphaCutoff;
    }

    public boolean isDoubleSided() {
        return doubleSided;
    }

    /**
     * Initializes the gltf material from the json object.
     * @param obj The Json object
     * @param textures The initialized textures.
     * @return The initialized material
     * @throws GLTFParseException If the Json is malformed.
     */
    public static GLTFMaterial fromJSONObject(JSONObject obj, GLTFTexture[] textures) throws GLTFParseException {
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;
        GLTFPbrMetallicRoughness metallicRoughness = GLTFPbrMetallicRoughness.DEFAULTPBRMETALLICROUGHNESS;
        GLTFNormalTextureInfo normalTexture = null;
        GLTFOcclusionTextureInfo occlusionTexture = null;
        GLTFTextureInfo emissiveTexture = null;
        float[] emissiveFactor = new float[]{0,0,0};
        GLTFMaterialAlphaMode alphaMode = GLTFMaterialAlphaMode.OPAQUE;
        float alphaCutoff = 0.5f;
        boolean doubleSided = false;

        if (obj.has("name")){
            name = obj.getString("name");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }
        if (obj.has("pbrMetallicRoughness")){
            metallicRoughness = GLTFPbrMetallicRoughness.fromJSONObject(obj.getJSONObject("pbrMetallicRoughness"), textures);
        }
        if (obj.has("normalTexture")){
            normalTexture = GLTFNormalTextureInfo.fromJSONObject(obj.getJSONObject("normalTexture"), textures);
        }
        if (obj.has("occlusionTexture")){
            occlusionTexture = GLTFOcclusionTextureInfo.fromJSONObject(obj.getJSONObject("occlusionTexture"), textures);
        }
        if (obj.has("emissiveTexture")){
            emissiveTexture = GLTFTextureInfo.fromJSONObject(obj.getJSONObject("emissiveTexture"), textures);
        }
        if (obj.has("emissiveFactor")){
            JSONArray jsonArray = obj.getJSONArray("emissiveFactor");

            if (jsonArray.length() != 3){
                throw new GLTFParseException("The 'emissiveFactor' Array in 'Material' must be of size 3, is: " + jsonArray.length() + ". This indicates a corrupted file.");
            }

            for (int i = 0;i < jsonArray.length(); i++){
                emissiveFactor[i] = jsonArray.getBigDecimal(i).floatValue();
            }
        }
        if (obj.has("alphaMode")){
            alphaMode = GLTFMaterialAlphaMode.valueOf(obj.getString("alphaMode"));
        }
        if (obj.has("alphaCutoff")){
            alphaCutoff = obj.getBigDecimal("alphaCutoff").floatValue();
        }
        if (obj.has("doubleSided")){
            doubleSided = obj.getBoolean("doubleSided");
        }

        return new GLTFMaterial(
                name,
                extensions,
                extras,
                metallicRoughness,
                normalTexture,
                occlusionTexture,
                emissiveTexture,
                emissiveFactor,
                alphaMode,
                alphaCutoff,
                doubleSided
        );
    }
}
