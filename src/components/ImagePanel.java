/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

import actions.ComponentActions;
import editing.ComponentEditor;
import filehandling.ImageFile;
import handlers.DragResizeMouseHandler;
import handlers.IDragResizeMouseHandler;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import menus.ComponentPopupMenu;
import net.coobird.thumbnailator.Thumbnails;

/**
 *
 * @author kevin
 */
public class ImagePanel extends JLabel implements IDragResizeMouseHandler, ComponentActions{

    private final int id;
    private int pos_x, pos_y, width, height;
    private static int idCounter = 0;
    private boolean isLocked = false;
    private final ComponentPopupMenu menu;
    private BufferedImage image;
    private ImageFile imageFile;
    public static HashMap<Integer, ImagePanel> images = new HashMap<>();
    
    public record Instructions(
        String file, 
        int pos_x, 
        int pos_y, 
        int width, 
        int height, 
        boolean locked,
        Integer id
    ){}
    
    private ImagePanel(){
        super();
        this.id = idCounter;
        menu = new ComponentPopupMenu(this);
        idCounter++;
        MouseAdapter mouseHandler = new DragResizeMouseHandler(this, this);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        images.put(this.id, this);
    }
    
    public static ImagePanel createPanel(File file){
        ImagePanel newImagePanel = new ImagePanel();
        newImagePanel.setBounds(100, 100, 300, 300);
        newImagePanel.repaint();
        newImagePanel.setVisible(true);
        newImagePanel.imageFile = new ImageFile(file, newImagePanel.id);
        
        newImagePanel.pos_x = 100;
        newImagePanel.pos_y = 100;
        newImagePanel.width = 300;
        newImagePanel.height = 300;
        
        newImagePanel.readImage();
        newImagePanel.image = newImagePanel.resizeImage(300, 300);
        return newImagePanel;
    }
    
    public static ImagePanel createPanel(Instructions instructions){
        File file = ImageFile.readFile(instructions.file);
        ImagePanel newImagePanel = new ImagePanel();
        int x = instructions.pos_x;
        int y = instructions.pos_y;
        
        newImagePanel.setBounds(x + 10, y + 10, instructions.width, instructions.height);
        newImagePanel.imageFile = new ImageFile(file, newImagePanel.id);
        newImagePanel.pos_x = x + 10;
        newImagePanel.pos_y = y + 10;
        newImagePanel.width = instructions.width;
        newImagePanel.height = instructions.height;
        newImagePanel.setLocked(instructions.locked);
        newImagePanel.repaint();
        newImagePanel.setVisible(true);
        
        newImagePanel.readImage();
        newImagePanel.image = newImagePanel.resizeImage(instructions.width, instructions.height);
        return newImagePanel;
    }
    
    private void readImage(){
        File file = imageFile.getFile();
        try {       
            image = ImageIO.read(file);
        } catch (IOException ex) {
            System.getLogger(ImagePanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        g.drawImage(image, 0, 0, null);
    }
    
    @Override
    public void resize(int newWidth, int newHeight){
        width = newWidth;
        height = newHeight;
        
        this.setBounds(pos_x, pos_y, newWidth, newHeight);
        readImage();
        this.image = resizeImage(newWidth, newHeight);
        
        revalidate();
        repaint();
    }
    
    public BufferedImage resizeImage(int targetWidth, int targetHeight) {
        try {
            return Thumbnails.of(image).forceSize(targetWidth, targetHeight).asBufferedImage();
        } catch (IOException ex) {
            System.getLogger(ImagePanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
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
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void showMenu(int x, int y) {
        menu.show(this, x, y);
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
        images.remove(this.id);
        ComponentEditor.deleteComponent(this);
    }

    @Override
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getPosX() {
        return pos_x;
    }

    public int getPosY() {
        return pos_y;
    }
    
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public String getPath() {
        return imageFile.getFile().toString();
    }
    public int getId(){
        return id;
    }
    public static int getIdCounter(){
        return idCounter;
    }
}
