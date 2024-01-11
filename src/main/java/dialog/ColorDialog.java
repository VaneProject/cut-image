package dialog;

import data.LanguageField;
import data.SettingFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class ColorDialog extends JDialog {
    private final JButton fromColorButton = new JButton(LanguageField.COLOR_FROM.getText());
    private final JButton toColorButton = new JButton(LanguageField.COLOR_TO.getText());
    private final JButton save = new JButton(LanguageField.SAVE.getText());
    private final static ColorDialog dialog = new ColorDialog();
    // temp color
    private Color toColor = SettingFile.setting.toColor;
    private Color fromColor = SettingFile.setting.fromColor;
    private boolean useColor = SettingFile.setting.useColor;

    private ColorDialog() {
        JCheckBox useColor = new JCheckBox(LanguageField.USE_COLOR.getText());
        useColor.setSelected(this.useColor);

        setTitle(LanguageField.COLOR_SELECT.getText());
        setSize(300, 200);
        fromColorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, LanguageField.COLOR_FROM.getText(), fromColor);
            if (color != null) {
                fromColor = color;
                changeColor();
            }
        });
        toColorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, LanguageField.COLOR_TO.getText(), toColor);
            if (color != null) {
                toColor = color;
                changeColor();
            }
        });
        save.addActionListener(e -> finish());
        useColor.addItemListener(e -> {
            this.useColor = e.getStateChange() == ItemEvent.SELECTED;
            changeEnable();
        });

        changeColor();
        changeEnable();
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(useColor);
        getContentPane().add(fromColorButton);
        getContentPane().add(toColorButton);
        getContentPane().add(save);
    }

    private void changeColor() {
        fromColorButton.setBackground(fromColor);
        toColorButton.setBackground(toColor);
        fromColorButton.setOpaque(true);
        toColorButton.setOpaque(true);
    }

    private void changeEnable() {
        boolean check = useColor;
        fromColorButton.setEnabled(check);
        toColorButton.setEnabled(check);
    }

    public static void start() {
        dialog.setVisible(true);
    }

    public static void finish() {
        SettingFile.setting.useColor = dialog.useColor;
        SettingFile.setting.toColor = dialog.toColor;
        SettingFile.setting.fromColor = dialog.fromColor;
        dialog.setVisible(false);
        SettingFile.save();
    }
}
