package com.walgreens.readerboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
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
public class ImageButton extends JButton {
    public ImageButton(String text, String icon) {
        super("<HTML><CENTER>" + text + "</CENTER></HTML>");

        try {
            URL iconUrl = getClass().getClassLoader().getResource("icons/" + icon);
            if(iconUrl != null) {
                ImageIcon ico = new ImageIcon(ImageIO.read(iconUrl));
                setIcon(ico);
            }
            else {
                Debug.log("Icon does not exist: " + icon);
            }
        }
        catch (IOException e) {
            // Leave blank
        }

        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(new EmptyBorder(8,8,8,8));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public ImageButton(String text, Color foreground, String icon) {
        this(text, icon);
        setForeground(foreground);
    }

    public ImageButton(String text, Color foreground, String icon, ActionListener actionListener, String actionCommand) {
        this(text, foreground, icon);
        addActionListener(actionListener);
        setActionCommand(actionCommand);
    }
}
