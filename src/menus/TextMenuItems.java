package menus;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import localization.Localization;

/**
 *
 * @author kevin
 */
public class TextMenuItems{
    
    public static void addTo(JPopupMenu menu, JTextComponent component) {

        JMenuItem selectText = new JMenuItem(Localization.get("text.select"));
        JMenuItem cutText = new JMenuItem(Localization.get("text.cut"));
        JMenuItem copyText = new JMenuItem(Localization.get("text.copy"));
        JMenuItem pasteText = new JMenuItem(Localization.get("text.paste"));

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
