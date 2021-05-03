package gltf.camera;

import gltf.GLTFParseException;
import org.json.JSONObject;

public class GLTFOrthographicCamera extends GLTFCamera{

    private final float xmag;
    private final float ymag;
    private final float zfar;
    private final float znear;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFOrthographicCamera(String type, String name, JSONObject extensions, JSONObject extras, float xmag, float ymag, float zfar, float znear, JSONObject extensions1, JSONObject extras1) {
        super(type, name, extensions, extras);
        this.xmag = xmag;
        this.ymag = ymag;
        this.zfar = zfar;
        this.znear = znear;
        this.extensions = extensions1;
        this.extras = extras1;
    }

    public float getXmag() {
        return xmag;
    }

    public float getYmag() {
        return ymag;
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

    public static GLTFOrthographicCamera fromJSONObject(JSONObject obj, String type, String name, JSONObject parentExtensions, JSONObject parentExtras) throws GLTFParseException {
        float xmag;
        float ymag;
        float zfar;
        float znear;
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("xmag")){
            xmag = obj.getBigDecimal("xmag").floatValue();
        } else {
            throw new GLTFParseException("Field 'xmag' in 'Orthographic Camera' is required but not set");
        }
        if (obj.has("ymag")){
            ymag = obj.getBigDecimal("ymag").floatValue();
        } else {
            throw new GLTFParseException("Field 'ymag' in 'Orthographic Camera' is required but not set");
        }
        if (obj.has("zfar")){
            zfar = obj.getBigDecimal("zfar").floatValue();
        } else {
            throw new GLTFParseException("Field 'zfar' in 'Orthographic Camera' is required but not set");
        }
        if (obj.has("znear")){
            znear = obj.getBigDecimal("znear").floatValue();
        } else {
            throw new GLTFParseException("Field 'znear' in 'Orthographic Camera' is required but not set");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFOrthographicCamera(
                type,
                name,
                parentExtensions,
                parentExtras,
                xmag,
                ymag,
                zfar,
                znear,
                extensions,
                extras
        );
    }
}
