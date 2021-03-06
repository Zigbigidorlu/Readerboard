package com.walgreens.readerboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
class ImageButton extends JButton implements ChangeListener {
    private ImageButton(String text, String icon, boolean isHorizontal) {
        super((text != null) ? "<HTML><CENTER>" + text + "</CENTER></HTML>" : "");

        // Set icon
        updateIcon(icon);

        setVerticalTextPosition((isHorizontal) ? SwingConstants.CENTER : SwingConstants.BOTTOM);
        setHorizontalTextPosition((isHorizontal) ? SwingConstants.RIGHT : SwingConstants.CENTER);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addChangeListener(this);
    }

    private ImageButton(String text, String icon) {
        this(text, icon, false);
    }

    private ImageButton(String text, Color foreground, String icon, boolean isHorizontal) {
        this(text, icon, isHorizontal);
        setForeground(foreground);
    }

    ImageButton(String text, Color foreground, String icon, ActionListener actionListener, String actionCommand) {
        this(text, foreground, icon, false);
        addActionListener(actionListener);
        setActionCommand(actionCommand);
    }

    ImageButton(String text, Color foreground, String icon, ActionListener actionListener,
                String actionCommand, boolean isHorizontal) {
        this(text, foreground, icon, isHorizontal);
        addActionListener(actionListener);
        setActionCommand(actionCommand);
    }

    ImageButton(Color foreground, String icon, ActionListener actionListener, String actionCommand) {
        this(null, foreground, icon, false);
        addActionListener(actionListener);
        setActionCommand(actionCommand);
    }

    void updateIcon(String icon) {
        try {
            URL iconUrl = getClass().getClassLoader().getResource("icons/" + icon);
            if (iconUrl != null) {
                ImageIcon ico = new ImageIcon(ImageIO.read(iconUrl));
                setIcon(ico);
                revalidate();
            } else {
                Debug.log("Icon does not exist: " + icon);
            }
        } catch (IOException e) {
            // Leave blank
        }
    }

    // TODO: Click color handling
    @Override
    public void stateChanged(ChangeEvent e) {
        /*if (getModel().isPressed()) {
        }*/
    }
}
