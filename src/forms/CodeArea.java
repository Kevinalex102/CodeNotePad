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
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author kevin
 */
public class CodeArea extends JTextArea{
    
    private static int idCounter = 0;
    private int id;
    
    private static HashMap<Integer, CodeArea> codeAreas = new HashMap<Integer, CodeArea>();
    
    private Point mouseOffset;
    public static void Teste(){
    
    }
    
    public static CodeArea createArea()
    {
        CodeArea newCodeArea = new CodeArea();
        
        newCodeArea.setBounds(100, 100, 250, 100);
        newCodeArea.setBackground(Color.gray);
        newCodeArea.setForeground(Color.white);
        
        return newCodeArea;
    }
    
    public CodeArea()
    {
        super(10, 10);
        
        this.id = idCounter;
        idCounter++;
        
        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseOffset == null) {
                    return;
                }

                // Mouse relativo ao container
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
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        
        codeAreas.put(this.id, this);
    }
}
