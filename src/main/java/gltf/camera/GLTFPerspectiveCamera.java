package gltf.camera;

import gltf.GLTFParseException;
import org.json.JSONObject;

public class GLTFPerspectiveCamera extends GLTFCamera{

    private final float aspectRatio;
    private final float yfov;
    private final float zfar;
    private final float znear;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFPerspectiveCamera(String type, String name, JSONObject extensions, JSONObject extras, float aspectRatio, float yfov, float zfar, float znear, JSONObject extensions1, JSONObject extras1) {
        super(type, name, extensions, extras);
        this.aspectRatio = aspectRatio;
        this.yfov = yfov;
        this.zfar = zfar;
        this.znear = znear;
        this.extensions = extensions1;
        this.extras = extras1;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public float getYfov() {
        return yfov;
    }

    public float getZfar() {
        return zfar;
    }

    public float getZnear() {
        return znear;
    }

    @Override
    public JSONObject getExtensions() {
        return extensions;
    }

    @Override
    public JSONObject getExtras() {
        return extras;
    }

    public static GLTFPerspectiveCamera fromJSONObject(JSONObject obj, String type, String name, JSONObject parentExtensions, JSONObject parentExtras) throws GLTFParseException {
        float aspectRatio = -1;
        float yfov;
        float zfar = 0;
        float znear;
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("aspectRatio")){
            aspectRatio = obj.getBigDecimal("aspectRatio").floatValue();
        }
        if (obj.has("yfov")){
            yfov = obj.getBigDecimal("yfov").floatValue();
        } else {
            throw new GLTFParseException("Field 'yfov' in 'Perspective Camera' is required but not set");
        }
        if (obj.has("zfar")){
            zfar = obj.getBigDecimal("zfar").floatValue();
        }
        if (obj.has("znear")){
            znear = obj.getBigDecimal("znear").floatValue();
        } else {
            throw new GLTFParseException("Field 'znear' in 'Perspective Camera' is required but not set");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFPerspectiveCamera(
                type,
                name,
                parentExtensions,
                parentExtras,
                aspectRatio,
                yfov,
                zfar,
                znear,
                extensions,
                extras
        );
    }
}
