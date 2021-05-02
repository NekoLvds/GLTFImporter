package gltfImporter.buffer;

import gltfImporter.GLTFParseException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class GLTFBufferView {

    private final GLTFBuffer buffer;
    private final int byteOffset;
    private final int byteLength;
    private final int byteStride;
    private final int target;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    private final byte[] data;

    public GLTFBufferView(GLTFBuffer buffer, int byteOffset, int byteLength, int byteStride, int target, String name, JSONObject extensions, JSONObject extras) {
        this.buffer = buffer;
        this.byteOffset = byteOffset;
        this.byteLength = byteLength;
        this.byteStride = byteStride;
        this.target = target;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;

        data = Arrays.copyOfRange(this.buffer.getData(), this.byteOffset, this.byteOffset + this.byteLength);
    }

    public byte[] getData() {
        return data;
    }

    public GLTFBuffer getBuffer() {
        return buffer;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public int getByteLength() {
        return byteLength;
    }

    public int getByteStride() {
        return byteStride;
    }

    public int getTarget() {
        return target;
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

    //TODO potential error source
    public float[] getFloats(int byteOffset, int byteCount){
        byte[] floatData = Arrays.copyOfRange(this.data, byteOffset, byteOffset + byteCount);
        ByteBuffer buffer = ByteBuffer.wrap(floatData);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        assert floatData.length % 4 == 0; //must be for floats

        float[] result = new float[(int)(floatData.length / 4)];
        for(int i = 0;i < result.length; i++){
            result[i] = buffer.getFloat(i * 4);
        }

        return result;
    }

    //TODO potential error source
    public int[] getUnsignedShorts(int byteOffset, int byteCount){
        byte[] shortData = Arrays.copyOfRange(this.data, byteOffset, byteOffset + byteCount);
        ByteBuffer buffer = ByteBuffer.wrap(shortData);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        assert shortData.length % 2 == 0; //must be for shorts

        int[] result = new int[(int)(shortData.length / 2)];
        for(int i = 0;i < result.length; i++){
            result[i] = buffer.getShort(i);
        }
        return result;
    }

    public static GLTFBufferView fromJSONObject(JSONObject obj, GLTFBuffer[] buffers) throws GLTFParseException {
        GLTFBuffer buffer = null;
        int byteOffset = -1;
        int byteLength = -1;
        int byteStride = -1;
        int target = -1;
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if(obj.has("buffer")){
            buffer = buffers[obj.getInt("buffer")];
        }else{
            //RequiredField
            throw new GLTFParseException("Field 'buffer' in 'BufferView' is required but not set");
        }
        if (obj.has("byteOffset")){
            byteOffset = obj.getInt("byteOffset");
        }
        if (obj.has("byteLength")){
            byteLength = obj.getInt("byteLength");
        }else{
            //RequiredField
            throw new GLTFParseException("Field 'byteLength' in 'BufferView' is required but not set");
        }
        if (obj.has("byteStride")){
            byteStride = obj.getInt(("byteStride"));
        }
        if (obj.has("target")){
            target = obj.getInt("target");
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

        return new GLTFBufferView(
                buffer,
                byteOffset,
                byteLength,
                byteStride,
                target,
                name,
                extensions,
                extras
        );
    }

    @Override
    public String toString() {
        return "GLTFBufferView{" +
                "buffer=" + buffer +
                ", byteOffset=" + byteOffset +
                ", byteLength=" + byteLength +
                ", byteStride=" + byteStride +
                ", target=" + target +
                ", name='" + name + '\'' +
                ", extensions=" + extensions +
                ", extras=" + extras +
                ", data=" + data.length +
                '}';
    }
}