package gltfImporter.material;

import gltfImporter.GLTFParseException;
import gltfImporter.buffer.GLTFBufferView;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class GLTFImage {

    private final String name;
    private final JSONObject extension;
    private final JSONObject extras;

    private final BufferedImage bufferedImage; //TODO maybe rework

    public GLTFImage(BufferedImage image, String name, JSONObject extension, JSONObject extras) {
        this.bufferedImage = image;
        this.name = name;
        this.extension = extension;
        this.extras = extras;
    }

    public static GLTFImage fromJSONObject(JSONObject obj, GLTFBufferView[] bufferViews, File parentDirectory) throws GLTFParseException, IOException {
        String uri = "";
        String mimeType = "";
        GLTFBufferView bufferView = null;
        String name = "";
        JSONObject extensions = null;
        JSONObject extras = null;

        if (obj.has("uri")){
            uri = obj.getString("uri");
        }
        if (obj.has("mimeType")){
            mimeType = obj.getString("mimeType");
        }
        if (obj.has("bufferView")){
            bufferView = bufferViews[obj.getInt("bufferView")];
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

        //Check if either uri OR buffer view is available
        if (bufferView == null && (uri.equals("") || uri == null)){
            throw new GLTFParseException("Either Image uri or bufferView for image data has to be available. Both aren't");
        }

        BufferedImage image = null;
        if (bufferView != null){
            byte[] imageData = bufferView.getData();
            image = ImageIO.read(new ByteArrayInputStream(imageData));
        } else{
            String stringUri = parentDirectory + "\\" + uri;
            image = ImageIO.read(new File(stringUri));
        }

        if (image == null){
            throw new GLTFParseException("Failed to create image data: " + uri + " | " + (parentDirectory + "\\" + uri));
        }

        return new GLTFImage(
                image,
                name,
                extensions,
                extras
        );
    }

    @Override
    public String toString() {
        return "GLTFImage{" +
                "name='" + name + '\'' +
                ", extension=" + extension +
                ", extras=" + extras +
                ", bufferedImage=" + bufferedImage +
                '}';
    }
}
