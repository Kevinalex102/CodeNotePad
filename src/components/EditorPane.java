/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.*;

/**
 *
 * @author kevin
 */
public class EditorPane extends JLayeredPane {
    
    public static HashMap<Integer, CodeArea> codeAreas = new HashMap<>();
    public static HashMap<Integer, ImagePanel> images = new HashMap<>();
    private static int IdCounter = 0;
    private JTextArea backgroundComponent;

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
    @Override
    public Component add(Component component, int position){
        super.add(component);
        setPosition(component, position);
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
    
    @Override
    public void removeAll(){
        resetHashMaps();
        
        super.removeAll();
        
        add(backgroundComponent);
        setBackgroundComponent(backgroundComponent);
        doLayout();
    }
    
    public static int getIdCounter(){
        return IdCounter;
    }
    
    public static void setIdCounter(int id){
        IdCounter = id;
    }
    
    public static int newId(){
        int id = IdCounter;
        IdCounter++;
        return id;
    }
    
    public static void resetHashMaps(){
        codeAreas.clear();
        images.clear();
    }
}
