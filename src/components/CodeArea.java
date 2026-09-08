/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

import filehandling.CodeFile;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.*;

/**
 *
 * @author kevin
 */
public class CodeArea extends JTextArea{
    
    private static int idCounter = 0;
    private int id;
    private CodeFile textFile = null;
    private boolean isLocked, resizing = false;
    
    private int pos_x, pos_y, height, width;
    
    private static HashMap<Integer, CodeArea> codeAreas = new HashMap<Integer, CodeArea>();
    
    private Point mouseOffset;
    private final JPopupMenu menu = new JPopupMenu();

    private CodeArea(){
        super(10, 10);
        
        this.id = idCounter;
        idCounter++;
        
        initializeMenu(this);
        MouseAdapter mouseHandler = addMouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        codeAreas.put(this.id, this);
    }
    
    public static CodeArea createArea(){
        CodeArea newCodeArea = new CodeArea();
        
        newCodeArea.setBounds(100, 100, 250, 100);
        newCodeArea.width = 250;
        newCodeArea.height = 100;
        newCodeArea.setBackground(Color.gray);
        newCodeArea.setForeground(Color.white);
        
        return newCodeArea;
    }

    private void ChangeBackground() {
        this.setBackground(ColorPicker.pickAColor(this.getBackground()));
    }

    private void ChangeForeground() {
        this.setForeground(ColorPicker.pickAColor(this.getForeground()));
    }
    
    private void deleteArea() {
        codeAreas.remove(this.id);
        EditorPane parent = (EditorPane) this.getParent();
        parent.removeComponent(this);
    }

    private void lockArea() {
        if(isLocked) isLocked = false;
        else isLocked = true;
    }

    private void export(String type) {
        
        if (textFile == null){
            textFile = new CodeFile(this.getText(), this.id, type);
        }
        else{
            textFile.write(this.getText());
        }
        textFile.openWith();
    }
    
    @Override
    public void resize(int newWidth, int newHeight){
        width = newWidth;
        height = newHeight;
        
        this.setBounds(pos_x, pos_y, newWidth, newHeight);
        
        revalidate();
        repaint();
    }
    
    private void initializeMenu(CodeArea codeArea){
        JMenuItem select = new JMenuItem("Selecionar tudo");
        JMenuItem cut = new JMenuItem("Recortar texto");
        JMenuItem copy = new JMenuItem("Copiar texto");
        JMenuItem paste = new JMenuItem("Colar texto");
        JMenuItem changeBackground = new JMenuItem("Mudar cor do fundo");
        JMenuItem changeForeground = new JMenuItem("Mudar cor do texto");
        JMenuItem delete = new JMenuItem("Excluir janela");
        JToggleButton lock = new JToggleButton("Travar janela");
        JMenu exportMenu = new JMenu("Exportar para...");
        JMenuItem exportTxt = new JMenuItem(".txt");
        JMenuItem exportJar = new JMenuItem(".java");
        JMenuItem exportCs = new JMenuItem(".cs");
        JMenuItem exportJs = new JMenuItem(".js");
        JMenuItem exportHtml = new JMenuItem(".html");
        JMenuItem exportCss = new JMenuItem(".css");
        
        select.addActionListener(e -> codeArea.selectAll());
        cut.addActionListener(e -> codeArea.cut());
        copy.addActionListener(e -> codeArea.copy());
        paste.addActionListener(e -> codeArea.paste());
        changeBackground.addActionListener(e -> ChangeBackground());
        changeForeground.addActionListener(e -> ChangeForeground());
        delete.addActionListener(e -> deleteArea());
        lock.addActionListener(e -> lockArea());
        exportTxt.addActionListener(e -> export(".txt"));
        exportJar.addActionListener(e -> export(".java"));
        exportCs.addActionListener(e -> export(".cs"));
        exportJs.addActionListener(e -> export(".js"));
        exportHtml.addActionListener(e -> export(".html"));
        exportCss.addActionListener(e -> export(".cs"));

        exportMenu.add(exportTxt);
        exportMenu.addSeparator();
        exportMenu.add(exportJar);
        exportMenu.add(exportCs);
        exportMenu.add(exportJs);
        exportMenu.add(exportHtml);
        exportMenu.add(exportCss);
        menu.add(select);
        menu.add(cut);
        menu.add(copy);
        menu.add(paste);
        menu.addSeparator();
        menu.add(changeBackground);
        menu.add(changeForeground);
        menu.addSeparator();
        menu.add(delete);
        menu.add(lock);
        menu.add(exportMenu);
    }
    
    private MouseAdapter addMouseHandler(){
        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) { 
                    mouseOffset = e.getPoint();
                
                    resizing = mouseOffset.x > width - 10 && mouseOffset.y > height - 10;
                    
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    menu.show(CodeArea.this, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseOffset == null || isLocked) {
                    return;
                }
                // Mouse relative to the container
                Point parentPoint = SwingUtilities.convertPoint(
                    CodeArea.this,
                    e.getPoint(),
                    getParent()
                );
                
                if (resizing){
                    int newWidth = parentPoint.x - getX();
                    int newHeight = parentPoint.y - getY();
                    
                    newWidth = Math.max(newWidth, 50);
                    newHeight = Math.max(newHeight, 50);
                    
                    resize(newWidth, newHeight);
                }
                
                else{
                    int x = parentPoint.x - mouseOffset.x;
                    int y = parentPoint.y - mouseOffset.y;
                
                    // Prevents going out of bounds
                    if(x >= 0 && y >= 0){
                        pos_x = x;
                        pos_y = y;
                        setLocation(x, y);
                    } 
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseOffset = null;
            }
        };
        return mouseHandler;
    }
}
