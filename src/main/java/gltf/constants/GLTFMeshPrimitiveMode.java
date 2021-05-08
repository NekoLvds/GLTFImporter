package gltf.constants;

public enum GLTFMeshPrimitiveMode {
    POINTS,
    LINES,
    LINE_LOOP,
    LINE_STRIP,
    TRIANGLES,
    TRIANGLE_STRIP,
    TRIANGLE_FAN;

    public static GLTFMeshPrimitiveMode fromID(int id){
        switch (id){
            case 0: return GLTFMeshPrimitiveMode.POINTS;
            case 1: return GLTFMeshPrimitiveMode.LINES;
            case 2: return GLTFMeshPrimitiveMode.LINE_LOOP;
            case 3: return GLTFMeshPrimitiveMode.LINE_STRIP;
            case 4: return GLTFMeshPrimitiveMode.TRIANGLES;
            case 5: return GLTFMeshPrimitiveMode.TRIANGLE_STRIP;
            case 6: return GLTFMeshPrimitiveMode.TRIANGLE_FAN;
            default:return null;
        }
    }
}
