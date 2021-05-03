package gltfImporter.animation;


import gltfImporter.GLTFParseException;
import gltfImporter.accessor.GLTFAccessor;
import gltfImporter.constants.GLTFInterpolationAlgorithm;
import org.json.JSONObject;

public class GLTFAnimationSampler {

    private final GLTFAccessor input;
    private final GLTFInterpolationAlgorithm interpolation;
    private final GLTFAccessor output;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFAnimationSampler(GLTFAccessor input, GLTFInterpolationAlgorithm interpolation, GLTFAccessor output, JSONObject extensions, JSONObject extras) {
        this.input = input;
        this.interpolation = interpolation;
        this.output = output;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFAccessor getInput() {
        return input;
    }

    public GLTFInterpolationAlgorithm getInterpolation() {
        return interpolation;
    }

    public GLTFAccessor getOutput() {
        return output;
    }

    public JSONObject getExtensions() {
        return extensions;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public static GLTFAnimationSampler fromJsonObject(JSONObject obj, GLTFAccessor[] accessors) throws GLTFParseException {
        GLTFAccessor input;
        GLTFInterpolationAlgorithm algorithm = GLTFInterpolationAlgorithm.LINEAR;
        GLTFAccessor output;
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("input")){
            input = accessors[obj.getInt("input")];
        } else {
            throw new GLTFParseException("The field 'input' in 'AnimationSampler' is not set but is required.");
        }
        if (obj.has("interpolation")){
            algorithm = GLTFInterpolationAlgorithm.valueOf(obj.getString("interpolation"));
        }
        if (obj.has("output")){
            output = accessors[obj.getInt("output")];
        } else {
            throw new GLTFParseException("The field 'output' is required but not set.");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFAnimationSampler(
                input,
                algorithm,
                output,
                extensions,
                extras
        );
    }
}
