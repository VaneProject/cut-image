package data;

import dialog.ColorDialog;
import dialog.PathDialog;

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
    // menu
    String[] menus = {
            LanguageField.FILE.getText(),
            LanguageField.COLOR.getText()
    };
    ItemListener[] menuListener = {
            e -> PathDialog.start(),
            e -> ColorDialog.start()
    };
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

    default int getInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    default int getInteger(JTextField textField) {
        return getInteger(textField.getText());
    }

    default void setNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                try {
                    // 음수 입력 방지
                    if (Integer.parseInt(text) < 0)
                        return;
                    super.replace(fb, offset, length, text, attrs);
//                    String document = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
//                    int value = getInteger(document);
//                    ImageIcon icon = image.get();
//                    int width = icon.getIconWidth();
//                    int height = icon.getIconHeight();
//
//                    if (textField == widthField) {
//                        int x = getInteger(xField);
//                        if (x + value > width) {
//                            text = Integer.toString(width - x);
//                            super.replace(fb, offset, length, text, attrs);
//                            return;
//                        }
//                    } else if (textField == xField) {
//                        int w = getInteger(widthField);
//                        if (w + value > width) {
//                            text = Integer.toString(width - w);
//                            super.replace(fb, offset, length, text, attrs);
//                            return;
//                        }
//                    } else if (textField == heightField) {
//                        int y = getInteger(yField);
//                        if (y + value > height) {
//                            text = Integer.toString(height - y);
//                            super.replace(fb, offset, length, text, attrs);
//                            return;
//                        }
//                    } else if (textField == yField) {
//                        int h = getInteger(heightField);
//                        if (h + value > height) {
//                            text = Integer.toString(height - h);
//                            super.replace(fb, offset, length, text, attrs);
//                            return;
//                        }
//                    }
                } catch (NumberFormatException ignored) {}
            }
        });
    }
}
