/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package handlers;

/**
 *
 * @author kevin
 */
public interface IDragResizeMouseHandler {
    
    void resize(int width, int height);
    void moveTo(int x, int y);
    boolean isLocked();
    void showMenu(int x, int y);
    default void resizeFinished() {}
}
