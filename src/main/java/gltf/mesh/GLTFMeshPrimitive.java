package gltf.mesh;

import gltf.GLTFParseException;
import gltf.accessor.GLTFAccessor;
import gltf.buffer.GLTFBufferView;
import gltf.constants.GLTFMeshPrimitiveMode;
import gltf.material.GLTFMaterial;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;

public class GLTFMeshPrimitive {

    private final GLTFMeshPrimitiveAttribute attribute;
    private final GLTFAccessor indices;
    private final GLTFMaterial material;
    private final GLTFMeshPrimitiveMode mode;
    private final GLTFMeshPrimitiveAttribute[] targets;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFMeshPrimitive(GLTFMeshPrimitiveAttribute attribute, GLTFAccessor indices, GLTFMaterial material, GLTFMeshPrimitiveMode mode, GLTFMeshPrimitiveAttribute[] targets, JSONObject extensions, JSONObject extras) {
        this.attribute = attribute;
        this.indices = indices;
        this.material = material;
        this.mode = mode;
        this.targets = targets;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFMeshPrimitiveAttribute getAttribute() {
        return attribute;
    }

    public GLTFAccessor getIndices() {
        return indices;
    }

    public GLTFMaterial getMaterial() {
        return material;
    }

    public GLTFMeshPrimitiveMode getMode() {
        return mode;
    }

    public GLTFMeshPrimitiveAttribute[] getTargets() {
        return targets;
    }

    public JSONObject getExtensions() {
        return extensions;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public static GLTFMeshPrimitive fromJSONObject(JSONObject obj, GLTFAccessor[] accessors, GLTFBufferView[] bufferViews, GLTFMaterial[] materials) throws GLTFParseException, ExecutionControl.NotImplementedException {
        GLTFMeshPrimitiveAttribute attributes = null;
        GLTFAccessor indices = null;
        GLTFMaterial material = null;
        GLTFMeshPrimitiveMode mode = GLTFMeshPrimitiveMode.fromID(4);
        GLTFMeshPrimitiveAttribute[] targets = null;
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("attributes")){
            GLTFMeshPrimitiveAttribute.fromJSONObject(obj.getJSONObject("attributes"), accessors);
        } else{
            //required field
            throw new GLTFParseException("Field 'attributes' in 'mesh primitives' is required but not set.");
        }
        if (obj.has("indices")){
            indices = GLTFAccessor.fromJSONObject(obj.getJSONObject("indices"), bufferViews);
        }
        if (obj.has("material")){
            material = materials[obj.getInt("material")];
        }
        if (obj.has("mode")){
            mode = GLTFMeshPrimitiveMode.fromID(obj.getInt("mode"));
        }
        if (obj.has("targets")){
            JSONArray array = obj.getJSONArray("targets");
            targets = new GLTFMeshPrimitiveAttribute[array.length()];

            for (int i = 0;i < array.length(); i++){
                targets[i] = GLTFMeshPrimitiveAttribute.fromJSONObject(array.getJSONObject(i), accessors);
            }
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFMeshPrimitive(
                attributes,
                indices,
                material,
                mode,
                targets,
                extensions,
                extras
        );
    }
}
