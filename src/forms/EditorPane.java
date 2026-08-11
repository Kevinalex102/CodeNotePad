/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forms;

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
    
    public void addComponent(JComponent component){
        this.add(component);
        revalidate();
        repaint();
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
