package com.walgreens.readerboard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 2/24/2016
 */
public class AboutDialog extends JDialog {
    AboutDialog(List<Image> icons) {
        setTitle("About Readerboard Assistant");
        setResizable(false);
        setModal(true);
        setIconImages(icons);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
