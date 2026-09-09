/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handlers;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author kevin
 */

public class DragResizeMouseHandler extends MouseAdapter {
    
    private final JComponent component;
    private final IDragResizeMouseHandler target;
    private boolean resizing = false;
    private Point mouseOffset;
    
    public DragResizeMouseHandler(JComponent component, IDragResizeMouseHandler target) {
        this.component = component;
        this.target = target;
    }    
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseOffset = e.getPoint();
            resizing = 
                mouseOffset.x > component.getWidth() - 10 && 
                mouseOffset.y > component.getHeight() - 10;
        } else if(SwingUtilities.isRightMouseButton(e)){
            target.showMenu(e.getX(), e.getY());
        }
    }
    
     @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseOffset == null || target.isLocked()) {
            return;
        }

        Point parentPoint = SwingUtilities.convertPoint(
            component,
            e.getPoint(),
            component.getParent()
        );

        if (resizing) {
            int newWidth = parentPoint.x - component.getX();
            int newHeight = parentPoint.y - component.getY();

            target.resize(
                Math.max(newWidth, 50),
                Math.max(newHeight, 50)
            );
        } else {
            int x = parentPoint.x - mouseOffset.x;
            int y = parentPoint.y - mouseOffset.y;

            if (x >= 0 && y >= 0) {
                target.moveTo(x, y);
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        mouseOffset = null;
    }
}
