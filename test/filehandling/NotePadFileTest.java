package filehandling;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import components.*;
import java.awt.Color;
import java.nio.file.*;
import org.junit.jupiter.api.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kevin
 */
public class NotePadFileTest {
    private Path tempFolder;

    @AfterEach
    void tearDown() throws Exception {
        NotePadFile.currFolder = null;
        EditorPane.resetHashMaps();
        EditorPane.setIdCounter(0);

        if (tempFolder != null) {
            Files.walk(tempFolder)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception ignored) {
                        }
                    });
        }
    }

    @Test
    void shouldCreateNotepadFolderStructure() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-test");

        NotePadFile.newNotePad(tempFolder.toFile());

        assertEquals(
                tempFolder.toFile(),
                NotePadFile.currFolder
        );

        assertTrue(
                Files.isDirectory(tempFolder.resolve("images"))
        );

        assertTrue(
                Files.isRegularFile(tempFolder.resolve("data.json"))
        );
    }

    @Test
    void shouldDoNothingWhenFolderIsNull() {
        NotePadFile.currFolder = null;

        NotePadFile.newNotePad(null);

        assertNull(NotePadFile.currFolder);
    }

    @Test
    void shouldCreateDataFileOnlyOnce() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-test");

        NotePadFile.newNotePad(tempFolder.toFile());

        Path dataFile = tempFolder.resolve("data.json");

        assertTrue(Files.exists(dataFile));

        String originalContent = Files.readString(dataFile);

        NotePadFile.newNotePad(tempFolder.toFile());

        assertEquals(
                originalContent,
                Files.readString(dataFile)
        );
    }
    
    @Test
    void shouldSaveNotepadData() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-test");

        NotePadFile.newNotePad(tempFolder.toFile());

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode textAreaJson = mapper.createObjectNode();
        textAreaJson.put("text", "Hello World");
        textAreaJson.put("bgColor", Color.WHITE.getRGB());
        textAreaJson.put("fgColor", Color.BLACK.getRGB());
        textAreaJson.put("bold", false);
        textAreaJson.put("italic", false);
        textAreaJson.put("font", "Arial");
        textAreaJson.put("fontSize", 14);

        EditorPane.setIdCounter(10);

        NotePadFile.save(null, textAreaJson);

        Path dataFile = tempFolder.resolve("data.json");

        assertTrue(Files.exists(dataFile));

        String data = Files.readString(dataFile);

        JsonNode root = mapper.readTree(data);

        assertEquals(1, root.get("version").asInt());
        assertEquals(10, root.get("idCounter").asInt());

        JsonNode mainTextArea = root.get("mainTextArea");

        assertEquals(
                "Hello World",
                mainTextArea.get("text").asText()
        );

        assertEquals(
                Color.WHITE.getRGB(),
                mainTextArea.get("bgColor").asInt()
        );

        assertEquals(
                Color.BLACK.getRGB(),
                mainTextArea.get("fgColor").asInt()
        );

        assertFalse(mainTextArea.get("bold").asBoolean());
        assertFalse(mainTextArea.get("italic").asBoolean());
        assertEquals("Arial", mainTextArea.get("font").asText());
        assertEquals(14, mainTextArea.get("fontSize").asInt());

        assertTrue(root.get("codeAreas").isArray());
        assertTrue(root.get("images").isArray());

        assertEquals(0, root.get("codeAreas").size());
        assertEquals(0, root.get("images").size());
    }
    
    @Test
    void shouldSaveCodeAreas() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-test");

        NotePadFile.newNotePad(tempFolder.toFile());

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode textAreaJson = mapper.createObjectNode();
        textAreaJson.put("text", "");
        textAreaJson.put("bgColor", Color.WHITE.getRGB());
        textAreaJson.put("fgColor", Color.BLACK.getRGB());
        textAreaJson.put("bold", false);
        textAreaJson.put("italic", false);
        textAreaJson.put("font", "Arial");
        textAreaJson.put("fontSize", 14);

        EditorPane editorPane = new EditorPane();

        CodeArea codeArea = CodeArea.createArea();
        codeArea.setText("System.out.println(\"Hello\");");

        editorPane.add(codeArea, 1);

        EditorPane.codeAreas.put(codeArea.getId(), codeArea);

        NotePadFile.save(null, textAreaJson);

        String data = Files.readString(
                tempFolder.resolve("data.json")
        );

        JsonNode root = mapper.readTree(data);
        JsonNode codeAreas = root.get("codeAreas");

        assertEquals(1, codeAreas.size());

        JsonNode savedCodeArea = codeAreas.get(0);

        assertEquals(
                "CodeArea",
                savedCodeArea.get("type").asText()
        );

        assertEquals(
                "System.out.println(\"Hello\");",
                savedCodeArea.get("text").asText()
        );

        assertEquals(
                100,
                savedCodeArea.get("pos_x").asInt()
        );

        assertEquals(
                100,
                savedCodeArea.get("pos_y").asInt()
        );

        assertEquals(
                250,
                savedCodeArea.get("width").asInt()
        );

        assertEquals(
                100,
                savedCodeArea.get("height").asInt()
        );

        assertEquals(
                Color.GRAY.getRGB(),
                savedCodeArea.get("bgColor").asInt()
        );

        assertEquals(
                Color.WHITE.getRGB(),
                savedCodeArea.get("fgColor").asInt()
        );
    }

    @Test
    void shouldOverwriteExistingDataWhenSaving() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-test");

        NotePadFile.newNotePad(tempFolder.toFile());

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode firstTextArea = mapper.createObjectNode();
        firstTextArea.put("text", "First");
        firstTextArea.put("bgColor", Color.WHITE.getRGB());
        firstTextArea.put("fgColor", Color.BLACK.getRGB());
        firstTextArea.put("bold", false);
        firstTextArea.put("italic", false);
        firstTextArea.put("font", "Arial");
        firstTextArea.put("fontSize", 14);

        NotePadFile.save(null, firstTextArea);

        ObjectNode secondTextArea = mapper.createObjectNode();
        secondTextArea.put("text", "Second");
        secondTextArea.put("bgColor", Color.WHITE.getRGB());
        secondTextArea.put("fgColor", Color.BLACK.getRGB());
        secondTextArea.put("bold", false);
        secondTextArea.put("italic", false);
        secondTextArea.put("font", "Arial");
        secondTextArea.put("fontSize", 14);

        NotePadFile.save(null, secondTextArea);

        String data = Files.readString(
                tempFolder.resolve("data.json")
        );

        JsonNode root = mapper.readTree(data);

        assertEquals(
                "Second",
                root.get("mainTextArea").get("text").asText()
        );
    }
    
    @Test
    void shouldSaveImagePanels() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-test");

        NotePadFile.newNotePad(tempFolder.toFile());

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode textAreaJson = mapper.createObjectNode();
        textAreaJson.put("text", "");
        textAreaJson.put("bgColor", Color.WHITE.getRGB());
        textAreaJson.put("fgColor", Color.BLACK.getRGB());
        textAreaJson.put("bold", false);
        textAreaJson.put("italic", false);
        textAreaJson.put("font", "Arial");
        textAreaJson.put("fontSize", 14);

        Path originalImage = Files.createTempFile("codenotepad-image", ".png");

        BufferedImage image = new BufferedImage(
                100,
                100,
                BufferedImage.TYPE_INT_RGB
        );

        ImageIO.write(
                image,
                "png",
                originalImage.toFile()
        );

        ImagePanel imagePanel = ImagePanel.createPanel(
                originalImage.toFile()
        );

        EditorPane editorPane = new EditorPane();
        editorPane.add(imagePanel, 1);

        NotePadFile.save(null, textAreaJson);

        Path dataFile = tempFolder.resolve("data.json");
        Path imagesFolder = tempFolder.resolve("images");

        assertTrue(Files.exists(dataFile));
        assertTrue(Files.isDirectory(imagesFolder));

        JsonNode root = mapper.readTree(
                Files.readString(dataFile)
        );

        JsonNode images = root.get("images");

        assertEquals(1, images.size());

        JsonNode savedImage = images.get(0);

        assertEquals(
                "ImagePanel",
                savedImage.get("type").asText()
        );

        assertEquals(
                100,
                savedImage.get("pos_x").asInt()
        );

        assertEquals(
                100,
                savedImage.get("pos_y").asInt()
        );

        assertEquals(
                300,
                savedImage.get("width").asInt()
        );

        assertEquals(
                300,
                savedImage.get("height").asInt()
        );

        try (var files = Files.list(imagesFolder)) {
            assertEquals(1, files.count());
        }
    }
    
    @Test
    void shouldLoadMainTextArea() throws Exception {
        String json = """
            {
              "version": 1,
              "mainTextArea": {
                "text": "Hello CodeNotePad",
                "bgColor": 16777215,
                "fgColor": 0,
                "bold": true,
                "italic": false,
                "font": "Arial",
                "fontSize": 18
              },
              "idCounter": 5,
              "codeAreas": [],
              "images": []
            }
            """;

        EditorPane editorPane = new EditorPane();

        NotePadFile.MainTextAreaInstructions[] loaded =
                new NotePadFile.MainTextAreaInstructions[1];

        NotePadFile.loadFromData(
                json,
                editorPane,
                data -> loaded[0] = data
        );

        assertNotNull(loaded[0]);

        assertEquals(
                "Hello CodeNotePad",
                loaded[0].text()
        );

        assertEquals(
                Color.WHITE,
                loaded[0].bgColor()
        );

        assertEquals(
                Color.BLACK,
                loaded[0].fgColor()
        );

        assertTrue(loaded[0].bold());
        assertFalse(loaded[0].italic());

        assertEquals(
                "Arial",
                loaded[0].font()
        );

        assertEquals(
                18,
                loaded[0].fontSize()
        );

        assertEquals(
                5,
                EditorPane.getIdCounter()
        );
    }
    
    @Test
    void shouldRejectInvalidJson() throws Exception {
        String json = "{ invalid json";

        assertThrows(
                IllegalArgumentException.class,
                () -> NotePadFile.loadFromData(
                        json,
                        new EditorPane(),
                        data -> {}
                )
        );
    }

    @Test
    void shouldRejectMissingVersion() throws Exception {
        String json = """
            {
              "mainTextArea": {},
              "idCounter": 0,
              "codeAreas": [],
              "images": []
            }
            """;

        assertThrows(
                IllegalArgumentException.class,
                () -> NotePadFile.loadFromData(
                        json,
                        new EditorPane(),
                        data -> {}
                )
        );
    }

    @Test
    void shouldRejectUnsupportedVersion() throws Exception {
        String json = """
            {
              "version": 999,
              "mainTextArea": {},
              "idCounter": 0,
              "codeAreas": [],
              "images": []
            }
            """;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> NotePadFile.loadFromData(
                        json,
                        new EditorPane(),
                        data -> {}
                )
        );

        assertTrue(
                exception.getMessage().contains(
                        "Unsupported notepad version"
                )
        );
    }

    @Test
    void shouldLoadCodeAreas() throws Exception {
        String json = """
            {
              "version": 1,
              "mainTextArea": {
                "text": "",
                "bgColor": -1,
                "fgColor": -16777216,
                "bold": false,
                "italic": false,
                "font": "Arial",
                "fontSize": 12
              },
              "idCounter": 8,
              "codeAreas": [
                {
                  "type": "CodeArea",
                  "text": "System.out.println(\\\"Hello\\\");",
                  "pos_x": 120,
                  "pos_y": 150,
                  "width": 300,
                  "height": 200,
                  "bgColor": -8355712,
                  "fgColor": -1,
                  "locked": false,
                  "layer": 1,
                  "id": 7
                }
              ],
              "images": []
            }
            """;

        EditorPane editorPane = new EditorPane();

        NotePadFile.loadFromData(
                json,
                editorPane,
                data -> {}
        );

        assertEquals(8, EditorPane.getIdCounter());
        assertEquals(1, EditorPane.codeAreas.size());

        CodeArea codeArea = EditorPane.codeAreas.get(7);

        assertNotNull(codeArea);
        assertEquals(
                "System.out.println(\"Hello\");",
                codeArea.getText()
        );
        assertEquals(120, codeArea.getX());
        assertEquals(150, codeArea.getY());
        assertEquals(300, codeArea.getWidth());
        assertEquals(200, codeArea.getHeight());
    }
    
    @Test
    void shouldLoadImagePanels() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-test");

        Path imageFile = tempFolder.resolve("test-image.png");

        BufferedImage image = new BufferedImage(
                100,
                80,
                BufferedImage.TYPE_INT_RGB
        );

        ImageIO.write(
                image,
                "png",
                imageFile.toFile()
        );

        String json = """
            {
              "version": 1,
              "mainTextArea": {
                "text": "",
                "bgColor": -1,
                "fgColor": -16777216,
                "bold": false,
                "italic": false,
                "font": "Arial",
                "fontSize": 12
              },
              "idCounter": 8,
              "codeAreas": [],
              "images": [
                {
                  "type": "ImagePanel",
                  "file": "%s",
                  "pos_x": 120,
                  "pos_y": 150,
                  "width": 300,
                  "height": 250,
                  "locked": false,
                  "layer": 1,
                  "id": 7
                }
              ]
            }
            """.formatted(
                imageFile.toString().replace("\\", "\\\\")
            );

        EditorPane editorPane = new EditorPane();

        NotePadFile.loadFromData(
                json,
                editorPane,
                data -> {}
        );

        assertEquals(8, EditorPane.getIdCounter());
        assertEquals(1, EditorPane.images.size());

        ImagePanel imagePanel = EditorPane.images.get(7);

        assertNotNull(imagePanel);
        assertEquals(120, imagePanel.getX());
        assertEquals(150, imagePanel.getY());
        assertEquals(300, imagePanel.getWidth());
        assertEquals(250, imagePanel.getHeight());
        assertFalse(imagePanel.isLocked());

        assertNotNull(imagePanel.getImageFile());
        assertTrue(imagePanel.getImageFile().getFile().exists());
    }
    
    @Test
    void shouldSaveAndLoadNotepadData() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-test");

        NotePadFile.newNotePad(tempFolder.toFile());

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode textAreaJson = mapper.createObjectNode();
        textAreaJson.put("text", "Hello CodeNotePad");
        textAreaJson.put("bgColor", Color.WHITE.getRGB());
        textAreaJson.put("fgColor", Color.BLACK.getRGB());
        textAreaJson.put("bold", true);
        textAreaJson.put("italic", false);
        textAreaJson.put("font", "Arial");
        textAreaJson.put("fontSize", 18);

        EditorPane editorPane = new EditorPane();

        CodeArea codeArea = CodeArea.createArea();
        codeArea.setText("System.out.println(\"Hello\");");

        Path originalImage = Files.createTempFile(
                "codenotepad-image",
                ".png"
        );

        BufferedImage image = new BufferedImage(
                100,
                100,
                BufferedImage.TYPE_INT_RGB
        );

        ImageIO.write(
                image,
                "png",
                originalImage.toFile()
        );

        ImagePanel imagePanel = ImagePanel.createPanel(
                originalImage.toFile()
        );

        editorPane.add(codeArea, 0);
        editorPane.add(imagePanel, 1);

        EditorPane.codeAreas.put(
                codeArea.getId(),
                codeArea
        );

        EditorPane.images.put(
                imagePanel.getId(),
                imagePanel
        );

        int originalIdCounter = EditorPane.getIdCounter();

        NotePadFile.save(null, textAreaJson);

        String json = Files.readString(
                tempFolder.resolve("data.json")
        );

        EditorPane.resetHashMaps();

        EditorPane newEditorPane = new EditorPane();

        NotePadFile.MainTextAreaInstructions[] loaded =
                new NotePadFile.MainTextAreaInstructions[1];

        NotePadFile.loadFromData(
                json,
                newEditorPane,
                data -> loaded[0] = data
        );

        // MainTextArea

        assertNotNull(loaded[0]);

        assertEquals(
                "Hello CodeNotePad",
                loaded[0].text()
        );

        assertEquals(
                Color.WHITE,
                loaded[0].bgColor()
        );

        assertEquals(
                Color.BLACK,
                loaded[0].fgColor()
        );

        assertTrue(loaded[0].bold());
        assertFalse(loaded[0].italic());

        assertEquals(
                "Arial",
                loaded[0].font()
        );

        assertEquals(
                18,
                loaded[0].fontSize()
        );

        // IdCounter

        assertEquals(
                originalIdCounter,
                EditorPane.getIdCounter()
        );

        // CodeArea

        assertEquals(1, EditorPane.codeAreas.size());

        CodeArea loadedCodeArea =
                EditorPane.codeAreas.get(codeArea.getId());

        assertNotNull(loadedCodeArea);

        assertEquals(
                "System.out.println(\"Hello\");",
                loadedCodeArea.getText()
        );

        assertEquals(
                0,
                newEditorPane.getPosition(loadedCodeArea)
        );

        // ImagePanel

        assertEquals(1, EditorPane.images.size());

        ImagePanel loadedImagePanel =
                EditorPane.images.get(imagePanel.getId());

        assertNotNull(loadedImagePanel);

        assertNotNull(loadedImagePanel.getImageFile());

        assertTrue(
                loadedImagePanel.getImageFile().getFile().exists()
        );

        assertEquals(
                1,
                newEditorPane.getPosition(loadedImagePanel)
        );
    }
}
