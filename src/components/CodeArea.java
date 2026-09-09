/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

import filehandling.CodeFile;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import handlers.*;
import actions.*;
import menus.*;
import java.util.HashMap;
import javax.swing.*;

/**
 *
 * @author kevin
 */
public class CodeArea extends JTextArea implements IDragResizeMouseHandler, ComponentActions, ExportActions{
    
    private static int idCounter = 0;
    private int id;
    private CodeFile textFile = null;
    private boolean isLocked = false;
    
    private int pos_x, pos_y, height, width;
    
    private static HashMap<Integer, CodeArea> codeAreas = new HashMap<Integer, CodeArea>();
    
    private final ComponentPopupMenu menu;

    private CodeArea(){
        super(10, 10);
        
        this.id = idCounter;
        idCounter++;
        menu = new ComponentPopupMenu(this);
        initializeMenu();
        MouseAdapter mouseHandler = new DragResizeMouseHandler(this, this);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        codeAreas.put(this.id, this);
    }
    
    private void initializeMenu(){
        JMenuItem changeBackground = new JMenuItem("Mudar cor do fundo");
        JMenuItem changeForeground = new JMenuItem("Mudar cor do texto");
        changeBackground.addActionListener(e -> changeBackground());
        changeForeground.addActionListener(e -> changeForeground());
        
        menu.addSeparator();
        TextMenuItems.addTo(menu, this);
        menu.add(changeBackground);
        menu.add(changeForeground);
        menu.addSeparator();
        menu.add(new ExportMenu(this).exportMenu);
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
    
    @Override
    public void selectComponent() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void copyComponent() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void pasteComponent() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void cutComponent() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void deleteComponent() {
        codeAreas.remove(this.id);
        EditorPane parent = (EditorPane) this.getParent();
        parent.removeComponent(this);
    }
    @Override
    public void setLocked(boolean locked) {
        isLocked = locked;
    }
    
    public void changeBackground() {
        this.setBackground(ColorPicker.pickAColor(this.getBackground()));
    }

    private void changeForeground() {
        this.setForeground(ColorPicker.pickAColor(this.getForeground()));
    }

    @Override
    public void export(String type) {
        
        if (textFile == null){
            textFile = new CodeFile(this.getText(), this.id, type);
        }
        else{
            textFile.write(this.getText());
        }
        textFile.openWith();
    }
    
    @Override
    public boolean isLocked() {
        return isLocked;
    }
    
    @Override
    public void resize(int newWidth, int newHeight){
        width = newWidth;
        height = newHeight;
        
        this.setBounds(pos_x, pos_y, newWidth, newHeight);
        
        revalidate();
        repaint();
    }
    
    @Override
    public void moveTo(int x, int y) {
        pos_x = x;
        pos_y = y;
        setLocation(x, y);
    }
    
    @Override
    public void showMenu(int x, int y) {
        menu.show(this, x, y);
    }
}