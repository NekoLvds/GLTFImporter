package gltfImporter.animation;

import gltfImporter.GLTFNode;
import gltfImporter.GLTFParseException;
import gltfImporter.constants.GLTFAnimationPath;
import org.json.JSONObject;

public class GLTFAnimationChannelTarget {

    private final GLTFNode target;
    private final GLTFAnimationPath path;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFAnimationChannelTarget(GLTFNode target, GLTFAnimationPath path, JSONObject extensions, JSONObject extras) {
        this.target = target;
        this.path = path;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFNode getTarget() {
        return target;
    }

    public GLTFAnimationPath getPath() {
        return path;
    }

    public JSONObject getExtensions() {
        return extensions;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public static GLTFAnimationChannelTarget fromJsonObject(JSONObject obj, GLTFNode[] nodes) throws GLTFParseException {
        GLTFNode target = null;
        GLTFAnimationPath path;
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("target")){
            target = nodes[obj.getInt("target")];
        }
        if (obj.has("path")){
            path = GLTFAnimationPath.valueOf(obj.getString("path"));
        } else {
            throw new GLTFParseException("Field 'path' in 'AnimationChannelTarget' must be set but isn't");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFAnimationChannelTarget(
                target,
                path,
                extensions,
                extras
        );
    }
}
