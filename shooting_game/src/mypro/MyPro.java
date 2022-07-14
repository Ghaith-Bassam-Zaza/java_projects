/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mypro;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import javax.swing.*;

/**
 *
 * @author ghaith
 */
public class MyPro {

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int WIDTH = (int) screenSize.getWidth();
    public static final int HEIGHT = (int) screenSize.getHeight();

    public static final JButton startButton = new JButton("Start");
    public static final JButton optionsButton = new JButton("Options");
    public static final JButton closeButton = new JButton("EXIT");
    private static final int BW = 300;
    private static final int BH = 60;

    static JFrame F = new JFrame("GameFrame");
    static JFrame F1 = new JFrame("GameFrame");
    static JFrame MF = new JFrame("MENU");
    static optionMenu OPF = new optionMenu("OPTIONS", BH, BW, WIDTH, HEIGHT);
    static JFrame OF = new JFrame("GAME OVER");

    public static void main(String[] args) {

        F.setLayout(null);
        startButton.setBounds(WIDTH / 2 - BW / 2, HEIGHT / 2 - BH / 2, BW, BH);
        optionsButton.setBounds(WIDTH / 2 - BW / 2, HEIGHT / 2 + BH - 15, BW, BH);
        closeButton.setBounds(WIDTH / 2 - BW / 2, HEIGHT / 2 + BH * 2, BW, BH);

        F.getContentPane().setBackground(Color.BLACK);

        BufferedImage cursorImgMain = null;

        try {
            cursorImgMain = ImageIO.read(new File("cursorG.png"));
            cursorImgMain = resize(cursorImgMain,90,100);
        } catch (Exception e) {
        }
        Cursor CursorMain = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImgMain, new Point(0, 0), "cursorG");
        
        F.getContentPane().setCursor(CursorMain);

        startButton.setBorder(null);
        optionsButton.setBorder(null);
        closeButton.setBorder(null);

        startButton.setFocusable(false);
        optionsButton.setFocusable(false);
        closeButton.setFocusable(false);

        startButton.setForeground(Color.green);
        optionsButton.setForeground(Color.green);
        closeButton.setForeground(Color.green);

        startButton.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        optionsButton.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        closeButton.setFont(new Font("Century Gothic", Font.PLAIN, 50));

        startButton.setBackground(Color.black);
        optionsButton.setBackground(Color.black);
        closeButton.setBackground(Color.black);

        startButton.setContentAreaFilled(false);
        optionsButton.setContentAreaFilled(false);
        closeButton.setContentAreaFilled(false);

        startButton.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        optionsButton.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        closeButton.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));

        F.add(closeButton);
        F.add(optionsButton);
        F.add(startButton);

        startButton.addActionListener((ActionEvent e) -> {

            F1.setContentPane(new framer());
            F1.setExtendedState(JFrame.MAXIMIZED_BOTH);
            F1.setUndecorated(true);
            F1.setResizable(false);
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 0), "blank cursor");
            F1.getContentPane().setCursor(blankCursor);
            F1.pack();
            F1.setLocationRelativeTo(null);
            F1.setVisible(true);
            F.dispose();
        });
        optionsButton.addActionListener((ActionEvent a) -> {

            OPF.setExtendedState(JFrame.MAXIMIZED_BOTH);
            OPF.setVisible(true);
            F.dispose();
        });
        closeButton.addActionListener((ActionEvent e) -> {
            F.dispose();
            System.exit(0);
        });
        F.setExtendedState(JFrame.MAXIMIZED_BOTH);
        F.setUndecorated(true);

        F.setResizable(false);

        F.setLocationRelativeTo(null);

        F.setVisible(true);

    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
