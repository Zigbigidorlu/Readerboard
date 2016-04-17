package com.walgreens.readerboard;

import javax.swing.*;
import java.awt.*;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 2/27/2016
 */
class JLabel extends javax.swing.JLabel {
    JLabel(String str) {
        super(str);
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setSize(getPreferredSize());
    }

    JLabel(ImageIcon imageIcon) {
        super(imageIcon);
    }

    JLabel(String str, int pos) {
        this(str);
        setHorizontalAlignment(pos);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        super.paint(g);
    }
}
