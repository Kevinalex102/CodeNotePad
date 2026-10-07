package menus;

import actions.ExportActions;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import localization.Localization;

/**
 *
 * @author kevin
 */
public class ExportMenu extends JMenu{

    public JMenu exportMenu = new JMenu(Localization.get("component.export"));
    
    public ExportMenu(ExportActions component) {
        JMenuItem exportTxt = new JMenuItem(".txt");
        JMenuItem exportJar = new JMenuItem(".java");
        JMenuItem exportCs = new JMenuItem(".cs");
        JMenuItem exportJs = new JMenuItem(".js");
        JMenuItem exportHtml = new JMenuItem(".html");
        JMenuItem exportCss = new JMenuItem(".css");
        
        exportTxt.addActionListener(e -> component.export(".txt"));
        exportJar.addActionListener(e -> component.export(".java"));
        exportCs.addActionListener(e -> component.export(".cs"));
        exportJs.addActionListener(e -> component.export(".js"));
        exportHtml.addActionListener(e -> component.export(".html"));
        exportCss.addActionListener(e -> component.export(".cs"));
        
        exportMenu.add(exportTxt);
        exportMenu.addSeparator();
        exportMenu.add(exportJar);
        exportMenu.add(exportCs);
        exportMenu.add(exportJs);
        exportMenu.add(exportHtml);
        exportMenu.add(exportCss);
    }
}
