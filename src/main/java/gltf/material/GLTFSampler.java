package gltf.material;

import gltf.constants.GLTFMagnificationFilter;
import gltf.constants.GLTFMinificationFilter;
import gltf.constants.GLTFWrappingMode;
import org.json.JSONObject;

public class GLTFSampler {

    private final GLTFMagnificationFilter magFilter;
    private final GLTFMinificationFilter minFilter;
    private final GLTFWrappingMode wrapS;
    private final GLTFWrappingMode wrapT;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFSampler(GLTFMagnificationFilter magFilter, GLTFMinificationFilter minFilter, GLTFWrappingMode wrapS, GLTFWrappingMode wrapT, String name, JSONObject extensions, JSONObject extras) {
        this.magFilter = magFilter;
        this.minFilter = minFilter;
        this.wrapS = wrapS;
        this.wrapT = wrapT;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFMagnificationFilter getMagFilter() {
        return magFilter;
    }

    public GLTFMinificationFilter getMinFilter() {
        return minFilter;
    }

    public GLTFWrappingMode getWrapS() {
        return wrapS;
    }

    public GLTFWrappingMode getWrapT() {
        return wrapT;
    }

    public String getName() {
        return name;
    }

    public JSONObject getExtensions() {
        return extensions;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public static GLTFSampler fromJSONObject(JSONObject obj){
        GLTFMagnificationFilter magFilter = null;
        GLTFMinificationFilter minFilter = null;
        GLTFWrappingMode wrapS = GLTFWrappingMode.fromID(10497);
        GLTFWrappingMode wrapT = GLTFWrappingMode.fromID(10497);
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("magFilter")){
            magFilter = GLTFMagnificationFilter.fromID(obj.getInt("magFilter"));
        }
        if (obj.has("minfilter")){
            minFilter = GLTFMinificationFilter.fromID(obj.getInt("minFilter"));
        }
        if (obj.has("wrapS")){
            wrapS = GLTFWrappingMode.fromID(obj.getInt("wrapS"));
        }
        if (obj.has("wrapT")){
            wrapT = GLTFWrappingMode.fromID(obj.getInt("wrapT"));
        }
        if (obj.has("name")){
            name = name;
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFSampler(
                magFilter,
                minFilter,
                wrapS,
                wrapT,
                name,
                extensions,
                extras
        );
    }
}
