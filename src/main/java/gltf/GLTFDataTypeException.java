package gltf;

public class GLTFDataTypeException extends GLTFParseException{

    public GLTFDataTypeException() {
    }

    public GLTFDataTypeException(String message) {
        super(message);
    }

    public GLTFDataTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GLTFDataTypeException(Throwable cause) {
        super(cause);
    }

    public GLTFDataTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
