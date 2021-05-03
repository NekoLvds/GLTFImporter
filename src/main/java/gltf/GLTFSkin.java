package gltf;

import gltf.accessor.GLTFAccessor;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;

public class GLTFSkin {

    private final GLTFAccessor inverseBindMatrices;
    private GLTFNode skeleton;
    private final int skeletonIndex;
    private GLTFNode[] joints;
    private final int[] jointIndices;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFSkin(GLTFAccessor inverseBindMatrices, int skeletonIndex, int[] jointIndices, String name, JSONObject extensions, JSONObject extras) {
        this.inverseBindMatrices = inverseBindMatrices;
        this.skeletonIndex = skeletonIndex;
        this.jointIndices = jointIndices;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFAccessor getInverseBindMatrices() {
        return inverseBindMatrices;
    }

    public GLTFNode getSkeleton() {
        return skeleton;
    }

    public GLTFNode[] getJoints() {
        return joints;
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

    public void postFill_Nodes(GLTFNode[] nodes){
        if (this.skeletonIndex != -1){
            this.skeleton = nodes[this.skeletonIndex];
        }

        if (this.jointIndices != null){
            this.joints = new GLTFNode[this.jointIndices.length];
            for (int i = 0;i < this.joints.length; i++){
                this.joints[i] = nodes[i];
            }
        }

    }

    public static GLTFSkin fromJSONObject(JSONObject obj, GLTFAccessor[] accessors) throws GLTFParseException, ExecutionControl.NotImplementedException {
        GLTFAccessor inverseBindMatrices = null;
        int skeletonIndex = -1;
        int[] jointIndices;
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("inverseBindMatrices")){
            inverseBindMatrices = accessors[obj.getInt("inverseBindMatrices")];
        }
        if (obj.has("skeleton")){
            skeletonIndex = obj.getInt("skeleton");
        }
        if (obj.has("joints")){
            JSONArray array = obj.getJSONArray("joints");
            jointIndices = new int[array.length()];

            for (int i = 0;i < array.length(); i++){
                jointIndices[i] = array.getBigInteger(i).intValue();
            }
        }else{
            throw new GLTFParseException("Field 'joints' in 'Skin' has to be set but isn't.");
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

        return new GLTFSkin(
                inverseBindMatrices,
                skeletonIndex,
                jointIndices,
                name,
                extensions,
                extras
        );
    }
}
