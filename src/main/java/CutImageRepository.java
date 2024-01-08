import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public interface CutImageRepository {
    AtomicReference<ImageIcon> image = new AtomicReference<>();
    String separator = File.separator;
    String[] imageType = {"png", "jpg"};
    String[] selectType = {"png", "jpg"};
    // menu
    String[] menus = {LanguageField.FILE.getText()};
    ItemListener[] menuListener = {e -> PathDialog.start()};
    // view
    JTextField xField = new JTextField("0", 5);
    JTextField yField = new JTextField("0", 5);
    JTextField widthField = new JTextField("1", 5);
    JTextField heightField = new JTextField("1", 5);
    JTextField fileName = new JTextField(10);
    JCheckBox autoFileName = new JCheckBox(LanguageField.AUTO_FILE_NAME.getText());
    JButton openImage = new JButton(LanguageField.LOAD.getText());
    JButton cropImage = new JButton(LanguageField.CROP.getText());
    JButton saveImage = new JButton(LanguageField.SAVE.getText());
    JComboBox<String> imageTypeBox = new JComboBox<>(imageType) {{
        setSelectedIndex(0);
    }};
    JPanel initPanel = new JPanel(new BorderLayout()) {{
        add(new JPanel() {{
            add(new JLabel("X:"));
            add(xField);
            add(new JLabel("Y:"));
            add(yField);
            add(new JLabel(LanguageField.WIDTH.getText() + ":"));
            add(widthField);
            add(new JLabel(LanguageField.HEIGHT.getText() + ":"));
            add(heightField);
        }}, BorderLayout.NORTH);
        add(new JPanel() {{
            add(new JLabel(LanguageField.FILE_NAME.getText() + ":"));
            add(fileName);
            add(imageTypeBox);
            add(autoFileName);
        }}, BorderLayout.CENTER);
        add(new JPanel() {{
            add(openImage);
            add(cropImage);
            add(saveImage);
        }}, BorderLayout.SOUTH);
    }};

    default int getInteger(JTextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    default void setNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                try {
                    int value = Integer.parseInt(text);
                    ImageIcon icon = image.get();
                    // 음수 입력 방지
                    if (value < 0)
                        return;

                    int width = icon.getIconWidth();
                    int height = icon.getIconHeight();
                    if (textField == widthField) {
                        int x = getInteger(xField);
                        if (x + value > width) {
                            String t = Integer.toString(width - x);
                            super.replace(fb, 0, t.length(), t, attrs);
                            return;
                        }
                    } else if (textField == xField) {
                        int w = getInteger(widthField);
                        if (w + value > width) {
                            String t = Integer.toString(width - w);
                            super.replace(fb, 0, t.length(), t, attrs);
                            return;
                        }
                    } else if (textField == heightField) {
                        int y = getInteger(yField);
                        if (y + value > height) {
                            String t = Integer.toString(height - y);
                            super.replace(fb, 0, t.length(), t, attrs);
                            return;
                        }
                    } else if (textField == yField) {
                        int h = getInteger(heightField);
                        if (h + value > height) {
                            String t = Integer.toString(height - h);
                            super.replace(fb, 0, t.length(), t, attrs);
                            return;
                        }
                    }
                    super.replace(fb, offset, length, text, attrs);
                } catch (NumberFormatException ignored) {}
            }
        });
    }

    // 이미지 크기가 넘어갔을때 불러오는 함수
    private void imageSizeWarring() {
        String message = LanguageField.IMAGE_SIZE_WARRING.getText();
    }

    private void widthHeightWarring() {
        String message = LanguageField.WIDTH_HEIGHT_WARRING.getText();
//        new Notifications().show(Notifications.Type.WARNING, message);
    }
}
