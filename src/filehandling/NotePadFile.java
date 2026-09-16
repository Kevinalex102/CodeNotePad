/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import components.CodeArea;
import components.ImagePanel;
import static filehandling.Converter.toJsonNode;
import static filehandling.ImageFile.getImagesFolder;
import forms.NotepadForm;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author kevin
 */
public class NotePadFile {
    private final static ObjectMapper mapper = new ObjectMapper();
    
    private File folder;
    public static File currFolder = null;
    
    public NotePadFile(File folder){
        this.folder = newNotePad(folder);
        currFolder = this.folder;
    }
    
    public static void save(NotepadForm form, ObjectNode textAreaJson){
        if(currFolder == null){
            new NotePadFile(form.chooseFolder());
        }
        String data = saveJson(textAreaJson);
        saveImages();
        writeData(data);
    }
    
    private static String saveJson(ObjectNode textAreaJson){
        ObjectNode node = mapper.createObjectNode();

        try {
            node.set("mainTextArea", textAreaJson);
            node.put("codeAreasIdCounter", CodeArea.getIdCounter());
            node.put("imagesIdCounter", ImagePanel.getIdCounter());

            ArrayNode codeAreas = mapper.createArrayNode();

            CodeArea.codeAreas.forEach((k, v) -> {
                codeAreas.add(toJsonNode(v, false));
            });

            node.set("codeAreas", codeAreas);

            ArrayNode images = mapper.createArrayNode();

            ImagePanel.images.forEach((k, v) -> {
                images.add(toJsonNode(v, false));
            });

            node.set("images", images);

            String json = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(node);

            return json;

        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not create project JSON", ex);
        }
    }
    
    private static void writeData(String data){
        File jsonFile = new File(currFolder, "data.json");
        try {
            var writer = new FileWriter(jsonFile, false);
            writer.write(data);
            writer.close();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not write to data.json", ex);
        }
    }
    
    public static void load(){
        
    }
    
    private static void saveImages(){
        File tempFolder = ImageFile.getImagesFolder();
        File newFolder = new File(currFolder, "images");
        try{
            for(File file:tempFolder.listFiles()){
                if (!file.getName().equals(".gitkeep")) {
                    ImageFile.copyFile(file, newFolder);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private File newNotePad(File folder){;
        this.folder = folder;
        new File(folder, "images").mkdir();
        File data = new File(folder, "data.json");
        try {
            data.createNewFile();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not create data.json", ex);
        }
        return folder;
    }
}
