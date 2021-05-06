package gltf;

import jdk.jshell.spi.ExecutionControl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public Main(){
        File baseDirectory = new File(getClass().getResource("/testAssets").getFile());
        File[] assetDirectories = baseDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        List<Duration> glbDurations = new LinkedList<>();
        List<Duration> gltfDurations = new LinkedList<>();

        for (File asset : assetDirectories){
            File glbAssetFile = new File(asset + "/glb/" + asset.getName() + ".glb");
            File gltfAssetFile = new File(asset + "/gltf/" + asset.getName() + ".gltf");

            try{
                Instant glbStart = Instant.now();
                GLTFAsset glbAsset = new GLTFAsset(glbAssetFile.toURI());
                Instant glbEnd = Instant.now();

                glbDurations.add(Duration.between(glbStart, glbEnd));
            } catch (GLTFParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionControl.NotImplementedException e) {
                e.printStackTrace();
            }

            try{
                Instant gltfStart = Instant.now();
                GLTFAsset gltfAsset = new GLTFAsset(glbAssetFile.toURI());
                Instant gltfEnd = Instant.now();

                gltfDurations.add(Duration.between(gltfStart, gltfEnd));
            } catch (GLTFParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionControl.NotImplementedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String... arg) throws ExecutionControl.NotImplementedException, IOException, GLTFParseException, URISyntaxException {
        Main main = new Main();
    }
}
