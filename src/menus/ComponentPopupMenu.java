/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menus;

import javax.swing.JPopupMenu;
import actions.*;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

/**
 *
 * @author kevin
 */
public class ComponentPopupMenu extends JPopupMenu{
    
    public ComponentPopupMenu(ComponentActions component)
    {
        JMenuItem copy = new JMenuItem("Copiar componente");
        JMenuItem paste = new JMenuItem("Colar componente");
        JMenuItem cut = new JMenuItem("Recortar componente");
        JMenuItem delete = new JMenuItem("Excluir componente");
        JCheckBoxMenuItem lock = new JCheckBoxMenuItem("Travar componente");

        copy.addActionListener(e -> component.copyComponent());
        paste.addActionListener(e -> component.pasteComponent());
        cut.addActionListener(e -> component.cutComponent());
        delete.addActionListener(e -> component.deleteComponent());
        lock.addActionListener(e -> component.setLocked(lock.isSelected()));

        add(copy);
        add(paste);
        add(cut);
        addSeparator();
        add(delete);
        add(lock);
    }
}