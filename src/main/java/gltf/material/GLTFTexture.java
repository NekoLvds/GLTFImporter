package gltf.material;

import org.json.JSONObject;

public class GLTFTexture {

    private final GLTFSampler sampler;
    private final GLTFImage image;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFTexture(GLTFSampler sampler, GLTFImage image, String name, JSONObject extensions, JSONObject extras) {
        this.sampler = sampler;
        this.image = image;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }

    public GLTFSampler getSampler() {
        return sampler;
    }

    public GLTFImage getImage() {
        return image;
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

    public static GLTFTexture fromJSONObject(JSONObject obj, GLTFImage[] images, GLTFSampler[] samplers){
        GLTFImage image = null;
        GLTFSampler sampler = null;
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("source")){
            image = images[obj.getInt("source")];
        }
        if (obj.has("sampler")){
            sampler = samplers[obj.getInt("sampler")];
        }
        if (obj.has("name")){
            name = obj.getString("name");
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFTexture(
                sampler,
                image,
                name,
                extensions,
                extras
        );
    }

    @Override
    public String toString() {
        return "GLTFTexture{" +
                "sampler=" + sampler +
                ", image=" + image +
                ", name='" + name + '\'' +
                ", extensions=" + extensions +
                ", extras=" + extras +
                '}';
    }
}
