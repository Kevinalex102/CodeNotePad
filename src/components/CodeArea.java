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
import editing.ComponentEditor;
import menus.*;
import javax.swing.*;

/**
 *
 * @author kevin
 */
public class CodeArea extends JTextArea implements IDragResizeMouseHandler, ComponentActions, ExportActions{

    private final int id;
    private boolean isLocked = false;
    private final ComponentPopupMenu menu;
    private CodeFile textFile = null;
    
    public record Instructions(
        String text, 
        int pos_x, 
        int pos_y, 
        int width, 
        int height, 
        int layer,
        Color bgColor, 
        Color fgColor, 
        boolean locked,
        Integer id
        ){}

    private CodeArea(){
        super(10, 10);
        
        this.id = EditorPane.newId();
        this.menu = new ComponentPopupMenu(this);
        initialize();
    }
    private CodeArea(int id){
        super(10, 10);
        
        this.id = id;
        this.menu = new ComponentPopupMenu(this);
        initialize();
    }
    
    private void initialize(){
        initializeMenu();
        MouseAdapter mouseHandler = new DragResizeMouseHandler(this, this);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        EditorPane.codeAreas.put(this.id, this);
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
        newCodeArea.setBackground(Color.gray);
        newCodeArea.setForeground(Color.white);
        
        return newCodeArea;
    }
    
    public static CodeArea createArea(Instructions instructions){
        CodeArea newCodeArea;
        if (instructions.id == null){
            newCodeArea = new CodeArea();
        }
        else {
            newCodeArea = new CodeArea(instructions.id);
        }
        
        newCodeArea.setBounds(instructions.pos_x, instructions.pos_y , instructions.width, instructions.height);
        newCodeArea.setBackground(instructions.bgColor);
        newCodeArea.setForeground(instructions.fgColor);
        newCodeArea.setLocked(instructions.locked);
        newCodeArea.setText(instructions.text);
        
        return newCodeArea;
    }
    
    @Override
    public void copyComponent() {
        ComponentEditor.copyComponent(this);
    }

    @Override
    public void pasteComponent() {
        ComponentEditor.pasteComponent(this.getParent());
    }

    @Override
    public void cutComponent() {
        ComponentEditor.cutComponent(this);
    }
    
    @Override
    public void deleteComponent() {
        EditorPane.codeAreas.remove(this.id);
        ComponentEditor.deleteComponent(this);
    }
    
    @Override
    public void setLocked(boolean locked) {
        isLocked = locked;
    }
    
    @Override
    public void pushUp() {
        ComponentEditor.pushup(this);
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
    public void resize(int newWidth, int newHeight){
        this.setBounds(getX(), getY(), newWidth, newHeight);
        revalidate();
        repaint();
    }
    
    @Override
    public void moveTo(int x, int y) {
        setLocation(x, y);
        revalidate();
        repaint();
    }
    
    @Override
    public void showMenu(int x, int y) {
        menu.show(this, x, y);
    }
    
    public void changeBackground() {
        this.setBackground(ColorPicker.pickAColor(this.getBackground()));
    }

    private void changeForeground() {
        this.setForeground(ColorPicker.pickAColor(this.getForeground()));
    }
    
    @Override
    public boolean isLocked() {
        return isLocked;
    }
    public int getId(){
        return id;
    }
    public int getLayer(){
        EditorPane parent = (EditorPane) this.getParent();
        return parent.getPosition(this);
    }
}