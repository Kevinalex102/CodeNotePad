package actions;

/**
 *
 * @author kevin
 */
public interface ComponentActions {
    void copyComponent();
    void pasteComponent();
    void cutComponent();
    void deleteComponent();
    void setLocked(boolean locked);
    void pushUp();
}