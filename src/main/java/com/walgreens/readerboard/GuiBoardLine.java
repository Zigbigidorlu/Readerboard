package com.walgreens.readerboard;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
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
class GuiBoardLine extends JPanel {
    private Font font;
    private final List<Character> message;

    GuiBoardLine(String contents) {
        super();
        setBackground(new Color(215,225,225));
        setOpaque(true);
        setFocusable(true);

        // Set width
        int width = 30 * Main.maxLength;
        setPreferredSize(new Dimension(width,50));

        // Set layout
        setLayout(new GridBagLayout());

        // Set border
        setBorder(BorderFactory.createLineBorder(new Color(176,186,187),2));

        // Load font
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("Evogria.ttf");
            if (fontStream != null) {
                Font coreFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                font = coreFont.deriveFont(26f);
            }
        }
        catch(FontFormatException | IOException e) {
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

    private double messageSize() {
        double count = 0.0;
        for(char character : message) {
            if(character == ' ') {
                count = count + 0.3;
            }
            else {
                count++;
            }
        }
        return count;
    }

    boolean addChar(char character) {
        if(messageSize() + 1 < Main.maxLength) {
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

    private class CharBlock extends JPanel implements MouseListener, KeyListener {
        Color color;
        CharBlock(Character character) {
            put(character);
        }

        void put(Character character) {
            String c = String.valueOf(character);
            put(c);
        }

        void put(String c) {
            boolean isSpace = c.equals(" ");
            color = (StringUtils.isNumeric(c)) ? Color.RED : Color.BLACK;
            Dimension size = new Dimension((isSpace) ? 10 : 30,40);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setOpaque(!isSpace);
            setBackground(new Color(255,255,255,85));

            Color highlight = new Color(196,206,207);
            Color shadow = new Color(176,186,187);
            setBorder(isSpace ? new EmptyBorder(0,0,0,0) : new CompoundBorder(
                    BorderFactory.createMatteBorder(1,1,0,0,highlight),
                    BorderFactory.createMatteBorder(0,0,1,1,shadow)
            ));

            JLabel label = new JLabel(c,SwingConstants.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setForeground(color);
            label.setFont(font);

            removeAll();
            add(label);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2) {
                Color focus = new Color((GuiBoard.board_default.getRed() + 20),
                        (GuiBoard.board_default.getGreen() -15),
                        (GuiBoard.board_default.getBlue() + 15)
                );
                setBackground(focus);
                grabFocus();
            }
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {
            if(hasFocus()) {
                char character = Character.toLowerCase(e.getKeyChar());
                if (GuiBoard.filterList.contains(character)) {
                    put(character);
                    setBackground(new Color(255,255,255,85));
                    revalidate();
                    repaint();
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
