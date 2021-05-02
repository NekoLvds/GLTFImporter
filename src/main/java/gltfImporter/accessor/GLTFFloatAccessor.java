package gltfImporter.accessor;

import gltfImporter.buffer.GLTFBufferView;
import gltfImporter.constants.GLTFComponentType;
import gltfImporter.constants.GLTFType;
import org.json.JSONObject;

import java.util.Arrays;

public class GLTFFloatAccessor extends GLTFAccessor{

    private final float[] min;
    private final float[] max;
    private final float[] data;

    public GLTFFloatAccessor(GLTFBufferView bufferView, int byteOffset, GLTFComponentType componentType, boolean normalized, int count, GLTFType type, JSONObject sparse, String name, JSONObject extensions, JSONObject extras, float[] min, float[] max) {
        super(bufferView, byteOffset, componentType, normalized, count, type, sparse, name, extensions, extras);
        this.min = min;
        this.max = max;

        this.data = this.getBufferView().getFloats(this.getByteOffset(), this.getComponentType().size() * this.getCount()); //TODO potential error
    }

    @Override
    public String toString() {
        return "GLTFFloatAccessor{" +
                "min=" + Arrays.toString(min) +
                ", max=" + Arrays.toString(max) +
                ", data=" + data.length +
                "} " + super.toString();
    }
}
