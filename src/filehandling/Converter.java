/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import components.*;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JTextArea;

/**
 *
 * @author kevin
 */
public class Converter {
    
    private final static ObjectMapper mapper = new ObjectMapper();
    
    public static String toJson(JComponent component, boolean copying){
        switch (component) {
            case CodeArea codeArea -> {
                return toJson(codeArea, copying);
            }
            case ImagePanel imagePanel -> {
                return toJson(imagePanel, copying);
            }
            default -> {
                 return "";
            }
        }
    }
    
    public static String toJson(CodeArea component, boolean copying) {
        ObjectNode node = toJsonNode(component, copying);
        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not create JSON", ex);
        }
    }
    
    public static ObjectNode toJsonNode(CodeArea component, boolean copying) {

        ObjectNode node = mapper.createObjectNode();
        
        Color bg = component.getBackground();
        Color fg = component.getForeground();

        node.put("type", "CodeArea");
        node.put("text", component.getText());
        node.put("pos_x", component.getX());
        node.put("pos_y", component.getY());
        node.put("width", component.getWidth());
        node.put("height", component.getHeight());
        node.put("bgColor", bg.getRGB());
        node.put("fgColor", fg.getRGB());

        if (copying) {
            node.put("locked", false);
        } else {
            node.put("locked", component.isLocked());
            node.put("layer", component.getLayer());
            node.put("id", component.getId());
        }

        return node;
    }
    
    public static String toJson(ImagePanel component, boolean copying) {
        ObjectNode node = toJsonNode(component, copying);

        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not create JSON", ex);
        }
    }
    
    public static ObjectNode toJsonNode(ImagePanel component, boolean copying) {
        ObjectNode node = mapper.createObjectNode();

        node.put("type", "ImagePanel");
        node.put("file", component.getPath());
        node.put("pos_x", component.getX());
        node.put("pos_y", component.getY());
        node.put("width", component.getWidth());
        node.put("height", component.getHeight());
        if(copying){
            node.put("locked", false);
        }
        else{
            node.put("locked", component.isLocked());
            node.put("id", component.getId());
            node.put("layer", component.getLayer());
        }

        return node;
    }
    
    public static ObjectNode toJsonNode(JTextArea textArea) {
        Font currFont = textArea.getFont();
        ObjectNode node = mapper.createObjectNode();
        Color bg = textArea.getBackground();
        Color fg = textArea.getForeground();

        node.put("text", textArea.getText());
        node.put("bgColor", bg.getRGB());
        node.put("fgColor", fg.getRGB());
        node.put("bold", currFont.isBold());
        node.put("italic", currFont.isItalic());
        node.put("font", currFont.getFamily());
        node.put("fontSize", currFont.getSize());

        return node;
    }
    
    public static CodeArea.Instructions toCodeArea(String jsonString, boolean pasting){
        JsonNode node;
        try {
            node = mapper.readTree(jsonString);
        } catch (JsonProcessingException ex) {
            System.getLogger(Converter.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            throw new IllegalArgumentException("Not a Json string", ex);
        }

        String text = node.get("text").asText();
        int pos_x = node.get("pos_x").asInt();
        int pos_y = node.get("pos_y").asInt();
        int width = node.get("width").asInt();
        int height = node.get("height").asInt();
        boolean locked = node.get("locked").asBoolean();
        
        Color bgColor = new Color(node.get("bgColor").asInt());
        Color fgColor = new Color(node.get("fgColor").asInt());
        
        Integer id = null;
        int layer = 0;
        
        if(pasting){
            pos_x += 10;
            pos_y += 10;
        }
        
        if(!pasting) {
            id = node.get("id").asInt();
            layer = node.get("layer").asInt();
        }
    
        return new CodeArea.Instructions(text, pos_x, pos_y, width, height, layer, bgColor, fgColor, locked, id);
    }
    
    public static ImagePanel.Instructions toImagePanel(String jsonString, boolean pasting){
        JsonNode node;
        try {
            node = mapper.readTree(jsonString);
        } catch (JsonProcessingException ex) {
            System.getLogger(Converter.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            throw new IllegalArgumentException("Not a Json string", ex);
        }

        String file = node.get("file").asText();
        int pos_x = node.get("pos_x").asInt();
        int pos_y = node.get("pos_y").asInt();
        int width = node.get("width").asInt();
        int height = node.get("height").asInt();
        boolean locked = node.get("locked").asBoolean();
        
        Integer id = null;
        int layer = 0;
        
        if(pasting){
            pos_x += 10;
            pos_y += 10;
        }
        
        if(!pasting) {
            id = node.get("id").asInt();
            layer = node.get("layer").asInt();
        }
    
        return new ImagePanel.Instructions(file, pos_x, pos_y, width, height, layer, locked, id);
    }
    
    public static String getType(String jsonString) {
        try {
            JsonNode node = mapper.readTree(jsonString);
            return node.get("type").asText();
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Invalid component JSON", ex);
        }
    }
}