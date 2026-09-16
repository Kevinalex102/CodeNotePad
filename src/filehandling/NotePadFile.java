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

/**
 *
 * @author kevin
 */
public class NotePadFile {
    private final static ObjectMapper mapper = new ObjectMapper();
    
    public static void save(ObjectNode textAreaJson){
        saveJson(textAreaJson);
    }
    
    private static void saveJson(ObjectNode textAreaJson){
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

            System.out.println(json);

        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not create project JSON", ex);
        }
    }
    
    public static void load(){
        
    }
}
