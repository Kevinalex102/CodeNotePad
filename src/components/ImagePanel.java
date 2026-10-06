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
    private boolean isLocked = false;
    private ComponentPopupMenu menu;
    private BufferedImage image;
    private ImageFile imageFile;
    
    public record Instructions(
        String file, 
        int pos_x, 
        int pos_y, 
        int width, 
        int height, 
        int layer,
        boolean locked,
        Integer id
    ){}
    
    private ImagePanel(){
        super();
        this.id = EditorPane.newId();
        menu = new ComponentPopupMenu(this);
        initialize();
    }
    
    private ImagePanel(int id){
        super();
        this.id = id;
        menu = new ComponentPopupMenu(this);
        initialize();
    }
    
    private void initialize(){
        MouseAdapter mouseHandler = new DragResizeMouseHandler(this, this);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        EditorPane.images.put(this.id, this);
    }
    
    public static ImagePanel createPanel(File file){
        ImagePanel newImagePanel = new ImagePanel();
        newImagePanel.setBounds(100, 100, 300, 300);
        newImagePanel.repaint();
        newImagePanel.setVisible(true);
        newImagePanel.imageFile = new ImageFile(file, newImagePanel.id);
        
        newImagePanel.readImage();
        newImagePanel.image = newImagePanel.resizeImage(300, 300);
        return newImagePanel;
    }
    
    public static ImagePanel createPanel(Instructions instructions){
        File file = new File(instructions.file);
        
        ImagePanel newImagePanel;
        if(instructions.id == null){
            newImagePanel = new ImagePanel();
        } else{
            newImagePanel = new ImagePanel(instructions.id);
        }
        
        newImagePanel.setBounds(instructions.pos_x, instructions.pos_y, instructions.width, instructions.height);
        newImagePanel.imageFile = new ImageFile(file, newImagePanel.id);
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
            throw new IllegalArgumentException("Could not read image (Check if image is missing!)", ex);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        if (image == null) {
            return;
        }

        g.drawImage(
            image,
            0,
            0,
            getWidth(),
            getHeight(),
            null
        );
    }
    
    @Override
    public void resize(int newWidth, int newHeight){
        this.setBounds(getX(), getY(), newWidth, newHeight);
        
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
    public void resizeFinished(){
        readImage();
        image = resizeImage(getWidth(), getHeight());
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
        ComponentEditor.cutComponent(this);
    }
    
    @Override
    public void deleteComponent() {
        EditorPane.images.remove(this.id);
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
    
    public void updateTheme() {
        SwingUtilities.updateComponentTreeUI(menu);
    }
    
    public void resetMenu(){
        menu = new ComponentPopupMenu(this);
    }

    public String getPath() {
        return imageFile.getFile().toString();
    }
    public int getId(){
        return id;
    }
    public int getLayer(){
        EditorPane parent = (EditorPane) this.getParent();
        return parent.getPosition(this);
    }
    public ImageFile getImageFile(){
        return imageFile;
    }
}
