package gltf;

import gltf.accessor.GLTFAccessor;
import gltf.animation.GLTFAnimation;
import gltf.buffer.GLTFBuffer;
import gltf.buffer.GLTFBufferView;
import gltf.camera.GLTFCamera;
import gltf.material.GLTFImage;
import gltf.material.GLTFMaterial;
import gltf.material.GLTFSampler;
import gltf.material.GLTFTexture;
import gltf.mesh.GLTFMesh;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * The GLTFAsset class is a abstract representation of a GLTF asset.
 *
 * <p></p>
 *
 * It stores the objects contained by a gltf asset and holds the binary data in a more usable and structured way in memory.
 *
 * @see GLTFAccessor
 * @see GLTFBuffer
 * @see GLTFBufferView
 * @see GLTFCamera
 * @see GLTFImage
 * @see GLTFMaterial
 * @see GLTFMesh
 * @see GLTFNode
 * @see GLTFSampler
 * @see GLTFSkin
 * @see GLTFTexture
 */
public class GLTFAsset {

    private final URI assetFile;
    private final URI assetDirectory;

    private final GLTFBuffer[] buffers;
    private final GLTFBufferView[] bufferViews;
    private final GLTFAccessor[] accessors;
    private final GLTFImage[] images;
    private final GLTFSampler[] samplers;
    private final GLTFTexture[] textures;
    private final GLTFMaterial[] materials;
    private final GLTFMesh[] meshes;
    private final GLTFSkin[] skins;
    private final GLTFCamera[] cameras;
    private final GLTFNode[] nodes;
    private final GLTFAnimation[] animations;

    /**
     * Constructs a new GLTF Asset from the given File.
     * <p></p>
     * That can either be a gltf (.gltf) or a bundled gltf (.glb). The Importer will do it's best to read the data from the uriFile(s).
     * <p></p>
     * This Importer works in the sense of the <a href="https://github.com/KhronosGroup/glTF/tree/master/specification/2.0">specification</a> given
     * by the Khronos Group. Version 2.0.
     * @param uriFile The File to be read.
     * @throws ExecutionControl.NotImplementedException Should not occur. Will be removed in future update.
     * @throws IOException If the File cannot be read. See the exception for further details.
     * @throws GLTFParseException If the gltf uriFile isn't as expected. See the exception message for more details.
     */
    public GLTFAsset(URI uriFile) throws ExecutionControl.NotImplementedException, IOException, GLTFParseException {
        //Reading/Creating basic info
        this.assetFile = uriFile;
        this.assetDirectory = uriFile.getPath().endsWith("/") ? uriFile.resolve("..") : uriFile.resolve(".");;

        //Getting the Json
        JSONObject jsonAsset = getJSON(this.assetFile);

        //Creating the buffers
        JSONArray jsonBuffers = jsonAsset.getJSONArray("buffers");
        this.buffers = new GLTFBuffer[jsonBuffers.length()];
        for (int i = 0;i < jsonBuffers.length(); i++){
            this.buffers[i] = GLTFBuffer.fromJSONObject(jsonBuffers.getJSONObject(i), this.assetFile, this.assetDirectory);
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

        //Creating the cameras
        if (jsonAsset.has("cameras")){
            JSONArray jsonCameras = jsonAsset.getJSONArray("cameras");
            this.cameras = new GLTFCamera[jsonCameras.length()];
            for (int i = 0;i < jsonCameras.length(); i++){
                this.cameras[i] = GLTFCamera.fromJSONObject(jsonCameras.getJSONObject(i));
            }
        }else{
            this.cameras = new GLTFCamera[]{};
        }

        //Creating the nodes
        JSONArray jsonNodes = jsonAsset.getJSONArray("nodes");
        this.nodes = new GLTFNode[jsonNodes.length()];
        for (int i = 0;i < jsonNodes.length(); i++){
            this.nodes[i] = GLTFNode.fromJSONObject(jsonNodes.getJSONObject(i), this.cameras, this.meshes, this.skins);
        }

        //Creating the animations
        JSONArray jsonAnimations = jsonAsset.getJSONArray("animations");
        this.animations = new GLTFAnimation[jsonAnimations.length()];
        for (int i = 0; i < jsonAnimations.length(); i++){
            this.animations[i] = GLTFAnimation.fromJsonObject(jsonAnimations.getJSONObject(i), this.accessors, this.nodes);
        }

        //POST PROCESSING
        for (GLTFSkin skin : this.skins){
            skin.postFill_Nodes(this.nodes);
        }
        for (GLTFNode node : this.nodes){
            node.postFill_nodes(this.nodes);
        }
    }

    /**
     * Reads the JSON from either the GLTF file or the GLB file.
     * @param assetFile The asset file.
     * @return The JSONObject containing the gltf json data.
     * @throws IOException If the File cannot be read for some reason. See exception message for more details.
     * @throws GLTFParseException If the File isn't as expected by GLTF Spec. See exception message for more details.
     */
    private JSONObject getJSON(URI assetFile) throws IOException, GLTFParseException {
        String fileName = assetFile.getPath().substring( assetFile.getPath().lastIndexOf('/')+1, assetFile.getPath().length() );
        String mimeType = fileName.split("\\.")[1];

        if(mimeType.equals("gltf")){
            InputStream inputStream = assetFile.toURL().openStream();
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            inputStream.close();
            return new JSONObject(json);
        }else if(mimeType.equals("glb")){
            DataInputStream inputStream = new DataInputStream(assetFile.toURL().openStream());

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
