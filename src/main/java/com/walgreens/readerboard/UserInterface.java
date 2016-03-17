package com.walgreens.readerboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
 * @since 2/22/2016
 */
public class UserInterface extends JFrame implements ActionListener {
    JPanel contents;
    GuiBoard guiBoard;
    static Color wagRed = new Color(229,24,55);
    static Color background = Color.WHITE;
    static List<Image> icons = new ArrayList<>();

    UserInterface() {
        // Set icon/s
        try {
            ClassLoader loader = getClass().getClassLoader();
            URL ico16 = loader.getResource("icons/icon_16.png"),
                    ico32 = loader.getResource("icons/icon_32.png"),
                    ico64 = loader.getResource("icons/icon_64.png"),
                    ico128 = loader.getResource("icons/icon_128.png");
            if (ico16 != null) icons.add(ImageIO.read(ico16));
            if (ico32 != null) icons.add(ImageIO.read(ico32));
            if (ico64 != null) icons.add(ImageIO.read(ico64));
            if (ico128 != null) icons.add(ImageIO.read(ico128));

            setIconImages(icons);
        }
        catch (IOException e) {
            // Ignore, as it's not critical
            e.printStackTrace();
        }

        // Build the user interface
        buildInterface();

        // Set window configuration
        setTitle(Main.name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        // Set dimensions
        Dimension dimension = new Dimension(600,450);
        setPreferredSize(dimension);

        // Pack elements
        pack();

        // Center in screen
        setLocationRelativeTo(null);

        // Show the window
        setVisible(true);
    }

    private void buildInterface() {
        JPanel view = new JPanel(new BorderLayout());
        view.setBorder(new EmptyBorder(10,10,10,10));
        view.setBackground(background);
        view.setOpaque(true);

        // Add elements
        view.add(buildHeader(), BorderLayout.NORTH);
        view.add(buildToolbar(), BorderLayout.WEST);

        // Build contents panel
        contents = new JPanel();
        contents.setLayout(new BoxLayout(contents,BoxLayout.Y_AXIS));
        MatteBorder border = new MatteBorder(1,0,0,0,wagRed);
        contents.setBorder(border);
        view.add(contents, BorderLayout.CENTER);

        // Build initial GUI board (TODO: Replace with listing)
        guiBoard = new GuiBoard();
        contents.add(guiBoard);

        // Put contents in frame
        add(view);
    }

    private JPanel buildHeader() {
        // Header image
        JPanel header = new JPanel(new BorderLayout());
        URL url = getClass().getClassLoader().getResource("images/header.png");
        if(url != null) {
            JLabel image = new JLabel(new ImageIcon(url));
            header.add(image, BorderLayout.WEST);
        }

        JLabel version = new JLabel("Version " + Main.version);
        version.setForeground(new Color(200,200,200));
        version.setBorder(new EmptyBorder(0,0,10,20));
        version.setVerticalAlignment(JLabel.BOTTOM);
        header.add(version, BorderLayout.EAST);

        return header;
    }

    private JToolBar buildToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new BorderLayout());

        // Set dimensions
        Dimension dimension = new Dimension();
        dimension.width = 60;
        toolBar.setPreferredSize(dimension);
        toolBar.setBackground(wagRed);

        // Top menu
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        JButton save = new ImageButton("Import", Color.WHITE, "import.png", this, "import");
        JButton print = new ImageButton("Print", Color.WHITE, "print.png", this, "print");
        menu.add(save);
        menu.add(print);

        // Boards button
        JButton select = new ImageButton("Boards", Color.WHITE, "select.png", this, "select");
        menu.add(select);

        // QR Export button
        /*JButton qr = new ImageButton("Export", Color.WHITE, "qr.png", this, "qr");
        menu.add(qr);*/

        toolBar.add(menu, BorderLayout.CENTER);

        // Bottom "About" option
        JButton about = new ImageButton("About", Color.WHITE, "about.png", this, "about");
        toolBar.add(about, BorderLayout.SOUTH);

        return toolBar;
    }

    private void aboutDialog() {
        new AboutDialog();
    }

    private void importBoard() {
        File saveDir = new File("saves");
        JFileChooser chooser = new JFileChooser(saveDir);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Load Readerboard Savestate");
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".rb");
                }
            }

            @Override
            public String getDescription() {
                return "Readerboard Save Files (*.rb)";
            }
        });

        chooser.showOpenDialog(this);
    }

    //@Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "import":
                importBoard();
                break;
            case "about":
                aboutDialog();
                break;
            case "qr":
                new QRBlockDialog();
                break;
            default:
                UnsupportedOperationException unsupported =
                        new UnsupportedOperationException("Unsupported operation");
                new CrashHandler(unsupported);
        }
    }
}
