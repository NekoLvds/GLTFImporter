package gltf.constants;

import gltf.GLTFParseException;

public enum GLTFComponentType {
    BYTE,
    UNSIGNED_BYTE,
    SHORT,
    UNSIGNED_SHORT,
    UNSIGNED_INT,
    FLOAT;

    public static GLTFComponentType fromID(int id) throws GLTFParseException {
        switch (id){
            case 5120:
                return GLTFComponentType.BYTE;
            case 5121:
                return GLTFComponentType.UNSIGNED_BYTE;
            case 5122:
                return GLTFComponentType.SHORT;
            case 5123:
                return GLTFComponentType.UNSIGNED_SHORT;
            case 5125:
                return GLTFComponentType.UNSIGNED_INT;
            case 5126:
                return GLTFComponentType.FLOAT;
            default: throw new GLTFParseException("Component Type with id: " + id + " is not defined.");
        }
    }

    public int size() {
        switch (this){
            case BYTE: return 1;
            case UNSIGNED_BYTE: return 1;
            case SHORT: return 2;
            case UNSIGNED_SHORT: return 2;
            case UNSIGNED_INT: return 4;
            case FLOAT:return 4;
            default: return -1;
        }
    }
}
