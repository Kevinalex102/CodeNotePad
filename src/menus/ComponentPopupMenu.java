package menus;

import javax.swing.JPopupMenu;
import actions.*;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import localization.Localization;

/**
 *
 * @author kevin
 */
public class ComponentPopupMenu extends JPopupMenu{
    
    public ComponentPopupMenu(ComponentActions component)
    {
        JMenuItem copy = new JMenuItem(Localization.get("component.copy"));
        JMenuItem paste = new JMenuItem(Localization.get("component.paste"));
        JMenuItem cut = new JMenuItem(Localization.get("component.cut"));
        JMenuItem delete = new JMenuItem(Localization.get("component.delete"));
        JCheckBoxMenuItem lock = new JCheckBoxMenuItem(Localization.get("component.lock"));
        JMenuItem push = new JMenuItem(Localization.get("component.push"));

        copy.addActionListener(e -> component.copyComponent());
        paste.addActionListener(e -> component.pasteComponent());
        cut.addActionListener(e -> component.cutComponent());
        delete.addActionListener(e -> component.deleteComponent());
        lock.addActionListener(e -> component.setLocked(lock.isSelected()));
        push.addActionListener(e -> component.pushUp());

        add(copy);
        add(paste);
        add(cut);
        addSeparator();
        add(delete);
        add(lock);
        add(push);
    }
}