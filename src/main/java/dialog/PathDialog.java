package dialog;

import data.LanguageField;
import data.SettingFile;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class PathDialog extends JDialog {
    private final JTextField savePath = new JTextField(15);
    private final static PathDialog dialog = new PathDialog();
    static {
        dialog.savePath.setText(SettingFile.setting.savePath);
    }

    private PathDialog() {
        final JButton finish = new JButton(LanguageField.SAVE.getText());
        final JButton selectPath = new JButton(LanguageField.PATH.getText());
        finish.addActionListener(e -> finish());
        selectPath.addActionListener(e -> selectPath());

        setLayout(new FlowLayout());
        setSize(300, 100);
        add(new JLabel(LanguageField.PATH.getText()));
        add(savePath);
        add(selectPath);
        add(finish);
    }

    private void selectPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(this, null);
        savePath.setText(chooser.getSelectedFile().getAbsolutePath());
    }

    public static void start() {
        dialog.setVisible(true);
    }

    public static void finish() {
        SettingFile.setting.savePath = dialog.savePath.getText();
        dialog.setVisible(false);
        SettingFile.save();
    }
}
