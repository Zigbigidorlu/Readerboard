package com.walgreens.readerboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 2/24/2016
 */
public class AboutDialog extends JDialog implements MouseListener {
    AboutDialog() {
        setTitle("About " + Main.name);
        setResizable(false);
        setModal(true);
        setIconImages(UserInterface.icons);
        Dimension dimension = new Dimension(350,275);
        setPreferredSize(dimension);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UserInterface.background);
        content.setOpaque(true);
        add(content);

        JPanel top = new JPanel();
        top.setBackground(UserInterface.wagRed);
        top.setOpaque(true);
        content.add(top,BorderLayout.NORTH);

        JPanel container = new JPanel(new BorderLayout());
        content.add(container,BorderLayout.CENTER);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        container.add(info, BorderLayout.CENTER);

        JPanel icon = new JPanel("icon_64.png");
        icon.setFocusable(true);
        icon.addMouseListener(this);
        container.add(icon, BorderLayout.WEST);

        JLabel appName = new JLabel(Main.name);
        appName.setFont(new Font("Segoe UI",Font.BOLD, 18));
        appName.setBorder(new EmptyBorder(10,0,5,0));
        info.add(appName);

        JLabel appVersion = new JLabel("Version " + Main.version);
        appVersion.setFont(new Font("Segoe UI",Font.PLAIN, 14));
        appVersion.setBorder(new EmptyBorder(0,0,10,10));
        info.add(appVersion);

        JLabel developed = new JLabel("<HTML>Developed for the exclusive usage of Walgreens store #03342.</HTML>");
        developed.setBorder(new EmptyBorder(0,0,10,10));
        info.add(developed);

        JLabel distribution = new JLabel(   "<HTML>Do not distribute, copy, or otherwise make available " +
                                            "to any other devices.</HTML>");
        distribution.setBorder(new EmptyBorder(0,0,10,10));
        info.add(distribution);

        JLabel copyright = new JLabel(  "<HTML>The Walgreens Logo and Corner W Logo are Copyright 2016 " +
                                        "Walgreen Co. 200 Wilmot Rd. Deerfield IL.</HTML>");
        copyright.setBorder(new EmptyBorder(0,0,10,10));
        info.add(copyright);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 7) {
            Debug.console();
            dispose();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
