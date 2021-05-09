package gltf.mesh;

import gltf.GLTFParseException;
import gltf.accessor.GLTFAccessor;
import gltf.buffer.GLTFBufferView;
import gltf.material.GLTFMaterial;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;

public class GLTFMesh {

    private final GLTFMeshPrimitive[] primitives;
    private final float[] weights;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFMesh(GLTFMeshPrimitive[] primitives, float[] weights, String name, JSONObject extensions, JSONObject extras) {
        this.primitives = primitives;
        this.weights = weights;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }



    public GLTFMeshPrimitive[] getPrimitives() {
        return primitives;
    }

    public float[] getWeights() {
        return weights;
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

    public static GLTFMesh fromJSONObject(JSONObject obj, GLTFAccessor[] accessors, GLTFBufferView[] bufferViews, GLTFMaterial[] materials) throws GLTFParseException, ExecutionControl.NotImplementedException {
        GLTFMeshPrimitive[] primitives = null;
        float[] weights = null;
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("primitives")){
            JSONArray array = obj.getJSONArray("primitives");
            primitives = new GLTFMeshPrimitive[array.length()];

            for (int i = 0;i < array.length(); i++){
                primitives[i] = GLTFMeshPrimitive.fromJSONObject(array.getJSONObject(i), accessors, bufferViews, materials);
            }
        }else{
            throw new GLTFParseException("Field 'primitives' in 'Mesh' must be set but isn't");
        }
        if (obj.has("weights")){
            JSONArray array = obj.getJSONArray("weights");
            weights = new float[array.length()];

            for (int i = 0;i < array.length(); i++){
                weights[i] = array.getBigDecimal(i).floatValue();
            }
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

        return new GLTFMesh(
                primitives,
                weights,
                name,
                extensions,
                extras
        );
    }
}
