package data;

import java.awt.*;
import java.io.*;

public class SettingFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1391911266022262825L;

    public String savePath = "." + CutImageRepository.separator;
    public boolean useColor = false;
    public Color fromColor = new Color(0, 0, 0, 0);
    public Color toColor = Color.BLACK;

    private final static String SETTING = "setting.vane";
    public static SettingFile setting;
    static {
        try (FileInputStream fis = new FileInputStream(SETTING);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            setting = (SettingFile) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            setting = new SettingFile();
        }
    }

    public static void save() {
        try (FileOutputStream fos = new FileOutputStream(SETTING);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(setting);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
