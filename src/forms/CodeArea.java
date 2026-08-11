/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forms;

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
    
    private static HashMap<Integer, CodeArea> codeAreas = new HashMap<Integer, CodeArea>();
    
    private Point mouseOffset;
    private final JPopupMenu menu = new JPopupMenu();

    public CodeArea(){
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
        newCodeArea.setBackground(Color.gray);
        newCodeArea.setForeground(Color.white);
        
        return newCodeArea;
    }
    
    private void initializeMenu(CodeArea codeArea){
        JMenuItem select = new JMenuItem("Selecionar tudo");
        JMenuItem cut = new JMenuItem("Recortar texto");
        JMenuItem copy = new JMenuItem("Copiar texto");
        JMenuItem paste = new JMenuItem("Colar texto");
        JMenuItem changeBackground = new JMenuItem("Mudar cor do fundo");
        JMenuItem changeForeground = new JMenuItem("Mudar cor do texto");
        JMenuItem delete = new JMenuItem("Excluir janela");
        JMenuItem export = new JMenuItem("Exportar para...");
        
        select.addActionListener(e -> codeArea.selectAll());
        cut.addActionListener(e -> codeArea.cut());
        copy.addActionListener(e -> codeArea.copy());
        paste.addActionListener(e -> codeArea.paste());
        //changeBackground.addActionListener(e -> ChangeBackground());
        //changeForeground.addActionListener(e -> ChangeForeground());
        delete.addActionListener(e -> deleteArea());
        //export.addActionListener(e -> Export());
        
        menu.add(select);
        menu.add(cut);
        menu.add(copy);
        menu.add(paste);
        menu.addSeparator();
        menu.add(changeBackground);
        menu.add(changeForeground);
        menu.addSeparator();
        menu.add(delete);
        menu.add(export);
    }
    
    private MouseAdapter addMouseHandler()
    {
        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) { 
                    mouseOffset = e.getPoint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    menu.show(CodeArea.this, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseOffset == null) {
                    return;
                }

                // Mouse relative to the container
                Point parentPoint = SwingUtilities.convertPoint(
                    CodeArea.this,
                    e.getPoint(),
                    getParent()
                );

                int x = parentPoint.x - mouseOffset.x;
                int y = parentPoint.y - mouseOffset.y;

                setLocation(x, y);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseOffset = null;
            }
        };
        return mouseHandler;
    }    

    private void deleteArea() {
        codeAreas.remove(this.id);
        EditorPane parent = (EditorPane) this.getParent();
        parent.removeComponent(this);
    }
}
