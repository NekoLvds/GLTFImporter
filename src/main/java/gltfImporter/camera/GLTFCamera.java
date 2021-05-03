package gltfImporter.camera;

import gltfImporter.GLTFParseException;
import org.json.JSONObject;

public abstract class GLTFCamera {

    private final String type;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFCamera(String type, String name, JSONObject extensions, JSONObject extras) {
        this.type = type;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }

    public String getType() {
        return type;
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

    public static GLTFCamera fromJSONObject(JSONObject obj) throws GLTFParseException {
        String type = "";
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("type")){
            type = obj.getString("type");
        } else {
            throw new GLTFParseException("Field 'type' in 'Camera' is required but not set");
        }
        if (obj.has("name")){
            name = obj.getString("name");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        if (type.equalsIgnoreCase("perspective")){
            return GLTFPerspectiveCamera.fromJSONObject(
                    obj.getJSONObject("perspective"),
                    type,
                    name,
                    extensions,
                    extras
            );
        } else if (type.equalsIgnoreCase("orthographic")){
            return GLTFOrthographicCamera.fromJSONObject(
                    obj.getJSONObject("orthographic"),
                    type,
                    name,
                    extensions,
                    extras
            );
        } else {
            throw new GLTFParseException("Field 'type' in 'camera' must either be 'perspective' or 'orthographic'. " + type + " is not supported.");
        }
    }
}
