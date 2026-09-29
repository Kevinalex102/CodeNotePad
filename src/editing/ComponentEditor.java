/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editing;

import components.CodeArea;
import components.EditorPane;
import components.ImagePanel;
import filehandling.Converter;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;

/**
 *
 * @author kevin
 */
public class ComponentEditor {
    public static void copyComponent(JComponent component) {
        StringSelection jsonInstructions = new StringSelection(Converter.toJson(component, true));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(jsonInstructions, null);
    }
    
    public static void pasteComponent(Container container) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            try {
                String clipboardData = (String)clipboard.getData(DataFlavor.stringFlavor);
                switch (Converter.getType(clipboardData)) {
                    case "CodeArea" -> {
                        CodeArea.Instructions instructions =
                                Converter.toCodeArea(clipboardData, true);
                        container.add(CodeArea.createArea(instructions));
                    }
                    case "ImagePanel" -> {
                        ImagePanel.Instructions instructions =
                                Converter.toImagePanel(clipboardData, true);
                        container.add(ImagePanel.createPanel(instructions));
                    }
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                System.getLogger(ComponentEditor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (IllegalArgumentException ex) {
                // Clipboard does not contain valid CodeNotePad component data.
            }
        }
    }
    
    public static void cutComponent(JComponent component) {
        copyComponent(component);
        deleteComponent(component);
    }
    
    public static void deleteComponent(JComponent component) {
        Container parent = component.getParent();
        if (parent instanceof EditorPane editorPane) {
            editorPane.removeComponent(component);
        }
    }
    
    public static void pushup(JComponent component){
        Container parent = component.getParent();
        if (parent instanceof EditorPane editorPane){
            editorPane.setPosition(component, 0);
        }
    }    
}
