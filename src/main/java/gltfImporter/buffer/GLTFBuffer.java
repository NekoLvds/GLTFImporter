package gltfImporter.buffer;

import gltfImporter.GLTFParseException;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GLTFBuffer {

    public static final int GLBmagic = 0x46546C67;

    private final byte[] data;
    private final String name;
    private final JSONObject extensions;
    private final JSONObject extras;

    public GLTFBuffer(byte[] data, String name, JSONObject extensions, JSONObject extras) {
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

    public static long readUnsignedInt(InputStream stream) throws IOException {
        byte[] bytes = new byte[4];
        stream.read(bytes);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }
}
