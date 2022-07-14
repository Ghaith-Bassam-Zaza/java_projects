package greenball;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
public class GreenBall extends JComponent implements ActionListener , MouseMotionListener{
    static int ballX=100;
    static int ballY=100;
    static int ballSX=7;
    static int ballSY=5;
    static int boardX=200;
    static int boardY=475;
    static int Score =0;
    static JFrame F = new JFrame("GreenBall");
    BufferedImage BI;
    public GreenBall(){
        File f = new File("Untitled-1.png");
        try{
            
            BI = ImageIO.read(f);
            
        }
        catch(Exception e ){
            JOptionPane.showMessageDialog(null, e);
        } 
    }
    public static void main(String[] args) {
        GreenBall B=new GreenBall();
        
        F.setResizable(false);
        F.add(B);
        F.pack();
        F.setLocationRelativeTo(null);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        F.addMouseMotionListener(B);
        F.setVisible(true);
        
        Timer T = new Timer(17,B);
        T.start();
        
    }
    public void gameOver(){
        JOptionPane.showMessageDialog(null, "!!!!Game Over!!!! \n your score = " + Score);
        ballX=100;
        ballY=100;
        F.setVisible(true);
        Score=0;
        repaint();
    }
    @Override
    public Dimension getPreferredSize() {

        return new Dimension(800, 550);
    }
    @Override 
    public void paintComponent(Graphics B){
        B.setColor(Color.blue);
        B.fillRect(0,0, 800, 500);
        
        B.setColor(Color.green);
        B.fillRect(0, 500, 800, 50);
        
        B.drawImage(BI, ballX, ballY, this);
        
        B.setColor(Color.black);
        B.fillRect(boardX, boardY, 130, 20);
        
        B.setColor(Color.white);
        B.setFont(new Font("Arial", 8, 30));
        B.drawString(String.valueOf(Score), 20, 50);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ballX =ballSX+ballX ;
        ballY =ballSY+ballY;
        if (ballX >= 800){
            ballSX=-5;
        }
        if (ballX <= 0){
            ballSX=5;
        }
        if (ballY >= 550){
            gameOver();
        }
        if (ballY <= 0){
            ballSY=7;
        }
        if (ballX >= boardX && ballX <= boardX + 120 && ballY >= 460 && ballY <=480){
            ballSY=-7;
            Score++;
            repaint();
        }
        repaint();
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        boardX=e.getX()-60;
        repaint();
    }
}