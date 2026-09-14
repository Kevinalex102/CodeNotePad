/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author kevin
 */
public class EditorPane extends JLayeredPane {
    
    private JComponent backgroundComponent;

    public EditorPane() {
        setLayout(null);
    }
    
    @Override
    public Component add(Component component){
        super.add(component);
        revalidate();
        repaint();
        return component;
    }
    
    public void removeComponent(JComponent component){
        this.remove(component);
        revalidate();
        repaint();
    }

    public void setBackgroundComponent(JTextArea textArea) {
        this.backgroundComponent = textArea;
        setLayer(textArea, -10);

        revalidate();
        repaint();
    }

    @Override
    public void doLayout() {
        super.doLayout();

        if(backgroundComponent != null)
        {
            backgroundComponent.setBounds(
                0,
                0,
                getWidth(),
                getHeight()
            );
        }
    }
}
