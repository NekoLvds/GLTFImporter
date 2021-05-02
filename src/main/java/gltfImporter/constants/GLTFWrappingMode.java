package gltfImporter.constants;

public enum GLTFWrappingMode {
    CLAMP_TO_EDGE,
    MIRRORED_REPEAT,
    REPEAT;

    public static GLTFWrappingMode fromID(int id){
        switch (id){
            case 33071:
                return GLTFWrappingMode.CLAMP_TO_EDGE;
            case 33648:
                return GLTFWrappingMode.MIRRORED_REPEAT;
            case 10497:
                return GLTFWrappingMode.REPEAT;
            default: return null;
        }
    }
}
