/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author kevin
 */
public class CodeFile {
    
    private String text;
    private final File file;
    
    public CodeFile(String text, int id, String type)
    {
        Path tempPath = TemporaryStorage.getCodeFilesFolder();
        
        this.file = tempPath.resolve("code" + id + type).toFile();
        this.text = text;
        write();
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
    
    public File getFile() {
        return file;
    }
}
