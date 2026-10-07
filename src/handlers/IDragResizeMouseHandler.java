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
