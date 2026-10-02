package components;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.JTextArea;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kevin
 */
public class EditorPaneTest {

    private EditorPane editorPane;
    private JTextArea background;

    @BeforeEach
    void setUp() {
        EditorPane.resetHashMaps();
        EditorPane.setIdCounter(0);

        editorPane = new EditorPane();
        background = new JTextArea();

        editorPane.add(background);
        editorPane.setBackgroundComponent(background);
    }

    @AfterEach
    void tearDown() {
        EditorPane.resetHashMaps();
        EditorPane.setIdCounter(0);
    }

    @Test
    void shouldStartWithEmptyComponentMaps() {
        assertTrue(EditorPane.codeAreas.isEmpty());
        assertTrue(EditorPane.images.isEmpty());
    }

    @Test
    void shouldStartIdCounterAtZero() {
        assertEquals(0, EditorPane.getIdCounter());
    }

    @Test
    void shouldGenerateIncrementingIds() {
        int first = EditorPane.newId();
        int second = EditorPane.newId();
        int third = EditorPane.newId();

        assertEquals(0, first);
        assertEquals(1, second);
        assertEquals(2, third);
        assertEquals(3, EditorPane.getIdCounter());
    }

    @Test
    void shouldSetIdCounter() {
        EditorPane.setIdCounter(42);

        assertEquals(42, EditorPane.getIdCounter());

        assertEquals(42, EditorPane.newId());
        assertEquals(43, EditorPane.getIdCounter());
    }

    @Test
    void shouldResetComponentMaps() throws Exception{
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
        
        CodeArea codeArea = CodeArea.createArea();
        ImagePanel imagePanel = ImagePanel.createPanel(originalImage.toFile());

        EditorPane.codeAreas.put(codeArea.getId(), codeArea);
        EditorPane.images.put(imagePanel.getId(), imagePanel);

        assertFalse(EditorPane.codeAreas.isEmpty());
        assertFalse(EditorPane.images.isEmpty());

        EditorPane.resetHashMaps();

        assertTrue(EditorPane.codeAreas.isEmpty());
        assertTrue(EditorPane.images.isEmpty());
    }

    @Test
    void shouldAddComponent() {
        JTextArea textArea = new JTextArea();

        Component result = editorPane.add(textArea);

        assertSame(textArea, result);
        assertTrue(editorPane.getComponents().length >= 2);
        assertTrue(containsComponent(textArea));
    }

    @Test
    void shouldAddComponentAtSpecifiedPosition() {
        JTextArea first = new JTextArea();
        JTextArea second = new JTextArea();
        JTextArea third = new JTextArea();
        
        editorPane.add(first);
        editorPane.add(second); 
        editorPane.add(third, 1); 
        
        assertEquals(0, editorPane.getPosition(first)); 
        assertEquals(1, editorPane.getPosition(third)); 
        assertEquals(2, editorPane.getPosition(second));
    }

    @Test
    void shouldRemoveComponent() {
        JTextArea textArea = new JTextArea();

        editorPane.add(textArea);

        assertTrue(containsComponent(textArea));

        editorPane.removeComponent(textArea);

        assertFalse(containsComponent(textArea));
    }

    @Test
    void shouldSetBackgroundComponent() {
        JTextArea newBackground = new JTextArea();

        editorPane.setBackgroundComponent(newBackground);

        assertEquals(-10, editorPane.getLayer(newBackground));
    }

    @Test
    void shouldResizeBackgroundComponentDuringLayout() {
        editorPane.setSize(800, 600);

        editorPane.doLayout();

        assertEquals(0, background.getX());
        assertEquals(0, background.getY());
        assertEquals(800, background.getWidth());
        assertEquals(600, background.getHeight());
    }

    @Test
    void shouldKeepBackgroundComponentAfterRemoveAll() {
        JTextArea otherComponent = new JTextArea();

        editorPane.add(otherComponent);

        assertTrue(containsComponent(otherComponent));
        assertTrue(containsComponent(background));

        editorPane.removeAll();

        assertFalse(containsComponent(otherComponent));
        assertTrue(containsComponent(background));
    }

    @Test
    void shouldResetMapsWhenRemoveAllIsCalled() throws Exception{
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
        
        CodeArea codeArea = CodeArea.createArea();
        ImagePanel imagePanel = ImagePanel.createPanel(originalImage.toFile());

        EditorPane.codeAreas.put(codeArea.getId(), codeArea);
        EditorPane.images.put(imagePanel.getId(), imagePanel);

        assertFalse(EditorPane.codeAreas.isEmpty());
        assertFalse(EditorPane.images.isEmpty());

        editorPane.removeAll();

        assertTrue(EditorPane.codeAreas.isEmpty());
        assertTrue(EditorPane.images.isEmpty());
    }

    @Test
    void shouldKeepBackgroundComponentAtNegativeLayerAfterRemoveAll() {
        editorPane.removeAll();

        assertEquals(-10, editorPane.getLayer(background));
    }

    private boolean containsComponent(Component component) {
        for (Component current : editorPane.getComponents()) {
            if (current == component) {
                return true;
            }
        }

        return false;
    }
}
