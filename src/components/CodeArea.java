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
import java.util.HashMap;
import javax.swing.*;

/**
 *
 * @author kevin
 */
public class CodeArea extends JTextArea implements IDragResizeMouseHandler, ComponentActions, ExportActions{

    private final int id;
    private int pos_x, pos_y, width, height;
    private Color bgColor, fgColor;
    private boolean isLocked = false;
    private final ComponentPopupMenu menu;
    private CodeFile textFile = null;
    private static int idCounter = 0;
    private static HashMap<Integer, CodeArea> codeAreas = new HashMap<>();
    
    public record Instructions(
        String text, 
        int pos_x, 
        int pos_y, 
        int width, 
        int height, 
        Color bgColor, 
        Color fgColor, 
        boolean locked
    ){}

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
        newCodeArea.pos_x = 100;
        newCodeArea.pos_y = 100;
        newCodeArea.width = 250;
        newCodeArea.height = 100;
        newCodeArea.setBackground(Color.gray);
        newCodeArea.setForeground(Color.white);
        
        return newCodeArea;
    }
    
    public static CodeArea createArea(Instructions instructions){
        CodeArea newCodeArea = new CodeArea();
        
        int x = instructions.pos_x;
        int y = instructions.pos_y;
        
        newCodeArea.setBounds(x + 10, y + 10, instructions.width, instructions.height);
        newCodeArea.pos_x = x + 10;
        newCodeArea.pos_y = y + 10;
        newCodeArea.width = instructions.width;
        newCodeArea.height = instructions.height;
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
        ComponentEditor.copyComponent(this);
        deleteComponent();
    }
    
    @Override
    public void deleteComponent() {
        codeAreas.remove(this.id);
        ComponentEditor.deleteComponent(this);
    }
    
    @Override
    public void setLocked(boolean locked) {
        isLocked = locked;
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
    public void setBackground(Color color){
        super.setBackground(color);
        bgColor = color;
    }
    
    @Override
    public void setForeground(Color color){
        super.setForeground(color);
        fgColor = color;
    }
    
    public int getPosX(){
        return pos_x;
    }
    public int getPosY(){
        return pos_y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Color getBg(){
        return bgColor;
    }
    public Color getFg(){
        return fgColor;
    }
    @Override
    public boolean isLocked() {
        return isLocked;
    }
}