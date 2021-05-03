package gltf;

import gltf.camera.GLTFCamera;
import gltf.mesh.GLTFMesh;
import org.json.JSONArray;
import org.json.JSONObject;

public class GLTFNode {

    private final GLTFCamera camera;
    private GLTFNode[] children;
    private final int[] childIndices;
    private final GLTFSkin skin;
    private final float[] matrix;
    private final GLTFMesh mesh;
    private final float[] rotation;
    private final float[] scale;
    private final float[] translation;
    private final float[] weights;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFNode(GLTFCamera camera, int[] childIndices, GLTFSkin skin, float[] matrix, GLTFMesh mesh, float[] rotation, float[] scale, float[] translation, float[] weights, String name, JSONObject extensions, JSONObject extras) {
        this.camera = camera;
        this.childIndices = childIndices;
        this.skin = skin;
        this.matrix = matrix;
        this.mesh = mesh;
        this.rotation = rotation;
        this.scale = scale;
        this.translation = translation;
        this.weights = weights;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }

    public void postFill_nodes(GLTFNode[] nodes){
        if (this.childIndices != null){
            this.children = new GLTFNode[this.childIndices.length];
            for (int i = 0;i < this.children.length; i++){
                this.children[i] = nodes[this.childIndices[i]];
            }
        }

    }

    public static GLTFNode fromJSONObject(JSONObject obj, GLTFCamera[] cameras, GLTFMesh[] meshes, GLTFSkin[] skins){
        GLTFCamera camera = null;
        int[] children = null;
        GLTFSkin skin = null;
        float[] matrix = new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        GLTFMesh mesh = null;
        float[] rotation = new float[]{0,0,0,1};
        float[] scale = new float[]{1,1,1};
        float[] translation = new float[]{0,0,0};
        float[] weights = null;
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("camera")){
            camera = cameras[obj.getInt("camera")];
        }
        if (obj.has("children")){
            JSONArray array = obj.getJSONArray("children");
            children = new int[array.length()];

            for (int i = 0; i < array.length(); i++){
                children[i] = array.getBigInteger(i).intValue();
            }
        }
        if (obj.has("skin")){
            skin = skins[obj.getInt("skin")];
        }
        if (obj.has("matrix")){
            JSONArray array = obj.getJSONArray("matrix");
            matrix = new float[array.length()];

            for (int i = 0; i < array.length(); i++){
                matrix[i] = array.getBigDecimal(i).floatValue();
            }
        }
        if (obj.has("mesh")){
            mesh = meshes[obj.getInt("mesh")];
        }
        if (obj.has("rotation")){
            JSONArray array = obj.getJSONArray("rotation");
            rotation = new float[array.length()];

            for (int i = 0;i < array.length(); i++){
                rotation[i] = array.getBigDecimal(i).floatValue();
            }
        }
        if (obj.has("scale")){
            JSONArray array = obj.getJSONArray("scale");
            scale = new float[array.length()];

            for (int i = 0;i < array.length(); i++){
                scale[i] = array.getBigDecimal(i).floatValue();
            }
        }
        if (obj.has("translation")){
            JSONArray array = obj.getJSONArray("translation");
            translation = new float[array.length()];

            for (int i = 0; i < array.length(); i++){
                translation[i] = array.getBigDecimal(i).floatValue();
            }
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

        return new GLTFNode(
                camera,
                children,
                skin,
                matrix,
                mesh,
                rotation,
                scale,
                translation,
                weights,
                name,
                extensions,
                extras
        );
    }
}
