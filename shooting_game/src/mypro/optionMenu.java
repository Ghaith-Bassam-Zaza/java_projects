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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 *
 * @author Ghaith
 */

public class optionMenu extends JFrame{
    JButton backButton = new JButton("Back");
    JButton KBU = new JButton("use keyboard control");
    JButton MU = new JButton("use mouse control");
    
    //CONSTRACTOR
    public optionMenu(String title,int BH ,int BW,int WIDTH ,int HEIGHT) {
        
        this.setLayout(null);
        backButton.setBounds(WIDTH / 2 - BW / 2, HEIGHT / 2 - BH / 2, BW, BH);
        KBU.setBounds(WIDTH / 2 - BW / 2, HEIGHT / 2 + BH-15, BW, BH);
        MU.setBounds(WIDTH / 2 - BW / 2, HEIGHT / 2 + BH*2, BW, BH);

        backButton.setBorder(null);
        KBU.setBorder(null);
        MU.setBorder(null);

        backButton.setFocusable(false);
        KBU.setFocusable(false);
        MU.setFocusable(false);

        backButton.setForeground(Color.green);
        KBU.setForeground(Color.green);
        MU.setForeground(Color.green);

        backButton.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        KBU.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        MU.setFont(new Font("Century Gothic", Font.PLAIN, 20));

        backButton.setBackground(Color.black);
        KBU.setBackground(Color.black);
        MU.setBackground(Color.black);

        backButton.setContentAreaFilled(false);
        KBU.setContentAreaFilled(false);
        MU.setContentAreaFilled(false);

        backButton.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        KBU.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        MU.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        
        backButton.addActionListener((ActionEvent e) -> {
            MyPro.F.setVisible(true);
            this.dispose();
            
        });
        KBU.addActionListener((ActionEvent e) -> {
            Properties prop = new Properties();
        	OutputStream output = null;

	try {

		output = new FileOutputStream("config.properties");

		// set the properties value
                
		prop.setProperty("controlMethod", "k");
		
		// save properties to project root folder
		prop.store(output, null);

	} catch (Exception e1) {
	} finally {
		if (output != null) {
			try {
				output.close();
			} catch (Exception e2) {
				
			}
                }
        }

       MyPro.F.setVisible(true);
            this.dispose();     
        });
        MU.addActionListener((ActionEvent e) -> {
            Properties prop = new Properties();
        	OutputStream output = null;

	try {

		output = new FileOutputStream("config.properties");

		// set the properties value
		prop.setProperty("controlMethod",null);
		
		// save properties to project root folder
		prop.store(output, null);

	} catch (Exception e1) {
	} finally {
		if (output != null) {
			try {
				output.close();
			} catch (Exception e2) {
				
			}
                }
        }

         MyPro.F.setVisible(true);
            this.dispose();   
        });
        
        this.add(backButton);
        this.add(KBU);
        this.add(MU);
        
        BufferedImage cursorImgMain = null;
        try{
            cursorImgMain=ImageIO.read(new File("cursorG.png"));
        }catch(Exception e){}
        Cursor CursorMain = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImgMain, new Point(0, 0), "cursorG");
        this.getContentPane().setCursor(CursorMain);
            
        this.getContentPane().setBackground(Color.BLACK);
        this.setUndecorated(true);
        this.setResizable(false);
        this.getContentPane().setCursor(CursorMain);
        this.add(backButton);
        this.setLocationRelativeTo(null);
        this.pack();
        
        this.setTitle(title);

    }
    
}
