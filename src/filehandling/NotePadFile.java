/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import components.CodeArea;
import components.ImagePanel;
import static filehandling.Converter.toJsonNode;
import forms.NotepadForm;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
        saveImages();
        String data = saveJson(textAreaJson);
        writeData(data);
    }
    
    public static void load(NotepadForm form){
        new NotePadFile(form.chooseFolder());
        loadImages();
        String data = readData();
        loadFromData(data);
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
    
    private static String readData(){
        File jsonFile = new File(currFolder, "data.json");
        String data = "";
        int i;
        try {
            var reader = new FileReader(jsonFile);
            while ((i = reader.read()) != -1) {
                data += (char)i;
            }
        } catch (IOException ex){
            throw new IllegalArgumentException("Could not read data.json", ex);
        }
        return data;
    }
    
    private static void saveImages(){
        File newFolder = new File(currFolder, "images");
        try{
            for(ImagePanel imagePanel: ImagePanel.images.values()){
                ImageFile file = imagePanel.getImageFile();
                file.copyFile(newFolder);
            }
        }
        catch(Exception ex)
        {
            throw new IllegalArgumentException("Could not save images", ex);
        }
    }
    
    private static void loadImages(){
        
    }
    
    private static void loadFromData(String data){
        try {
            ObjectNode node = (ObjectNode) new ObjectMapper().readTree(data);
            
            JsonNode mainTextAreaNode = node.get("mainTextArea");
            // loadmainTextArea(mainTextAreaNode);
            System.out.println(mainTextAreaNode.get("text"));
            
            CodeArea.resetHashMap();
            CodeArea.setIdCounter(node.get("codeAreasIdCounter").asInt());
            System.out.println(CodeArea.getIdCounter());
            ImagePanel.resetHashMap();
            ImagePanel.setIdCounter(node.get("imagesIdCounter").asInt());
            System.out.println(ImagePanel.getIdCounter());
            
            ArrayNode codeAreasNode = (ArrayNode) node.get("codeAreas");
            for(JsonNode codeAreaNode: codeAreasNode){
                //Converter.toCodeArea();
                System.out.println(codeAreaNode.get("text"));
            }
            
            ArrayNode imagesNode = (ArrayNode) node.get("images");
            for(JsonNode imageNode: imagesNode){
                //Converter.imagePanel();
                System.out.println(imageNode.get("file"));
            }
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not load from data", ex);
        }
    }
    
    private File newNotePad(File folder){
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
