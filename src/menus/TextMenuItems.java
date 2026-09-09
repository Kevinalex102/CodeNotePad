/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menus;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 *
 * @author kevin
 */
public class TextMenuItems{
    
    public static void addTo(JPopupMenu menu, JTextComponent component) {

        JMenuItem selectText = new JMenuItem("Selecionar tudo");
        JMenuItem cutText = new JMenuItem("Recortar texto");
        JMenuItem copyText = new JMenuItem("Copiar texto");
        JMenuItem pasteText = new JMenuItem("Colar texto");

        selectText.addActionListener(e -> component.selectAll());
        cutText.addActionListener(e -> component.cut());
        copyText.addActionListener(e -> component.copy());
        pasteText.addActionListener(e -> component.paste());

        menu.add(selectText);
        menu.add(cutText);
        menu.add(copyText);
        menu.add(pasteText);
    }
}
