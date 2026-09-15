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
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author kevin
 */
public class ImageFile {
    
    private File file;
    
    public ImageFile(File file, int id){
        this.file = createFile(file, id);
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
        } catch (IOException e) {
            return null;
        }
    }
    
    private static File createFile(File oldFile, int id){
        File newFile;
        Path oldPath = oldFile.toPath();
        String type = getType(oldPath);
        
        Path tempPath = Paths.get("src/temp/images");
        tempPath = Path.of(tempPath.toString(), "/image" + id + type);
        tempPath = tempPath.toAbsolutePath();
        try {
            newFile = Files.copy(oldPath, tempPath, StandardCopyOption.REPLACE_EXISTING).toFile();
        } catch (IOException e) {
            return null;
        }
        
        return newFile;
    }
    
    public static File readFile(JFrame form){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        int result = fileChooser.showOpenDialog(form);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(isImage(selectedFile)){
                return selectedFile;
            }
        }
        return null;
    }
    
    public static File readFile(String filePath){
        return new File(filePath);
    }
    
    private static boolean isImage(File file) {
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
    
    public static void clearCache(){
        Path imagesPath = Paths.get("src/temp/images");
        imagesPath = imagesPath.toAbsolutePath();
        File folder = imagesPath.toFile();
        try{
            for(File file:folder.listFiles()){
                if (!file.getName().equals(".gitkeep")) {
                    file.delete();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
