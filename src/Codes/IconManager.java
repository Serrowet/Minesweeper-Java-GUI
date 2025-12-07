package Codes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class IconManager {
    private ImageIcon mineIcon;
    private ImageIcon flagIcon;
    private int defaultIconSize;

    public IconManager(int defaultIconSize) {
        this.defaultIconSize = defaultIconSize;
        loadIcons();
    }

    private void loadIcons() {
        mineIcon = loadAndScaleIcon("/png/Mine.png", defaultIconSize);
        flagIcon = loadAndScaleIcon("/png/Flag.png", defaultIconSize);

        if (mineIcon == null) {
            System.out.println("Mine.png bulunamadı, emoji kullanılıyor");
            mineIcon = createTextIcon("💣", defaultIconSize);
        }
        if (flagIcon == null) {
            System.out.println("Flag.png bulunamadı, emoji kullanılıyor");
            flagIcon = createTextIcon("🚩", defaultIconSize);
        }
    }

    private ImageIcon loadAndScaleIcon(String path, int size) {
        try {
            URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        size, size, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                System.err.println("Dosya bulunamadı: " + path);
            }
        } catch (Exception e) {
            System.err.println("Icon yüklenemedi: " + path);
            e.printStackTrace();
        }
        return null;
    }

    private ImageIcon createTextIcon(String text, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, size, size);

        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size * 3 / 4));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(text)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();

        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x, y);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public ImageIcon getMineIcon() { return mineIcon; }
    public ImageIcon getFlagIcon() { return flagIcon; }
}