import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCrop extends JFrame implements CutImageRepository {
    private final JLabel imageLabel = new JLabel() {{
        // 이미지 중앙 정렬
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }};
    // File Data Field
    private int fileCount = 0;

    private ImageCrop() {
        setNumericFilter(xField);
        setNumericFilter(yField);
        setNumericFilter(widthField);
        setNumericFilter(heightField);
        // select image
        openImage.addActionListener(event -> {
            JFileChooser chooser = new JFileChooser();
            FileFilter fileFilter = new FileNameExtensionFilter(LanguageField.IMAGE_SELECT.getText(), selectType);
            chooser.setFileFilter(fileFilter);
            chooser.addChoosableFileFilter(fileFilter);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    ImageIcon icon = new ImageIcon(ImageIO.read(file));
                    image.set(icon);
                    widthField.setText(Integer.toString(icon.getIconWidth()));
                    heightField.setText(Integer.toString(icon.getIconHeight()));
                    displayImage();
                } catch (IOException e) {
                    dialog(e.getMessage());
                }
            }
        });
        // Auto Image Name Load
        imageTypeBox.addItemListener(e -> {
            if (autoFileName.isSelected()) {
                fileName.setText(autoImageName());
            }
        });
        autoFileName.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                fileName.setEditable(false);
                fileName.setForeground(Color.GRAY);
                fileName.setText(autoImageName());
            } else {
                fileName.setEditable(true);
                fileName.setForeground(null);
            }
        });
        // Panel Setting
        setJMenuBar(new JMenuBar() {{
            for (int i = 0; i < menus.length; i++) {
                JMenu menu = new JMenu(menus[i]);
                menu.addItemListener(menuListener[i]);
                add(menu);
            }
        }});
        autoFileName.setSelected(true);

        cropImage.addActionListener(e -> previewCrop());

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(initPanel, BorderLayout.NORTH);
        container.add(imageLabel, BorderLayout.CENTER);
        setTitle(LanguageField.TITLE.getText());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
    }

    private void displayImage() {
        ImageIcon icon = image.get();
        if (icon == null) {
            String message = LanguageField.NO_IMAGE.getText();
            String title = LanguageField.ERROR.getText();
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
        } else imageLabel.setIcon(icon);
    }

    private void previewCrop() {
        ImageIcon icon = image.get();
        if (icon == null) {
            dialog(LanguageField.NO_IMAGE.getText());
        } else {
            int x = getInteger(xField);
            int y = getInteger(yField);
            int width = getInteger(widthField);
            int height = getInteger(heightField);
            if (width == 0 || height == 0) {
                dialog(LanguageField.WIDTH_HEIGHT_WARRING.getText());
                return;
            }

            Image originalImage = icon.getImage();
            BufferedImage bufferedImage = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB
            ).getSubimage(x, y, width, height);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.drawImage(originalImage, 0, 0, null);
//            for (int y1 = 0; y1 < height; y1++) {
//                for (int x1 = 0; x1 < width; x1++) {
//                    int rgb = bufferedImage.getRGB(x1, y1);
//                    int alpha = (rgb >> 24) & 0xFF;
//                    if (alpha == 0) bufferedImage.setRGB(x1, y1, Color.BLACK.getRGB());
//                }
//            }
            g2.dispose();
            imageLabel.setIcon(new ImageIcon(bufferedImage));
        }
    }

    // 이미지 자동 이름 지정
    private String autoImageName() {
        // 확장자
        String ext = imageType[imageTypeBox.getSelectedIndex()];
        String path = PathDialog.setting.savePath;
        File file;
        if (path.endsWith(separator)) {
            do {
                file = new File(path + "temp" + (fileCount++) + "." + ext);
            } while (file.exists());
        } else {
            do {
                file = new File(path + separator + "temp" + (fileCount++) + "." + ext);
            } while (file.exists());
        }
        fileCount--;
        return file.getName();
    }

    private void dialog(String message) {
        String title = LanguageField.ERROR.getText();
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageCrop::new);
    }
}
