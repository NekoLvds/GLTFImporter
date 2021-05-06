package gltf.animation;

import gltf.GLTFNode;
import gltf.GLTFParseException;
import org.json.JSONObject;

public class GLTFChannel {

    private final GLTFAnimationSampler sampler;
    private final GLTFAnimationChannelTarget target;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFChannel(GLTFAnimationSampler sampler, GLTFAnimationChannelTarget target, JSONObject extensions, JSONObject extras) {
        this.sampler = sampler;
        this.target = target;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFAnimationSampler getSampler() {
        return sampler;
    }

    public GLTFAnimationChannelTarget getTarget() {
        return target;
    }

    public JSONObject getExtensions() {
        return extensions;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public static GLTFChannel fromJsonObject(JSONObject obj, GLTFAnimationSampler[] samplers, GLTFNode[] nodes) throws GLTFParseException {
        GLTFAnimationSampler sampler;
        GLTFAnimationChannelTarget target;
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("sampler")){
            sampler = samplers[obj.getInt("sampler")];
        } else {
            throw new GLTFParseException("Field 'sampler' in 'Channel' must be set but isn't");
        }
        if (obj.has("target")){
            target = GLTFAnimationChannelTarget.fromJsonObject(obj.getJSONObject("target"), nodes);
        } else {
            throw new GLTFParseException("Field 'target' in 'Channel' must be set but isn't");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFChannel(
                sampler,
                target,
                extensions,
                extras
        );
    }
}
