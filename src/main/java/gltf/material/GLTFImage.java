package gltf.material;

import gltf.GLTFParseException;
import gltf.buffer.GLTFBufferView;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * The GLTFImage class represents the image of a texture in the gltf and the binary data associated with it.
 *
 * <p></p>
 *
 * If {@link #fromJSONObject(JSONObject, GLTFBufferView[], File)} is used to initialize the image the actual image data
 * is stored as {@link BufferedImage}.
 */
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

    public String getName() {
        return name;
    }

    public JSONObject getExtension() {
        return extension;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    /**
     * Creates a GLTFImage based on the JSON object in the gltf asset and reads the binary data associated with it
     * into a {@link BufferedImage}.
     * @param obj The JSONObject representing this gltf image
     * @param bufferViews The initialized buffer views
     * @param parentDirectory The parent directory for relative uris
     * @return The initialized gltf image.
     * @throws GLTFParseException If the image has no binary data associated with it (no uri AND no buffer view).
     * @throws IOException If image creation fails.
     */
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
