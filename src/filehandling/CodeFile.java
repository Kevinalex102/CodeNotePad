/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

/**
 *
 * @author kevin
 */
public class CodeFile {
    
    private String text;
    private final File file;
    private static Path tempPath = null;
    
    public CodeFile(String text, int id, String type)
    {
        if(tempPath == null){
            initializeTempPath();
        }
        
        this.file = new File(tempPath + "/code" + id + type);
        this.text = text;
        write();
    }
    
    private static void initializeTempPath(){
        try{
            tempPath = Path.of(
                System.getProperty("java.io.tmpdir"),
                "CodeNotePad",
                "codeFiles"
            );
            Files.createDirectories(tempPath);
        }
        catch(IOException ex){
            throw new RuntimeException("Could not create temp path", ex);
        }
    }
    
    private void write(){
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(text);
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Could write to file: " + file.toString(), ex);
        }
    }
    
    public void write(String newText){
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(newText);
            text = newText;
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Could write to file: " + file.toString(), ex);
        }
    }
    
    public void openWith(){
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                throw new RuntimeException("Could not open: " + file.toString(), ex);
            }
        } else {
            throw new RuntimeException("File format not supported");
        }
    }
    
    public static void clearCache(){
        if (tempPath == null){
            return;
        }
        
        File folder = tempPath.toFile();
        try{
            for(File file:folder.listFiles()){
                Files.delete(file.toPath());
            }
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Could not clear cache");
        }
    }
    
    public File getFile() {
        return file;
    }
}
