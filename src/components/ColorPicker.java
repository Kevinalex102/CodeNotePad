/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

import java.awt.Color;
import javax.swing.JColorChooser;

/**
 *
 * @author kevin
 */
public class ColorPicker extends JColorChooser{
    
    public static Color pickAColor(Color oldColor){
        ColorPicker colorPicker = new ColorPicker();
        return(JColorChooser.showDialog(colorPicker, "Escolha a nova cor:", oldColor));
    }
}
