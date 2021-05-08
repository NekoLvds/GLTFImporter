package gltf;

public class GLTFParseException extends Exception {
    public GLTFParseException() {
    }

    public GLTFParseException(String message) {
        super(message);
    }

    public GLTFParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public GLTFParseException(Throwable cause) {
        super(cause);
    }

    public GLTFParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
