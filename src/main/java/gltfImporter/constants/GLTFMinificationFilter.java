package gltfImporter.constants;

public enum GLTFMinificationFilter {
    NEAREST,
    LINEAR,
    NEAREST_MIPMAP_NEAREST,
    LINEAR_MIPMAP_NEAREST,
    NEAREST_MIPMAP_LINEAR,
    LINEAR_MIPMAP_LINEAR;

    public static GLTFMinificationFilter fromID(int id){
        switch (id){
            case 9728:
                return GLTFMinificationFilter.NEAREST;
            case 9729:
                return GLTFMinificationFilter.LINEAR;
            case 9984:
                return GLTFMinificationFilter.NEAREST_MIPMAP_NEAREST;
            case 9985:
                return GLTFMinificationFilter.LINEAR_MIPMAP_NEAREST;
            case 9986:
                return GLTFMinificationFilter.NEAREST_MIPMAP_LINEAR;
            case 9987:
                return GLTFMinificationFilter.LINEAR_MIPMAP_LINEAR;
            default: return null;
        }
    }
}
