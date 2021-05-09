package gltf;

import gltf.accessor.GLTFAccessor;
import jdk.jshell.spi.ExecutionControl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {
    public Main(){
        File baseDirectory = new File(getClass().getResource("/glTF-Sample-Models/2.0").getFile());
        File[] assetDirectories = baseDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        List<Duration> glbDurations = new LinkedList<>();
        List<Duration> gltfDurations = new LinkedList<>();

        List<String> gltfErrorMessages = new LinkedList<>();

        for (File asset : assetDirectories){
            File glbAssetFile = new File(asset + "/gltf-Binary/" + asset.getName() + ".glb");
            File gltfAssetFile = new File(asset + "/gltf/" + asset.getName() + ".gltf");

            if (glbAssetFile.exists()){
                try{
                    Instant glbStart = Instant.now();
                    GLTFAsset glbAsset = new GLTFAsset(glbAssetFile.toURI());
                    Instant glbEnd = Instant.now();

                    System.out.println("Loaded: " + glbAssetFile);

                    glbDurations.add(Duration.between(glbStart, glbEnd));
                } catch (GLTFParseException e) {
                    gltfErrorMessages.add(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ExecutionControl.NotImplementedException e) {
                    e.printStackTrace();
                }
            }

            if (gltfAssetFile.exists()){
                try{
                    Instant gltfStart = Instant.now();
                    GLTFAsset gltfAsset = new GLTFAsset(gltfAssetFile.toURI());
                    Instant gltfEnd = Instant.now();

                    System.out.println("Loaded: " + gltfAssetFile);
                    
                    gltfDurations.add(Duration.between(gltfStart, gltfEnd));
                } catch (GLTFParseException e) {
                    gltfErrorMessages.add(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ExecutionControl.NotImplementedException e) {
                    e.printStackTrace();
                }
            }
        }

        Duration glbAvg = Duration.ZERO;
        for (Duration duration : glbDurations){
            glbAvg = glbAvg.plus(duration);
        }
        glbAvg = glbAvg.dividedBy(glbDurations.size());


        Duration gltfAvg = Duration.ZERO;
        for (Duration duration : gltfDurations){
            gltfAvg = gltfAvg.plus(duration);
        }
        gltfAvg = gltfAvg.dividedBy(gltfDurations.size());

        System.out.println("Average Durations:\n" +
                "GLTF \t:" + gltfAvg.toMillis() + " milliseconds.\n" +
                "GLB \t:" + glbAvg.toMillis() + " milliseconds\n");

        System.out.println("Encountered following errors: ");
        for (String error : gltfErrorMessages){
            System.out.println(error);
        }
    }

    public static void main(String... arg) throws ExecutionControl.NotImplementedException, IOException, GLTFParseException, URISyntaxException {
        Main main = new Main();
    }
}
