package gltf.buffer;

import gltf.GLTFParseException;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The GLTFBuffer represents binary data. The implementation reads the binary data from the given asset file when
 * creating a GLTFBuffer using {@link #fromJSONObject(JSONObject, File)}
 */
public class GLTFBuffer {

    public static final int GLBmagic = 0x46546C67;

    private final byte[] data;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    protected GLTFBuffer(byte[] data, String name, JSONObject extensions, JSONObject extras) {
        this.data = data;
        this.name = name;
        this.extensions = extensions;
        this.extras = extras;
    }

    public byte[] getData() {
        return data;
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

    //TODO may convert little endian to big endian

    /**
     * Constructs a new Buffer from a {@link JSONObject}.
     * <p></p>
     * The binary data is automatically read from the disk file represented by File parameter.
     * @param obj The {@link JSONObject}
     * @param gltfFile The File representing the binary data on the disk
     * @return A constructed GLTFBuffer with it's data read.
     * @throws ExecutionControl.NotImplementedException If a  data URI is used to embed BASE64 Data. Not yet supported.
     * @throws IOException If something went wrong reading the file. See exception message for more details.
     * @throws GLTFParseException If something is wrong for example with the files format. See exception message for more details.
     */
    public static GLTFBuffer fromJSONObject(JSONObject obj, File gltfFile) throws ExecutionControl.NotImplementedException, IOException, GLTFParseException {
        String name = "";
        byte[] data = null;
        JSONObject extensions = null;
        JSONObject extras = null;


        if (obj.has("name")){
            name = obj.getString("name");
        }
        if (obj.has("uri")){
            //External data source
            String relUri = obj.getString("uri");
            String baseURI = gltfFile.getParent();

            if(relUri.startsWith("data")){
                //data URI
                throw new ExecutionControl.NotImplementedException("Base64 Data URIS are not yet implemented");
            }else{
                FileInputStream inputStream = new FileInputStream(new File(baseURI + "\\" + relUri));
                data = inputStream.readAllBytes();
            }
        }else{
            DataInputStream inputStream = new DataInputStream(new FileInputStream(gltfFile));

            long magic = readUnsignedInt(inputStream);
            if (magic != GLBmagic){
                throw new GLTFParseException("magic( " + magic + " ) isn't " + GLBmagic + ". File seems corrupted");
            }

            readUnsignedInt(inputStream); // read the version
            readUnsignedInt(inputStream); // read file length

            long jsonChunkLength = readUnsignedInt(inputStream);
            readUnsignedInt(inputStream); // read chunk type
            inputStream.skipBytes((int) jsonChunkLength); // skip over the json

            long binChunkLength = readUnsignedInt(inputStream);
            readUnsignedInt(inputStream); //chunk type
            data = new byte[(int) binChunkLength];
            inputStream.read(data); //done
        }
        if (obj.has("extensions")){
            extensions = obj.getJSONObject("extensions");
        }
        if (obj.has("extras")){
            extras = obj.getJSONObject("extras");
        }

        return new GLTFBuffer(data, name, extensions, extras);
    }

    /**
     * Assistance methode for reading an unsigned 32bit little endian int.
     * @param stream The input stream to read the uint32 from.
     * @return A unsigned inr 32 (stored as long)
     * @throws IOException See exception message for more details.
     */
    public static long readUnsignedInt(InputStream stream) throws IOException {
        byte[] bytes = new byte[4];
        stream.read(bytes);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }
}
