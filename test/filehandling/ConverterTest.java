package filehandling;

import components.*;
import org.junit.jupiter.api.Test;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kevin
 */
class ConverterTest {

    @Test
    void shouldConvertTextAreaToJson() {
        JTextArea textArea = new JTextArea("Hello");
        textArea.setBackground(Color.RED);
        textArea.setForeground(Color.BLUE);
        textArea.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));

        var node = Converter.toJsonNode(textArea);

        assertEquals("Hello", node.get("text").asText());
        assertEquals(Color.RED.getRGB(), node.get("bgColor").asInt());
        assertEquals(Color.BLUE.getRGB(), node.get("fgColor").asInt());
        assertTrue(node.get("bold").asBoolean());
        assertTrue(node.get("italic").asBoolean());
        assertEquals("Arial", node.get("font").asText());
        assertEquals(20, node.get("fontSize").asInt());
    }

    @Test
    void shouldGetComponentType() {
        String json = """
                {
                    "type": "CodeArea"
                }
                """;

        assertEquals("CodeArea", Converter.getType(json));
    }

    @Test
    void shouldRejectInvalidJson() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Converter.getType("{ invalid json")
        );
    }

    @Test
    void shouldRejectJsonWithoutType() {
        String json = """
                {
                    "text": "Hello"
                }
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> Converter.getType(json)
        );
    }

    @Test
    void shouldConvertCodeAreaJson() {
        String json = """
                {
                    "type": "CodeArea",
                    "text": "Hello",
                    "pos_x": 100,
                    "pos_y": 200,
                    "width": 300,
                    "height": 150,
                    "bgColor": -1,
                    "fgColor": -16777216,
                    "locked": true,
                    "id": 5,
                    "layer": 2
                }
                """;

        CodeArea.Instructions result =
                Converter.toCodeArea(json, false);

        assertEquals("Hello", result.text());
        assertEquals(100, result.pos_x());
        assertEquals(200, result.pos_y());
        assertEquals(300, result.width());
        assertEquals(150, result.height());
        assertEquals(2, result.layer());
        assertEquals(Color.WHITE, result.bgColor());
        assertEquals(Color.BLACK, result.fgColor());
        assertTrue(result.locked());
        assertEquals(5, result.id());
    }

    @Test
    void shouldOffsetCodeAreaWhenPasting() {
        String json = """
                {
                    "type": "CodeArea",
                    "text": "Hello",
                    "pos_x": 100,
                    "pos_y": 200,
                    "width": 300,
                    "height": 150,
                    "bgColor": -1,
                    "fgColor": -16777216,
                    "locked": false
                }
                """;

        CodeArea.Instructions result =
                Converter.toCodeArea(json, true);

        assertEquals(110, result.pos_x());
        assertEquals(210, result.pos_y());
        assertNull(result.id());
        assertEquals(0, result.layer());
    }

    @Test
    void shouldRequireIdAndLayerWhenLoadingCodeArea() {
        String json = """
                {
                    "type": "CodeArea",
                    "text": "Hello",
                    "pos_x": 0,
                    "pos_y": 0,
                    "width": 100,
                    "height": 100,
                    "bgColor": -1,
                    "fgColor": -16777216,
                    "locked": false
                }
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> Converter.toCodeArea(json, false)
        );
    }

    @Test
    void shouldConvertImagePanelJson() {
        String json = """
                {
                    "type": "ImagePanel",
                    "file": "image.png",
                    "pos_x": 100,
                    "pos_y": 200,
                    "width": 300,
                    "height": 150,
                    "locked": true,
                    "id": 5,
                    "layer": 2
                }
                """;

        ImagePanel.Instructions result =
                Converter.toImagePanel(json, false);

        assertEquals("image.png", result.file());
        assertEquals(100, result.pos_x());
        assertEquals(200, result.pos_y());
        assertEquals(300, result.width());
        assertEquals(150, result.height());
        assertEquals(2, result.layer());
        assertTrue(result.locked());
        assertEquals(5, result.id());
    }

    @Test
    void shouldOffsetImagePanelWhenPasting() {
        String json = """
                {
                    "type": "ImagePanel",
                    "file": "image.png",
                    "pos_x": 100,
                    "pos_y": 200,
                    "width": 300,
                    "height": 150,
                    "locked": false
                }
                """;

        ImagePanel.Instructions result =
                Converter.toImagePanel(json, true);

        assertEquals(110, result.pos_x());
        assertEquals(210, result.pos_y());
        assertNull(result.id());
        assertEquals(0, result.layer());
    }

    @Test
    void shouldRequireIdAndLayerWhenLoadingImagePanel() {
        String json = """
                {
                    "type": "ImagePanel",
                    "file": "image.png",
                    "pos_x": 0,
                    "pos_y": 0,
                    "width": 100,
                    "height": 100,
                    "locked": false
                }
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> Converter.toImagePanel(json, false)
        );
    }
}
