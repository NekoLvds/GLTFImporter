package gltfImporter.accessor;

import gltfImporter.buffer.GLTFBufferView;
import gltfImporter.constants.GLTFComponentType;
import gltfImporter.constants.GLTFType;
import org.json.JSONObject;

import java.util.Arrays;

public class GLTFShortAccessor extends GLTFAccessor{

    private final short[] min;
    private final short[] max;
    private final int[] data;

    public GLTFShortAccessor(GLTFBufferView bufferView, int byteOffset, GLTFComponentType componentType, boolean normalized, int count, GLTFType type, JSONObject sparse, String name, JSONObject extensions, JSONObject extras, short[] min, short[] max) {
        super(bufferView, byteOffset, componentType, normalized, count, type, sparse, name, extensions, extras);
        this.min = min;
        this.max = max;

        this.data = this.getBufferView().getUnsignedShorts(this.getByteOffset(), this.getComponentType().size() * this.getCount());
    }

    @Override
    public String toString() {
        return "GLTFShortAccessor{" +
                "min=" + Arrays.toString(min) +
                ", max=" + Arrays.toString(max) +
                ", data=" + data.length +
                "} " + super.toString();
    }
}
