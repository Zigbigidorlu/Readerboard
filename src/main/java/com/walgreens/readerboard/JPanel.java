package com.walgreens.readerboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 2/23/2016
 */
public class JPanel extends javax.swing.JPanel {
    JPanel() {
        super();
        setOpaque(false);
    }

    JPanel(LayoutManager layoutManager) {
        super(layoutManager);
        setOpaque(false);
    }

    JPanel(String icon) {
        this();

        try {
            URL iconUrl = getClass().getClassLoader().getResource("icons/" + icon);
            ImageIcon ico = new ImageIcon(ImageIO.read(iconUrl));
            JLabel image = new JLabel(ico);
            add(image);
        }
        catch (IOException e) {
            // Leave blank
        }
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(new EmptyBorder(8,8,8,8));
    }
}
