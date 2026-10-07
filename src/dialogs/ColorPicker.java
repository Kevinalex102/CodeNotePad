package dialogs;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author kevin
 */
public final class ColorPicker {
    public static Color pickAColor() {
        JColorChooser chooser = new JColorChooser();

        JComponent colorPanel =
                (JComponent) chooser.getChooserPanels()[0].getComponent(0);

        JDialog dialog = new JDialog(
                (Frame) null,
                "Escolha a nova cor:",
                true
        );

        dialog.setLayout(new BorderLayout());
        dialog.add(colorPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();

        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Cancelar");

        final Color[] selectedColor = {null};

        btnOk.addActionListener(e -> {
            selectedColor[0] = chooser.getColor();
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(btnOk);
        buttonsPanel.add(btnCancel);

        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return selectedColor[0];
    }
}