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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;

/**
 *
 * @author kevin
 */
public class Converter {
    
    public static String toJson(JComponent component){
        switch (component) {
            case CodeArea codeArea -> {
                return toJson(codeArea);
            }
            case ImagePanel imagePanel -> {
                return toJson(imagePanel);
            }
            default -> {
                 return "";
            }
        }
    }
    
    public static String toJson(CodeArea component) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();

        node.put("text", component.getText());
        node.put("pos_x", component.getPosX());
        node.put("pos_y", component.getPosY());
        node.put("width", component.getWidth());
        node.put("height", component.getHeight());
        node.put("bgColor", component.getBg().toString());
        node.put("fgColor", component.getFg().toString());
        node.put("locked", component.isLocked());

        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not create JSON", ex);
        }
    }
    
    public static String toJson(ImagePanel component) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();

        node.put("file", component.getPath());
        node.put("pos_x", component.getPosX());
        node.put("pos_y", component.getPosY());
        node.put("width", component.getWidth());
        node.put("height", component.getHeight());
        node.put("locked", component.isLocked());

        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not create JSON", ex);
        }
    }
    
    public static CodeArea.Instructions toCodeArea(String jsonString){
        ObjectMapper mapper = new ObjectMapper();

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
        
        Color bgColor = Converter.colorExtractor(node.get("bgColor").asText());
        Color fgColor = Converter.colorExtractor(node.get("fgColor").asText());
    
        return new CodeArea.Instructions(text, pos_x, pos_y, width, height, bgColor, fgColor, locked);
    }
    
    public static ImagePanel.Instructions toImagePanel(String jsonString){
        ObjectMapper mapper = new ObjectMapper();

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
    
        return new ImagePanel.Instructions(file, pos_x, pos_y, width, height, locked);
    }
    
    private static Color colorExtractor(String colorString){
        Pattern pattern = Pattern.compile(
            "java\\.awt\\.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\]"
        );

        Matcher matcher = pattern.matcher(colorString);

        if (matcher.matches()) {
            int r = Integer.parseInt(matcher.group(1));
            int g = Integer.parseInt(matcher.group(2));
            int b = Integer.parseInt(matcher.group(3));

            Color color = new Color(r, g, b);

            return color;
        }
        //placeholder default value
        return new Color(255, 255, 255);
    }
}
