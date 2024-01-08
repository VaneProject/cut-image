import javax.swing.*;
import java.awt.*;
import java.io.*;

public class PathDialog extends JDialog {
    private final JTextField savePath = new JTextField(15);

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

    private final static String SETTING = "setting.vane";
    private final static PathDialog dialog = new PathDialog();
    public static SettingFile setting;
    static {
        try (FileInputStream fis = new FileInputStream(SETTING);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            setting = (SettingFile) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            setting = new SettingFile();
        }
        dialog.savePath.setText(setting.savePath);
    }
    public static void start() {
        dialog.setVisible(true);
    }

    public static void finish() {
        setting.savePath = dialog.savePath.getText();
        dialog.setVisible(false);
        try (FileOutputStream fos = new FileOutputStream(SETTING);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(setting);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
