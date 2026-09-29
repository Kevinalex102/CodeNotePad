/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;

/**
 *
 * @author kevin
 */
public class ImageFile {
    
    private File file;
    
    public ImageFile(File file, int id){
        this.file = ImageFile.copyToTemporary(file, id);
    }
        
    private static String getExtension(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');

        if (dot == -1) {
            throw new IllegalArgumentException("Image has no extension");
        }

        return name.substring(dot);
    }
    
    public static File copyToTemporary(File oldFile, int id){
        File newFile;
        Path oldPath = oldFile.toPath();
        String type = getExtension(oldPath);
        
        Path imagesPath = TemporaryStorage.getImagesFolder().resolve("image" + id + type);
        try {
            newFile = Files.copy(oldPath, imagesPath, StandardCopyOption.REPLACE_EXISTING).toFile();
        } catch (IOException ex) {
            throw new RuntimeException("Could not copy image: " + oldFile.toString(), ex);
        }
        
        return newFile;
    }
    
    public File copyTo(File newFolder){
        File newFile;
        Path oldPath = file.toPath();        
        String name = oldPath.getName(oldPath.getNameCount() - 1).toString();
        
        Path newPath = newFolder.toPath().resolve(name);
        try {
            newFile = Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING).toFile();
            this.file = newPath.toFile();
        } catch (IOException ex) {
             throw new RuntimeException("Could not copy image: " + oldPath.toString(), ex);
        }
        
        return newFile;
    }
    
    public static boolean isImage(File file) {
        if (file == null || !file.isFile()){
            return false;
        }
        
        try {
            BufferedImage image = ImageIO.read(file);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }
    
    public File getFile(){
        return file;
    }
}
