package gltf.accessor;

import gltf.GLTFDataTypeException;
import gltf.GLTFParseException;
import gltf.buffer.GLTFBufferView;
import gltf.constants.GLTFComponentType;
import gltf.constants.GLTFType;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.sampled.LineEvent;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
 */
public class GLTFAccessor {

    private final GLTFBufferView bufferView;
    private final int byteOffset;
    private final GLTFComponentType componentType;
    private final boolean normalized;
    private final int count;
    private final GLTFType type;
    private final JSONObject sparse;
    private final float[] min;
    private final float[] max;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;
    private final byte[] data;

    protected GLTFAccessor(GLTFBufferView bufferView, int byteOffset, GLTFComponentType componentType, boolean normalized, int count, GLTFType type, JSONObject sparse, float[] min, float[] max, String name, JSONObject extensions, JSONObject extras) {
        this.bufferView = bufferView;
        this.byteOffset = byteOffset;
        this.componentType = componentType;
        this.normalized = normalized;
        this.count = count;
        this.type = type;
        this.sparse = sparse;
        this.min = min;
        this.max = max;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;

        int bytesPerType = this.type.size() * this.componentType.size();
        int bytesTotal = bytesPerType * this.count;
        this.data = Arrays.copyOfRange(this.getBufferView().getData(), this.getByteOffset(), this.getByteOffset() + bytesTotal);
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

    public float[] readDataAsFloats() throws GLTFParseException {
        return convert(readData());
    }

    private float[] convert(Number[] numbers){
        float[] retArray = new float[numbers.length];

        for (int i = 0;i < retArray.length; i++){
            retArray[i] = numbers[i].floatValue();
        }

        return retArray;
    }

    private Number[] readData() throws GLTFParseException {
        int bytesPertype = this.type.size() * this.componentType.size();
        int bytesTotal = bytesPertype * this.count;
        //System.out.println("TotalBytes: " + bytesTotal);
        //System.out.println("Type size: " + this.type.size());
        //System.out.println("Comp Size: " + this.componentType.size());

        Number[][] dataRaw = new Number[this.count][];

        for (int typeIndex = 0;typeIndex < this.count; typeIndex++){
            dataRaw[typeIndex] = readType(typeIndex);
        }

        Number[] simplified =  simplify(dataRaw);
        return simplified;
    }

    private Number[] simplify (Number[][] complicatedArray){
        List<Number> list = new LinkedList<>();

        for (int x = 0; x < complicatedArray.length; x++){
            for (int y = 0; y < complicatedArray[x].length; y++){
                list.add(complicatedArray[x][y]);
            }
        }

        return list.toArray(new Number[list.size()]);
    }

    private Number[] readType(int typeOffset) throws GLTFParseException {
        //System.out.println("readData: " + typeOffset);

        int bytesPerType = this.type.size() * this.componentType.size();
        int byteOffset = bytesPerType * typeOffset; //The type starts here

        Number[] typeData = new Number[this.type.size()];

        for(int index = 0; index < typeData.length; index++){
            //System.out.println("readType: " + (byteOffset + (this.componentType.size() * index)));
            typeData[index] = readComponentType(byteOffset + (this.componentType.size() * index));
        }

        return typeData;
    }

    //Read float, int ...
    private Number readComponentType(int byteOffset) throws GLTFParseException {
        //System.out.println("ReadComponentType: " + byteOffset + " : " + this.data.length);
        byte[] componentTypeBytes = Arrays.copyOfRange(this.data, byteOffset, byteOffset + this.componentType.size()); //read data e.g. for a float

        ByteBuffer buffer = ByteBuffer.wrap(componentTypeBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);


        try{
            switch (this.componentType) {
                case UNSIGNED_BYTE:
                    return Byte.toUnsignedInt(buffer.get());
                case BYTE:
                    return buffer.get();
                case SHORT:
                    return buffer.getShort();
                case UNSIGNED_SHORT:
                    return Short.toUnsignedInt(buffer.getShort());
                case UNSIGNED_INT:
                    return Integer.toUnsignedLong(buffer.getInt());
                case FLOAT:
                    return buffer.getFloat();
                default:
                    throw new GLTFParseException("The type: " + this.componentType + " is not supported.");
            }
        } catch (BufferUnderflowException e) {
            throw new GLTFDataTypeException("Something went wrong when reading the data type." , e);
        }
    }

    /**
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
        float[] min = null;
        float[] max = null;

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
        if (obj.has("min")){
            JSONArray array = obj.getJSONArray("min");
            min = new float[array.length()];

            for (int i = 0;i < array.length(); i++){
                min[i] = array.getBigDecimal(i).floatValue();
            }
        }
        if (obj.has("max")){
            JSONArray array = obj.getJSONArray("max");
            max = new float[array.length()];

            for (int i = 0;i < array.length(); i++){
                max[i] = array.getBigDecimal(i).floatValue();
            }
        }

        return new GLTFAccessor(
                bufferView,
                byteOffset,
                componentType,
                normalized,
                count,
                type,
                sparse,
                min,
                max,
                name,
                extensions,
                extras
        );
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
