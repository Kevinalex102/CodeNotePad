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
    private File file;
    
    public File getFile() {
        return file;
    }
    
    public CodeFile(String text, int id, String type)
    {
        Path tempPath = Paths.get("src/temp");
        tempPath = tempPath.toAbsolutePath();
        this.file = new File(tempPath + "/code" + id + type);
        this.text = text;
        write();
    }
    
    public void write(){
        try{
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void write(String newText){
        text = newText;
        try{
        FileWriter writer = new FileWriter(file);
        writer.write(newText);
        writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void openWith(){
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }
    
    public static void clearCache(){
        Path tempPath = Paths.get("src/temp");
        tempPath = tempPath.toAbsolutePath();
        System.out.println(tempPath);
        File folder = tempPath.toFile();
        try{
            for(File file:folder.listFiles()){
                file.delete();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
