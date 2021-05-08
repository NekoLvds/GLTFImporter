package gltf.buffer;

import gltf.GLTFParseException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * The GLTFBufferView represents a buffer view. A buffer view is a subpart of a buffer and mostly represents a logically separate data part.
 * <p></p>
 *
 * Using {@link #fromJSONObject(JSONObject, GLTFBuffer[])} to initialize the buffer view the data represented by this buffer view will be stored in memory.
 */
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

    protected GLTFBufferView(GLTFBuffer buffer, int byteOffset, int byteLength, int byteStride, int target, String name, JSONObject extensions, JSONObject extras) {
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

    /**
     * Initialises a BufferView from a given {@link JSONObject} and the initialized list of {@link GLTFBuffer}.
     * @param obj The JSON representing this BufferView in the assset.
     * @param buffers The initialized list of Buffers.
     * @return An initialized GLTFBufferView.
     * @throws GLTFParseException If required fields aren't set. See exception message for further details.
     */
    public static GLTFBufferView fromJSONObject(JSONObject obj, GLTFBuffer[] buffers) throws GLTFParseException {
        GLTFBuffer buffer = null;
        int byteOffset = 0;
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
