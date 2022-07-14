package mypro;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author ghaiyth
 */
public class Bullet  {

    //FEILDS
    private double x, y, dx, dy, radian;
    public static double speed;
    private int r;
    private static boolean freez = false;
    private boolean reflected = false;
    private BufferedImage bullet = null;
    private static int BW;
    private static int BH;
    //CONSTRACTOR
    public Bullet(int angle, int x, int y) {
        this.x = x;
        this.y = y;
        r = 3;
        radian = Math.toRadians(angle);
        speed = 10;
        dx = Math.cos(radian) * speed;
        dy = Math.sin(radian) * speed;
        
        try{
            bullet = ImageIO.read(new File("myBullet.png"));
        }catch(IOException IOE){
            
        }
        BW = bullet.getWidth();
        BH = bullet.getHeight();
    }
    //METHODES

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    

    public boolean update() {
        if (!freez) {
            x += dx;
            y += dy;
        }
        return y + r < 0 || x + r < 0 || x - r > framer.WIDTH || y - r > framer.HEIGHT;
    }

    public void draw(Graphics2D g) {
        
        BufferedImage bullet1 = null;
        try{
            bullet1 = ImageIO.read(new File("myBullet2.png"));
        }catch(IOException IOE){
            
        }
       
        if(reflected){
            g.drawImage(bullet, null, (int) (x - bullet.getWidth()/2), (int) (y - bullet.getHeight()/2));
        }else{
            g.drawImage(bullet1, null, (int) (x - bullet1.getWidth()/2), (int) (y - bullet1.getHeight()/2));
        }
        reflected =!reflected;
      /*  g.setColor(Color.green);
        g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);*/
    }

    public static void freez() {
        freez = true;

    }

    public static void relese() {
        freez = false;

    }
    public static int getHeight(){
        return BH;
    }
    public static int getWidth(){
        return BW;
    }
}
