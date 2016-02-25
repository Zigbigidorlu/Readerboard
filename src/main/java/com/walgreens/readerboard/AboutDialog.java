package com.walgreens.readerboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    AboutDialog() {
        setTitle("About " + Main.name);
        setResizable(false);
        setModal(true);
        setIconImages(UserInterface.icons);
        Dimension dimension = new Dimension(250,325);
        setPreferredSize(dimension);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UserInterface.background);
        content.setOpaque(true);
        add(content);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(UserInterface.wagRed);
        top.setOpaque(true);
        content.add(top,BorderLayout.NORTH);

        JLabel appName = new JLabel(Main.name);
        appName.setFont(new Font("Arial",Font.BOLD, 18));
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);
        appName.setForeground(Color.WHITE);
        appName.setBorder(new EmptyBorder(15,0,5,0));
        top.add(appName);

        JLabel appVersion = new JLabel("Version " + Main.version);
        appVersion.setFont(new Font("Arial",Font.BOLD, 12));
        appVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        appVersion.setForeground(Color.WHITE);
        appVersion.setBorder(new EmptyBorder(0,0,10,0));
        top.add(appVersion);

        JPanel info = new JPanel(new BorderLayout());
        content.add(info,BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
