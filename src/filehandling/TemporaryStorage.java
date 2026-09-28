/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author kevin
 */
public class TemporaryStorage {
    private static Path tempPath = null;
    private static Path codeFilesPath = null;
    private static Path imagesPath= null;
    
    private static void initializeTempPath(){
        try{
            tempPath = Path.of(
            System.getProperty("java.io.tmpdir"),
            "CodeNotePad"
        );

        codeFilesPath = tempPath.resolve("codeFiles");
        imagesPath = tempPath.resolve("images");

        Files.createDirectories(codeFilesPath);
        Files.createDirectories(imagesPath);
        }
        catch(IOException ex){
            throw new RuntimeException("Could not create temp path", ex);
        }
    }
    
    private static void clearDirectory(Path directory) throws IOException {
        try (var files = Files.list(directory)) {
            for (Path file : files.toList()) {
                Files.delete(file);
            }
        }
    }
    
    public static void clearCache(){
        if (tempPath == null){
            return;
        }
        try{
            clearDirectory(codeFilesPath);
            clearDirectory(imagesPath);
        }
        
        catch(IOException ex)
        {
            throw new RuntimeException("Could not clear cache", ex);
        }
    }
    
    public static Path getImagesFolder(){
        if (imagesPath == null){
            initializeTempPath();
        }
        return imagesPath;
    }
    
    public static Path getCodeFilesFolder(){
        if (codeFilesPath == null){
            initializeTempPath();
        }
        return codeFilesPath;
    }
}
