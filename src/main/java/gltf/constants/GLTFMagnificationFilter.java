package gltf.constants;

public enum GLTFMagnificationFilter {
    NEAREST,
    LINEAR;

    public static GLTFMagnificationFilter fromID(int id){
        switch (id){
            case 9728:
                return GLTFMagnificationFilter.NEAREST;
            case 9729:
                return GLTFMagnificationFilter.LINEAR;
            default:
                return null;
        }
    }
}
