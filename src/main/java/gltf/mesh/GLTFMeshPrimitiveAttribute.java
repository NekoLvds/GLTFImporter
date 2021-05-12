package gltf.mesh;

import gltf.accessor.GLTFAccessor;
import org.json.JSONObject;

public class GLTFMeshPrimitiveAttribute {

    public static final String POSITION_KEY = "POSITION";
    public static final String NORMAL_KEY = "NORMAL";
    public static final String TANGENT_KEY = "TANGENT";
    public static final String TEXCOORD_0_KEY = "TEXCOORD_0";
    public static final String TEXTCOORD_1_KEY = "TEXCOORD_1";
    public static final String COLOR_0_KEY = "COLOR_0";
    public static final String JOINTS_0_KEY = "JOINTS_0";
    public static final String WEIGHTS_0_KEY = "WEIGHTS_0";

    private final GLTFAccessor position;
    private final GLTFAccessor normal;
    private final GLTFAccessor tangent;
    private final GLTFAccessor texCoord_0;
    private final GLTFAccessor texcoord_1;
    private final GLTFAccessor color_0;
    private final GLTFAccessor joints_0;
    private final GLTFAccessor weights_0;

    public GLTFMeshPrimitiveAttribute(GLTFAccessor position, GLTFAccessor normal, GLTFAccessor tangent, GLTFAccessor texCoord_0, GLTFAccessor texcoord_1, GLTFAccessor color_0, GLTFAccessor joints_0, GLTFAccessor weights_0) {
        this.position = position;
        this.normal = normal;
        this.tangent = tangent;
        this.texCoord_0 = texCoord_0;
        this.texcoord_1 = texcoord_1;
        this.color_0 = color_0;
        this.joints_0 = joints_0;
        this.weights_0 = weights_0;
    }

    public boolean isKey(String key){
        switch (key) {
            case POSITION_KEY:
                return this.position != null;
            case NORMAL_KEY:
                return this.normal != null;
            case TANGENT_KEY:
                return this.tangent != null;
            case TEXCOORD_0_KEY:
                return this.texCoord_0 != null;
            case TEXTCOORD_1_KEY:
                return this.texcoord_1 != null;
            case COLOR_0_KEY:
                return this.color_0 != null;
            case JOINTS_0_KEY:
                return this.joints_0 != null;
            case WEIGHTS_0_KEY:
                return this.weights_0 != null;
            default:
                return false;
        }
    }

    public GLTFAccessor getPosition() {
        return position;
    }

    public GLTFAccessor getNormal() {
        return normal;
    }

    public GLTFAccessor getTangent() {
        return tangent;
    }

    public GLTFAccessor getTexCoord_0() {
        return texCoord_0;
    }

    public GLTFAccessor getTexcoord_1() {
        return texcoord_1;
    }

    public GLTFAccessor getColor_0() {
        return color_0;
    }

    public GLTFAccessor getJoints_0() {
        return joints_0;
    }

    public GLTFAccessor getWeights_0() {
        return weights_0;
    }

    public static GLTFMeshPrimitiveAttribute fromJSONObject(JSONObject obj, GLTFAccessor[] accessors){
        GLTFAccessor position = null;
        GLTFAccessor normal = null;
        GLTFAccessor tangent = null;
        GLTFAccessor texcoord_0 = null;
        GLTFAccessor texcoord_1 = null;
        GLTFAccessor color_0 = null;
        GLTFAccessor joints_0 = null;
        GLTFAccessor weights_0 = null;

        if (obj.has("POSITION")){
            position = accessors[obj.getInt("POSITION")];
        }
        if (obj.has("NORMAL")){
            normal = accessors[obj.getInt("NORMAL")];
        }
        if (obj.has("TANGENT")){
            tangent = accessors[obj.getInt("TANGENT")];
        }
        if (obj.has("TEXCOORD_0")){
            texcoord_0 = accessors[obj.getInt("TEXCOORD_0")];
        }
        if (obj.has("TEXCOORD_1")){
            texcoord_1 = accessors[obj.getInt("TEXCOORD_1")];
        }
        if (obj.has("COLOR_0")){
            color_0 = accessors[obj.getInt("COLOR_0")];
        }
        if (obj.has("JOINTS_0")){
            joints_0 = accessors[obj.getInt("JOINTS_0")];
        }
        if (obj.has("WEIGHTS_0")){
            weights_0 = accessors[obj.getInt("WEIGHTS_0")];
        }

        return new GLTFMeshPrimitiveAttribute(
                position,
                normal,
                tangent,
                texcoord_0,
                texcoord_1,
                color_0,
                joints_0,
                weights_0
        );
    }

    @Override
    public String toString() {
        return "GLTFMeshPrimitiveAttribute{" +
                "position=" + position +
                ", normal=" + normal +
                ", tangent=" + tangent +
                ", texCoord_0=" + texCoord_0 +
                ", texcoord_1=" + texcoord_1 +
                ", color_0=" + color_0 +
                ", joints_0=" + joints_0 +
                ", weights_0=" + weights_0 +
                '}';
    }
}
