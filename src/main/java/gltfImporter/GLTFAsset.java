package gltfImporter;

import gltfImporter.accessor.GLTFAccessor;
import gltfImporter.buffer.GLTFBuffer;
import gltfImporter.buffer.GLTFBufferView;
import gltfImporter.material.GLTFImage;
import gltfImporter.material.GLTFMaterial;
import gltfImporter.material.GLTFSampler;
import gltfImporter.material.GLTFTexture;
import gltfImporter.mesh.GLTFMesh;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GLTFAsset {

    private final File assetFile;
    private final File assetDirectory;

    private final GLTFBuffer[] buffers;
    private final GLTFBufferView[] bufferViews;
    private final GLTFAccessor[] accessors;
    private final GLTFImage[] images;
    private final GLTFSampler[] samplers;
    private final GLTFTexture[] textures;
    private final GLTFMaterial[] materials;
    private final GLTFMesh[] meshes;
    private final GLTFSkin[] skins;
    private final GLTFNode[] nodes;

    public GLTFAsset(File file) throws ExecutionControl.NotImplementedException, IOException, GLTFParseException {
        //Reading/Creating basic info
        this.assetFile = file;
        this.assetDirectory = file.getParentFile();

        //Getting the Json
        JSONObject jsonAsset = getJSON(this.assetFile);

        //Creating the buffers
        JSONArray jsonBuffers = jsonAsset.getJSONArray("buffers");
        this.buffers = new GLTFBuffer[jsonBuffers.length()];
        for (int i = 0;i < jsonBuffers.length(); i++){
            this.buffers[i] = GLTFBuffer.fromJSONObject(jsonBuffers.getJSONObject(i), this.assetFile);
        }

        //Creating the buffer views
        JSONArray jsonBufferViews = jsonAsset.getJSONArray("bufferViews");
        this.bufferViews = new GLTFBufferView[jsonBufferViews.length()];
        for (int i = 0;i < jsonBufferViews.length(); i++){
            this.bufferViews[i] = GLTFBufferView.fromJSONObject(jsonBufferViews.getJSONObject(i), this.buffers);
        }

        //Creating the Accessors
        JSONArray jsonAccessors = jsonAsset.getJSONArray("accessors");
        this.accessors = new GLTFAccessor[jsonAccessors.length()];
        for (int i = 0;i < jsonAccessors.length(); i++){
            this.accessors[i] = GLTFAccessor.fromJSONObject(jsonAccessors.getJSONObject(i), this.bufferViews);
        }

        //Creating the Images
        JSONArray jsonImages = jsonAsset.getJSONArray("images");
        this.images = new GLTFImage[jsonImages.length()];
        for (int i = 0;i < jsonImages.length(); i++){
            this.images[i] = GLTFImage.fromJSONObject(jsonImages.getJSONObject(i), this.bufferViews, this.assetDirectory);
        }

        //Creating the samplers
        JSONArray jsonSamplers = jsonAsset.getJSONArray("samplers");
        this.samplers = new GLTFSampler[jsonSamplers.length()];
        for (int i = 0;i < jsonSamplers.length(); i++){
            this.samplers[i] = GLTFSampler.fromJSONObject(jsonSamplers.getJSONObject(i));
        }

        //Creating the textures
        JSONArray jsonTextures = jsonAsset.getJSONArray("textures");
        this.textures = new GLTFTexture[jsonTextures.length()];
        for (int i = 0; i < jsonTextures.length(); i++){
            this.textures[i] = GLTFTexture.fromJSONObject(jsonTextures.getJSONObject(i), this.images, this.samplers);
        }

        //Creating the materials
        JSONArray jsonMaterials = jsonAsset.getJSONArray("materials");
        this.materials = new GLTFMaterial[jsonMaterials.length()];
        for (int i = 0;i < jsonMaterials.length(); i++){
            this.materials[i] = GLTFMaterial.fromJSONObject(jsonMaterials.getJSONObject(i), this.textures);
        }

        //Creating the meshes
        JSONArray jsonMeshes = jsonAsset.getJSONArray("meshes");
        this.meshes = new GLTFMesh[jsonMeshes.length()];
        for (int i = 0;i < jsonMeshes.length(); i++){
            this.meshes[i] = GLTFMesh.fromJSONObject(jsonMeshes.getJSONObject(i), this.accessors, this.bufferViews, this.materials);
        }

        //Creating the skins
        JSONArray jsonSkins = jsonAsset.getJSONArray("skins");
        this.skins = new GLTFSkin[jsonSkins.length()];
        for (int i = 0;i < jsonSamplers.length(); i++){
            this.skins[i] = GLTFSkin.fromJSONObject(jsonSkins.getJSONObject(i), this.accessors);
        }

        //TODO implement camera - low priority
        //Creating the nodes
        JSONArray jsonNodes = jsonAsset.getJSONArray("nodes");
        this.nodes = new GLTFNode[jsonNodes.length()];
        for (int i = 0;i < jsonNodes.length(); i++){
            this.nodes[i] = GLTFNode.fromJSONObject(jsonNodes.getJSONObject(i), this.meshes, this.skins);
        }

        //POST PROCESSING
        for (GLTFSkin skin : this.skins){
            skin.postFill_Nodes(this.nodes);
        }
        for (GLTFNode node : this.nodes){
            node.postFill_nodes(this.nodes);
        }
    }

    private JSONObject getJSON(File assetFile) throws IOException, GLTFParseException {
        String mimeType = assetFile.getName().split("\\.")[1];

        if(mimeType.equals("gltf")){
            FileInputStream inputStream = new FileInputStream(assetFile);
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            inputStream.close();
            return new JSONObject(json);
        }else if(mimeType.equals("glb")){
            DataInputStream inputStream = new DataInputStream(new FileInputStream(assetFile));

            long magic = GLTFBuffer.readUnsignedInt(inputStream);
            if(magic != GLTFBuffer.GLBmagic){
                throw new GLTFParseException("The GLB Magic in the file: " + magic + " isn't equal to the constant: " + GLTFBuffer.GLBmagic + ". File corrupted!");
            }
            long version = GLTFBuffer.readUnsignedInt(inputStream);
            long fileLength = GLTFBuffer.readUnsignedInt(inputStream);

            long jsonChunkLength = GLTFBuffer.readUnsignedInt(inputStream);
            long chunkType = GLTFBuffer.readUnsignedInt(inputStream);

            byte[] jsonChunkData = new byte[(int) jsonChunkLength];
            inputStream.read(jsonChunkData);

            String jsonString = new String(jsonChunkData, StandardCharsets.UTF_8);

            inputStream.close();
            return new JSONObject(jsonString);
        }else{
            throw new GLTFParseException("mime type " + mimeType + " not supported");
        }

    }

}
