package gltf.constants;

public enum GLTFType {
    SCALAR, VEC2, VEC3, VEC4, MAT2, MAT3, MAT4;

    public int size(){
        switch (this){
            case SCALAR: return 1;
            case VEC2: return 2;
            case VEC3: return 3;
            case VEC4: return 4;
            case MAT2: return 4;
            case MAT3: return 9;
            case MAT4: return 16;
            default: return -1;
        }
    }
}
