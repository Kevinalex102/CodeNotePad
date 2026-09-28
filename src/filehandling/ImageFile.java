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
        this.file = ImageFile.copyFile(file, id);
    }
    
    private static String getType(Path path){
        try {
            String mimeType = Files.probeContentType(path);
            if(mimeType != null)
            {
                return mimeType.replace("image/", "."); //Returns: ".png", ".jpeg"
            }
            else{
                return null;
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not get image type", ex);
        }
    }
    
    public static File copyFile(File oldFile, int id){
        File newFile;
        Path oldPath = oldFile.toPath();
        String type = getType(oldPath);
        
        Path imagesPath = TemporaryStorage.getImagesFolder();
        imagesPath = Path.of(imagesPath.toString(), "/image" + id + type);
        try {
            newFile = Files.copy(oldPath, imagesPath, StandardCopyOption.REPLACE_EXISTING).toFile();
        } catch (IOException ex) {
            throw new RuntimeException("Could not copy image: " + oldFile.toString(), ex);
        }
        
        return newFile;
    }
    
    public File copyFile(File newFolder){
        File newFile;
        Path oldPath = file.toPath();        
        String name = oldPath.getName(oldPath.getNameCount() - 1).toString();
        
        Path newPath = newFolder.toPath();
        newPath = Path.of(newPath.toString(), "/" + name);
        try {
            newFile = Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING).toFile();
            this.file = newPath.toFile();
        } catch (IOException ex) {
             throw new RuntimeException("Could not copy image: " + oldPath.toString(), ex);
        }
        
        return newFile;
    }
    
    public static File readFile(String filePath){
        return new File(filePath);
    }
    
    public static boolean isImage(File file) {
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
    
    public void updateFile(File file){
        this.file = file;
    }
}
