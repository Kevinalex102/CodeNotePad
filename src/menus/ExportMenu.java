/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menus;

import actions.ExportActions;
import components.CodeArea;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author kevin
 */
public class ExportMenu extends JMenu{

    public JMenu exportMenu = new JMenu("Exportar para...");
    
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
