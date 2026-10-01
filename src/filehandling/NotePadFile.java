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
import components.*;
import static filehandling.Converter.toJsonNode;
import forms.NotepadForm;
import java.awt.Color;
import java.io.*;
import java.nio.file.Files;

/**
 *
 * @author kevin
 */
public class NotePadFile {
    private final static ObjectMapper mapper = new ObjectMapper();
    
    private static final int VERSION = 1;
    public static File currFolder = null;
    
    public static void save(NotepadForm form, ObjectNode textAreaJson){
        if (currFolder == null){
            newNotePad(form.createFolder());
        } 
        
        if(currFolder != null){
            saveImages();
            String data = saveJson(textAreaJson);
            writeData(data);
        }   
    }
    
    public static void load(NotepadForm form, EditorPane editorPane){
        newNotePad(form.chooseFolder());
        if (currFolder != null){
            String data = readData();
            loadFromData(data, editorPane, form);
        }
    }
    
    private static String saveJson(ObjectNode textAreaJson){
        ObjectNode node = mapper.createObjectNode();

        try {
            node.put("version", VERSION);
            node.set("mainTextArea", textAreaJson);
            node.put("idCounter", EditorPane.getIdCounter());

            ArrayNode codeAreas = mapper.createArrayNode();

            EditorPane.codeAreas.forEach((k, v) -> {
                codeAreas.add(toJsonNode(v, false));
            });

            node.set("codeAreas", codeAreas);

            ArrayNode images = mapper.createArrayNode();

            EditorPane.images.forEach((k, v) -> {
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
            Files.writeString(jsonFile.toPath(), data);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not write to data.json", ex);
        }
    }    
    
    private static String readData(){
        File jsonFile = new File(currFolder, "data.json");
        try {
            return Files.readString(jsonFile.toPath());
        } catch (IOException ex){
            throw new IllegalArgumentException("Could not read data.json", ex);
        }
    }
    
    private static void saveImages() {
        File newFolder = new File(currFolder, "images");

        for (ImagePanel imagePanel : EditorPane.images.values()) {
            ImageFile file = imagePanel.getImageFile();
            file.copyTo(newFolder);
        }
    }
    
    private static void loadFromData(String data, EditorPane editorPane, NotepadForm form){
        try {
            ObjectNode node = parseJson(data);
            
            //mainTextArea
            JsonNode mainTextAreaNode = node.get("mainTextArea");
            String text = mainTextAreaNode.get("text").asText();
            Color bgColor = new Color(mainTextAreaNode.get("bgColor").asInt());
            Color fgColor = new Color(mainTextAreaNode.get("fgColor").asInt());
            boolean bold = mainTextAreaNode.get("bold").asBoolean();
            boolean italic = mainTextAreaNode.get("italic").asBoolean();
            String font = mainTextAreaNode.get("font").asText();
            int fontSize = mainTextAreaNode.get("fontSize").asInt();
            form.loadTextArea(text, bgColor, fgColor, bold, italic, font, fontSize);
            
            //IdCounter
            EditorPane.resetHashMaps();
            EditorPane.setIdCounter(node.get("idCounter").asInt());
            
            //CodeAreas
            ArrayNode codeAreasNode = (ArrayNode) node.get("codeAreas");
            for(JsonNode codeAreaNode: codeAreasNode){
                String jsonString = mapper.writeValueAsString(codeAreaNode);
                CodeArea.Instructions instructions = Converter.toCodeArea(jsonString, false);
                editorPane.add(CodeArea.createArea(instructions), instructions.layer());
            }
            
            //ImagePanels
            ArrayNode imagesNode = (ArrayNode) node.get("images");
            for(JsonNode imageNode: imagesNode){
                String jsonString = mapper.writeValueAsString(imageNode);
                ImagePanel.Instructions instructions = Converter.toImagePanel(jsonString, false);
                editorPane.add(ImagePanel.createPanel(instructions), instructions.layer());
            }
            
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not load from data", ex);
        }
    }
    
    public static void newNotePad(File folder){
        if (folder == null){
            return;
        }
        new File(folder, "images").mkdirs();
        File data = new File(folder, "data.json");
        try {
            data.createNewFile();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not create data.json", ex);
        }
        currFolder = folder;
    }
    
    private static ObjectNode parseJson(String data) {
        try {
            JsonNode node = mapper.readTree(data);

            if (node == null || !node.isObject()) {
                throw new IllegalArgumentException("Invalid notepad data");
            }
            
            if (!node.has("version")) {
                throw new IllegalArgumentException("Missing notepad version");
            }

            int version = node.get("version").asInt();

            if (version != VERSION) {
                throw new IllegalArgumentException("Unsupported notepad version: " + version);
            }

            return (ObjectNode) node;

        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Invalid notepad data", ex);
        }
    }
}
