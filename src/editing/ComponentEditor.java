/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editing;

import components.CodeArea;
import components.EditorPane;
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
        StringSelection jsonInstructions = new StringSelection(Converter.toJson(component));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(jsonInstructions, null);
    }
    
    public static void pasteComponent(Container container) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            try {
                CodeArea.Instructions newAreaInstructions = Converter.toCodeArea((String) clipboard.getData(DataFlavor.stringFlavor));
                container.add(CodeArea.createArea(newAreaInstructions));
                
            } catch (UnsupportedFlavorException ex) {
                System.getLogger(CodeArea.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (IOException ex) {
                System.getLogger(CodeArea.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
    
    public static void cutComponent(JComponent component) {
        copyComponent(component);
        deleteComponent(component);
    }
    
    public static void deleteComponent(JComponent component) {
        EditorPane parent = (EditorPane) component.getParent();
        parent.removeComponent(component);
    }
}
