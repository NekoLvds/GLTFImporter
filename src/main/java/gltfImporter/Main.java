package gltfImporter;

import jdk.jshell.spi.ExecutionControl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;

public class Main {

    public Main() throws ExecutionControl.NotImplementedException, IOException, GLTFParseException, URISyntaxException {
        File glbAssetFile = new File(getClass().getResource("/fox/glb/Fox.glb").getFile());
        File gltfAssetFile = new File(getClass().getResource("/fox/gltf/Fox.gltf").getFile());

        Instant glbStart = Instant.now();
        GLTFAsset glbAsset = new GLTFAsset(glbAssetFile);
        Instant glbEnd = Instant.now();

        Instant gltfStart = Instant.now();
        GLTFAsset gltfAsset = new GLTFAsset(gltfAssetFile);
        Instant gltfEnd = Instant.now();

        long milisGLB = Duration.between(glbStart, glbEnd).toMillis();
        long milisGLTF = Duration.between(gltfStart, gltfEnd).toMillis();

        System.out.println("glb parsing took: " + milisGLB + " milliseconds");
        System.out.println("gltf parsing took: " + milisGLTF + " milliseconds");
    }

    public static void main(String... arg) throws ExecutionControl.NotImplementedException, IOException, GLTFParseException, URISyntaxException {
        Main main = new Main();
    }
}
