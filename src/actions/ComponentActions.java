/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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
}