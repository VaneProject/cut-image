import data.CutImageRepository;
import data.LanguageField;
import data.SettingFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageCrop extends JFrame implements CutImageRepository, ActionListener, ItemListener {
    private final static String filterTitle = LanguageField.IMAGE_SELECT.getText();
    private final FileFilter imageFilter = new FileNameExtensionFilter(filterTitle, ImageIO.getReaderFileSuffixes());
    private final JFileChooser chooser = new JFileChooser() {{
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
        setFileFilter(imageFilter);
    }};
    private final JLabel imageLabel = new JLabel() {{
        // 이미지 중앙 정렬
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }};
    // File Data Field
    private int fileCount = 0;

    private ImageCrop() {
        KeyAdapter adapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int x = getInteger(xField);
                int y = getInteger(yField);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        yField.setText(Integer.toString(y - 1));
                        previewCrop();
                    }
                    case KeyEvent.VK_DOWN -> {
                        yField.setText(Integer.toString(y + 1));
                        previewCrop();
                    }
                    case KeyEvent.VK_RIGHT -> {
                        xField.setText(Integer.toString(x + 1));
                        previewCrop();
                    }
                    case KeyEvent.VK_LEFT -> {
                        xField.setText(Integer.toString(x - 1));
                        previewCrop();
                    }
                }
            }
        };

        openImage.addKeyListener(adapter);
        cropImage.addKeyListener(adapter);
        saveImage.addKeyListener(adapter);
        imageTypeBox.addKeyListener(adapter);

        setNumericFilter(xField);
        setNumericFilter(yField);
        setNumericFilter(widthField);
        setNumericFilter(heightField);
        selectFile();
        // select image
        openImage.addActionListener(this);
        saveImage.addActionListener(this);
        cropImage.addActionListener(this);
        // Auto Image Name Load
        imageTypeBox.addItemListener(this);
        autoFileName.addItemListener(this);
        autoFileName.setSelected(true);
        // Panel Setting
        setJMenuBar(new JMenuBar() {{
            for (int i = 0; i < menus.length; i++) {
                JMenu menu = new JMenu(menus[i]);
                menu.addItemListener(menuListener[i]);
                add(menu);
            }
        }});

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(initPanel, BorderLayout.NORTH);
        container.add(imageLabel, BorderLayout.CENTER);
        setTitle(LanguageField.TITLE.getText());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream("icon.png")) {
            if (is != null) setIconImage(new ImageIcon(is.readAllBytes()).getImage());
        } catch (IOException e) {
            dialog(e.getMessage());
        }
    }

    // 이미지 선택
    private void selectFile() {
        int type = chooser.showSaveDialog(this);
        if (type == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                ImageIcon icon = new ImageIcon(ImageIO.read(file));
                image.set(icon);
                xField.setText("0");
                yField.setText("0");
                widthField.setText(Integer.toString(icon.getIconWidth()));
                heightField.setText(Integer.toString(icon.getIconHeight()));
                previewCrop(icon);
            } catch (IOException e) {
                dialog(e.getMessage());
            }
        } else if (image.get() == null) {
            dialog(LanguageField.NO_IMAGE.getText());
            selectFile();
        }
    }

    private void previewCrop(ImageIcon icon) {
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

            BufferedImage croppedImage = cropImage(originalImage, x, y, width, height);
            if (SettingFile.setting.useColor)
                croppedImage = convertTransparentToColor(croppedImage);
            imageLabel.setIcon(new ImageIcon(croppedImage));
        }
    }

    // image
    private BufferedImage cropImage(Image image, int x, int y, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage.getSubimage(x, y, width, height);
    }

    private BufferedImage convertTransparentToColor(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = convertedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        Color fromColor = SettingFile.setting.fromColor;
        Color toColor = SettingFile.setting.toColor;
        int fromRGB = fromColor.getRGB();
        int fromAlpha = fromColor.getAlpha();

        int toRGB = toColor.getRGB();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                if ((fromAlpha == 0 && alpha == 0) || (rgb == fromRGB && fromAlpha == alpha)) {
                    convertedImage.setRGB(x, y, toRGB);
                }
            }
        }
        g2d.dispose();
        return convertedImage;
    }

    private void previewCrop() {
        previewCrop(image.get());
    }

    // 이미지 자동 이름 지정
    private String autoImageName() {
        // 확장자
        String ext = imageType[imageTypeBox.getSelectedIndex()];
        String path = SettingFile.setting.savePath;
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

    private void saveFile() {
        int x = getInteger(xField);
        int y = getInteger(yField);
        int width = getInteger(widthField);
        int height = getInteger(heightField);
        // image load
        Image originalImage = image.get().getImage();
        BufferedImage croppedImage = cropImage(originalImage, x, y, width, height);
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resultImage.createGraphics();
        g.setColor(new Color(0, 0, 0, 0)); // 투명 배경 설정
        g.fillRect(0, 0, width, height);
        g.drawImage(croppedImage, 0, 0, null);
        g.dispose();
        // file load
        String savePath = SettingFile.setting.savePath;
        String fileName = this.fileName.getText();
        File file = savePath.endsWith(separator)
                ? new File(savePath + fileName)
                : new File(savePath + separator + fileName);
        String ext = imageType[imageTypeBox.getSelectedIndex()];
        try {
            ImageIO.write(resultImage, ext, file);
            if (autoFileName.isSelected())
                this.fileName.setText(autoImageName());
        } catch (IOException e) {
            e.printStackTrace();
            dialog(e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageCrop::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == saveImage)
            saveFile();
        else if (source == openImage)
            selectFile();
        else if (source == cropImage)
            previewCrop();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (source == imageTypeBox) {
            if (autoFileName.isSelected())
                fileName.setText(autoImageName());
        } else if (source == autoFileName) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                fileName.setEditable(false);
                fileName.setForeground(Color.GRAY);
                fileName.setText(autoImageName());
            } else {
                fileName.setEditable(true);
                fileName.setForeground(null);
            }
        }
    }
}
