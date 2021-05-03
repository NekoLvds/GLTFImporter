package gltf.accessor;

import gltf.GLTFParseException;
import gltf.buffer.GLTFBufferView;
import gltf.constants.GLTFComponentType;
import gltf.constants.GLTFType;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The GLTFAccessor accesses data through a {@link GLTFBufferView} in a specific way. For example reading the data as three dimensional vectors
 * to create the vertices or as 4x4 matrices to store a objects rotation, scale and translation.
 *
 * <p></p>
 *
 * This class abstract and only represents an accessor. The Implementations implement a data type to be read for example as float.
 *
 * <p></p>
 *
 * The {@link #fromJSONObject(JSONObject, GLTFBufferView[])} methode will create the correct type of accessor for you.
 *
 * @see GLTFFloatAccessor
 * @see GLTFShortAccessor
 */
public abstract class GLTFAccessor {

    private final GLTFBufferView bufferView;
    private final int byteOffset;
    private final GLTFComponentType componentType;
    private final boolean normalized;
    private final int count;
    private final GLTFType type;
    private final JSONObject sparse;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    protected GLTFAccessor(GLTFBufferView bufferView, int byteOffset, GLTFComponentType componentType, boolean normalized, int count, GLTFType type, JSONObject sparse, String name, JSONObject extensions, JSONObject extras) {
        this.bufferView = bufferView;
        this.byteOffset = byteOffset;
        this.componentType = componentType;
        this.normalized = normalized;
        this.count = count;
        this.type = type;
        this.sparse = sparse;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFBufferView getBufferView() {
        return bufferView;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public GLTFComponentType getComponentType() {
        return componentType;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public int getCount() {
        return count;
    }

    public GLTFType getType() {
        return type;
    }

    public JSONObject getSparse() {
        return sparse;
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

    /**
     * Constructs a new GLTFAccessor using the given {@link JSONObject}. This methode will create the correct type
     * of accessor like {@link GLTFFloatAccessor} or {@link GLTFShortAccessor} for you.
     * @param obj The JSONObject of the Accessor.
     * @param bufferViews The filled array of buffer views of the {@link gltf.GLTFAsset} used to read the binary data from a {@link gltf.buffer.GLTFBuffer};
     * @return The constructed Accessor ready to use.
     * @throws GLTFParseException If the JSON isn't properly filled. See exception message for details.
     * @throws ExecutionControl.NotImplementedException Sparse encoding is not yet supported.
     */
    public static GLTFAccessor fromJSONObject(JSONObject obj, GLTFBufferView[] bufferViews) throws GLTFParseException, ExecutionControl.NotImplementedException {
        GLTFBufferView bufferView = null;
        int byteOffset = 0;
        GLTFComponentType componentType = null;
        boolean normalized = false;
        int count = -1;
        GLTFType type = null;
        JSONObject sparse = null;
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("bufferView")){
            bufferView = bufferViews[obj.getInt("bufferView")];
        }else{
            //required field
            throw new GLTFParseException("Field 'bufferView' in 'Accessor' is required but not set");
        }
        if (obj.has("byteOffset")){
            byteOffset = obj.getInt("byteOffset");
        }
        if (obj.has("componentType")){
            componentType = GLTFComponentType.fromID(obj.getInt("componentType"));
        }else{
            throw new GLTFParseException("Field 'componentType' in 'Accessor' is required but not set");
        }
        if (obj.has("normalized")){
            normalized = obj.getBoolean("normalized");
        }
        if (obj.has("count")){
            count = obj.getInt("count");
        }else{
            throw new GLTFParseException("Field 'count' in 'Accessor' is required but not set");
        }
        if (obj.has("type")){
            type = GLTFType.valueOf(obj.getString("type"));
        }else{
            throw new GLTFParseException("Field 'type' in 'Accessor' is required but not set");
        }
        if (obj.has("sparse")){
            sparse = obj.getJSONObject("sparse");
            throw new ExecutionControl.NotImplementedException("Sparse Encoding in Accessor not yet supported");
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

        switch (componentType){
            case FLOAT:
                float[] min_floats = null;
                float[] max_floats = null;

                if (obj.has("min")){
                    JSONArray jsonMinArray = obj.getJSONArray("min");

                    min_floats = new float[jsonMinArray.length()];
                    for(int i = 0;i < jsonMinArray.length(); i++){
                        min_floats[i] = jsonMinArray.getBigDecimal(i).floatValue();
                    }
                }
                if (obj.has("max")){
                    JSONArray jsonMaxArray = obj.getJSONArray("max");

                    max_floats = new float[jsonMaxArray.length()];
                    for(int i = 0;i < jsonMaxArray.length(); i++){
                        max_floats[i] = jsonMaxArray.getBigDecimal(i).floatValue();
                    }
                }
                return new GLTFFloatAccessor(
                        bufferView,
                        byteOffset,
                        componentType,
                        normalized,
                        count,
                        type,
                        sparse,
                        name,
                        extensions,
                        extras,
                        min_floats,
                        max_floats
                );

            case UNSIGNED_SHORT:
                short[] min_shorts = null;
                short[] max_shorts = null;

                if (obj.has("min")){
                    JSONArray jsonMinArray = obj.getJSONArray("min");

                    min_shorts = new short[jsonMinArray.length()];
                    for(int i = 0;i < jsonMinArray.length(); i++){
                        min_shorts[i] = jsonMinArray.getBigInteger(i).shortValue();
                    }
                }
                if (obj.has("max")){
                    JSONArray jsonMaxArray = obj.getJSONArray("max");

                    max_shorts = new short[jsonMaxArray.length()];
                    for(int i = 0;i < jsonMaxArray.length(); i++){
                        max_shorts[i] = jsonMaxArray.getBigInteger(i).shortValue();
                    }
                }

                return new GLTFShortAccessor(
                        bufferView,
                        byteOffset,
                        componentType,
                        normalized,
                        count,
                        type,
                        sparse,
                        name,
                        extensions,
                        extras,
                        min_shorts,
                        max_shorts
                );
            default: throw new GLTFParseException("Cannot parse type " + componentType.name() + ". Component type(s): BYTE, UNSIGNED_BYTE, SHORT, UNSIGNED_SHORT, UNSIGNED_INT are not yet supported.");
        }
    }

    @Override
    public String toString() {
        return "GLTFAccessor{" +
                "bufferView=" + bufferView +
                ", byteOffset=" + byteOffset +
                ", componentType=" + componentType +
                ", normalized=" + normalized +
                ", count=" + count +
                ", type=" + type +
                ", sparse=" + sparse +
                ", name='" + name + '\'' +
                ", extensions=" + extensions +
                ", extras=" + extras +
                '}';
    }
}