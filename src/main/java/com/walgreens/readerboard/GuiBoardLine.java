package com.walgreens.readerboard;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 3/5/2016
 */
public class GuiBoardLine extends JPanel {
    Font font;
    List<Character> message;
    GridBagConstraints constraints;

    GuiBoardLine(String contents) {
        super();
        setBackground(new Color(194,204,203));
        setOpaque(true);
        setFocusable(true);

        // Set width
        int width = 30 * Main.maxLength;
        setPreferredSize(new Dimension(width,40));

        // Set layout
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();

        // Load font
        try {
            URL fontPath = getClass().getClassLoader().getResource("Evogria.ttf");
            File fontFile = new File(fontPath.toURI());
            Font coreFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            font = coreFont.deriveFont(26f);
        }
        catch(FontFormatException | IOException | URISyntaxException e) {
            new CrashHandler(e);
        }

        // Initialize message list
        message = new ArrayList<>();

        // Load characters onto board
        char[] letters = contents.toCharArray();
        for(char letter : letters) {
            addChar(letter);
        }
    }

    boolean addChar(char character) {
        if(message.size() < Main.maxLength) {
            message.add(character);
            CharBlock block = new CharBlock(character);
            add(block);
            revalidate();
            repaint();
            return true;
        }
        return false;
    }

    boolean delChar() {
        if(message.size() > 0) {
            message.remove(message.size() - 1);
            remove(getComponentCount() - 1);
            revalidate();
            repaint();
            return true;
        }
        return false;
    }

    void clear() {
        message.clear();
        removeAll();
        revalidate();
        repaint();
    }

    private class CharBlock extends JPanel {
        Color color;
        CharBlock(Character character) {
            String c = String.valueOf(character);
            color = (StringUtils.isNumeric(c)) ? Color.RED : Color.BLACK;
            Dimension size = new Dimension(30,40);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setOpaque(!c.equals(" "));
            setBackground(new Color(255,255,255,85));

            Color highlight = new Color(196,206,207);
            Color shadow = new Color(176,186,187);
            setBorder(c.equals(" ") ? new EmptyBorder(0,0,0,0) : new CompoundBorder(
                    BorderFactory.createMatteBorder(1,1,0,0,highlight),
                    BorderFactory.createMatteBorder(0,0,1,1,shadow)
            ));

            JLabel label = new JLabel(c,SwingConstants.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setForeground(color);
            label.setFont(font);
            add(label);
        }
    }
}
