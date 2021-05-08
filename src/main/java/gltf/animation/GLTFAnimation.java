package gltf.animation;

import gltf.GLTFNode;
import gltf.GLTFParseException;
import gltf.accessor.GLTFAccessor;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * GLTFAnimation represents the animation data defined the JSON.
 *
 * <p></p>
 *
 * Note: this does not do any animations it just stores it in a usable format.
 */
public class GLTFAnimation {

    private final GLTFChannel[] channels;
    private final GLTFAnimationSampler[] samplers;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFAnimation(GLTFChannel[] channels, GLTFAnimationSampler[] samplers, String name, JSONObject extensions, JSONObject extras) {
        this.channels = channels;
        this.samplers = samplers;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFChannel[] getChannels() {
        return channels;
    }

    public GLTFAnimationSampler[] getSamplers() {
        return samplers;
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

    public static GLTFAnimation fromJsonObject(JSONObject obj, GLTFAccessor[] accessors, GLTFNode[] nodes) throws GLTFParseException {
        GLTFChannel[] channels;
        GLTFAnimationSampler[] animationSamplers;
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("samplers")){
            JSONArray array = obj.getJSONArray("samplers");
            animationSamplers = new GLTFAnimationSampler[array.length()];

            for (int i = 0; i < array.length(); i++) {
                animationSamplers[i] = GLTFAnimationSampler.fromJsonObject(array.getJSONObject(i), accessors);
            }
        } else {
            throw new GLTFParseException("Field missing!");
        }
        if (obj.has("channels")) {
            JSONArray array = obj.getJSONArray("channels");
            channels = new GLTFChannel[array.length()];

            for (int i = 0; i < array.length(); i++) {
                channels[i] = GLTFChannel.fromJsonObject(array.getJSONObject(i), animationSamplers, nodes);
            }
        } else {
            throw new GLTFParseException("Field 'channels' in 'Animation' must be set but isn't");
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

        return new GLTFAnimation(
                channels,
                animationSamplers,
                name,
                extensions,
                extras
        );
    }
}
