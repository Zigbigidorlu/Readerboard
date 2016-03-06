package com.walgreens.readerboard;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks: QR Block Dialog will display a dialog with an
 * Android readable QR code with instructions on reader-board
 * difference.
 *
 * @author Adam Treadway
 * @since 3/5/2016
 */
public class QRBlockDialog extends JDialog {
    QRBlockDialog() {
        setTitle("QR Quick Export");
        setResizable(false);
        setModal(true);
        setBackground(Color.WHITE);
        setIconImages(UserInterface.icons);
        Dimension dimension = new Dimension(350,350);
        setPreferredSize(dimension);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UserInterface.background);
        content.setOpaque(true);
        add(content);

        try {
            String data = "readerboard://Not_yet_implemented";
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 350, 350, hintMap);

            // Build image
            BufferedImage image = new BufferedImage(350,350,BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0,0,350,350);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < 350; i++) {
                for (int j = 0; j < 350; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            ImageIcon imageIcon = new ImageIcon(image);
            JLabel imgLabel = new JLabel(imageIcon);
            add(imgLabel);
        }
        catch (WriterException e) {
            new CrashHandler(e);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
