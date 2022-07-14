package mypro;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class player {

    //FIELDS
    private static int X, Y, DX, DY, R,PW,PH, Lives, score, rocketsPack, beginingX, beginingY, stopingX, stopingY, stopingDistance;
    public static double SpeedX, SpeedY, accelerationX, accelerationY, Pwx, Pwy;

    private static boolean up, down, left, right;

    static Color C1;
    private static BufferedImage ship = null;

    private static boolean firing, recovering, shootingR;
    private static long firingTimer, firingDelay, recoveryTimer, shootingDelay, shootingTimer;

    protected static int powerLevel, power;
    private static int[] reqPower = {
        1, 2, 3, 4, 5, 6, 7
    };

    //CONSTRUCTOR
    public player() {
        X = framer.WIDTH / 2;
        Y = framer.HEIGHT / 2;

        R = 7;
        

        SpeedX = 0;
        SpeedY = 0;
        accelerationX = 0;
        accelerationY = 0;
        Pwx = 5;
        Pwy = 5;
        beginingX = 0;
        beginingY = 0;
        stopingX = 0;
        stopingY = 0;
        stopingDistance = 50;

        Lives = 3;

        DX = 0;
        DY = 0;

        C1 = Color.green;

        firing = false;
        shootingR = false;

        firingDelay = 400;
        shootingDelay = 1000;
        firingTimer = System.nanoTime();
        shootingTimer = System.nanoTime();

        recovering = false;
        recoveryTimer = 0;

        score = 0;
        rocketsPack = 0;
    }

    //METHODES
    public void update() {
        System.out.println(SpeedX + "/" + SpeedY);
        if (up) {

            SpeedY = -Math.sqrt(Math.abs(2 * accelerationY * (Y - beginingY) - 1));
            DY = (int) SpeedY;
            accelerationY = -Pwy / (SpeedY - 1);

        } else if (SpeedY < 0 && !down) {
            //  accelerationY = SpeedY * SpeedY / (2 * (Y - stopingY - 1));
            accelerationY = SpeedY * SpeedY / (2 * stopingDistance);
            SpeedY = -Math.sqrt(Math.abs(2 * accelerationY * (Y - stopingY) + SpeedY * SpeedY));
            DY = (int) SpeedY;

        }
        if (down) {

            SpeedY = Math.sqrt(Math.abs(2 * accelerationY * (Y - beginingY) + 1));
            DY = (int) SpeedY;
            accelerationY = -Pwy / (SpeedY + 1);

        } else if (SpeedY > 0 && !up) {
            //  accelerationY = -SpeedY * SpeedY / (2 * (Y - stopingY + 1));
            accelerationY = -SpeedY * SpeedY / (2 * stopingDistance);
            SpeedY = Math.sqrt(Math.abs(2 * accelerationY * (Y - stopingY) + SpeedY * SpeedY));
            DY = (int) SpeedY;

        }
        if (right) {

            SpeedX = Math.sqrt(Math.abs(2 * accelerationX * (X - beginingX) + 1));
            DX = (int) SpeedX;
            accelerationX = -Pwx / (SpeedX + 1);

        } else if (SpeedX > 0 && !left) {
            //accelerationX = -SpeedX * SpeedX / (2 * (X - stopingX + 1));
            accelerationX =- SpeedX * SpeedX / (2 * stopingDistance);
            SpeedX = Math.sqrt(Math.abs(2 * accelerationX * (X - stopingX) + SpeedX * SpeedX));
            DX = (int) SpeedX;

        }
        if (left) {

            SpeedX = -Math.sqrt(Math.abs(2 * accelerationX * (X - beginingX) - 1));
            DX = (int) SpeedX;
            accelerationX = -Pwx / (SpeedX - 1);

        } else if (SpeedX < 0 && !right) {
            // accelerationX = SpeedX * SpeedX / (2 * (X - stopingX - 1));
            accelerationX = SpeedX * SpeedX / (2 * stopingDistance);
            SpeedX = -Math.sqrt(Math.abs(2 * accelerationX * (X - stopingX) + SpeedX * SpeedX));
            DX = (int) SpeedX;

        }
        X += DX;
        Y += DY;
        if (X > framer.WIDTH - R) {
            X = framer.WIDTH - R;
        }
        if (Y > framer.HEIGHT - R) {
            Y = framer.HEIGHT - R;
        }
        if (X < R) {
            X = R;
        }
        if (Y < R) {
            Y = R;
        }

        if (shootingR && rocketsPack > 0) {
            long E;
            E = (System.nanoTime() - shootingTimer) / 1000000;
            if (E > shootingDelay) {
                framer.rockets.add(new rocket(X, Y));
                shootingTimer = System.nanoTime();
                rocketsPack--;
            }
        }
        if (firing) {
            long E;
            E = (System.nanoTime() - firingTimer) / 1000000;
            if (E > firingDelay) {
                switch (powerLevel) {
                    case 0:
                        framer.bullets.add(new Bullet(270, X, Y));
                        firingTimer = System.nanoTime();
                        break;
                    case 1:
                        framer.bullets.add(new Bullet(270, X - 5, Y));
                        framer.bullets.add(new Bullet(270, X + 5, Y));
                        firingTimer = System.nanoTime();
                        break;
                    case 2:
                        framer.bullets.add(new Bullet(270, X - 8, Y));
                        framer.bullets.add(new Bullet(270, X, Y));
                        framer.bullets.add(new Bullet(270, X + 8, Y));
                        firingTimer = System.nanoTime();
                        break;
                    case 3:
                        framer.bullets.add(new Bullet(250, X - 8, Y));
                        framer.bullets.add(new Bullet(270, X, Y));
                        framer.bullets.add(new Bullet(290, X + 8, Y));
                        firingTimer = System.nanoTime();
                        break;
                    case 4:
                        framer.bullets.add(new Bullet(250, X - 10, Y));
                        framer.bullets.add(new Bullet(270, X - 5, Y));
                        framer.bullets.add(new Bullet(270, X + 5, Y));
                        framer.bullets.add(new Bullet(290, X + 10, Y));
                        firingTimer = System.nanoTime();
                        break;
                    case 5:
                        framer.bullets.add(new Bullet(250, X - 10, Y));
                        framer.bullets.add(new Bullet(260, X - 5, Y));
                        framer.bullets.add(new Bullet(270, X, Y));
                        framer.bullets.add(new Bullet(280, X + 5, Y));
                        framer.bullets.add(new Bullet(290, X + 10, Y));
                        firingTimer = System.nanoTime();
                        break;
                    case 6:
                        framer.bullets.add(new Bullet(240, X - 10, Y));
                        framer.bullets.add(new Bullet(250, X - 10, Y));
                        framer.bullets.add(new Bullet(260, X - 5, Y));
                        framer.bullets.add(new Bullet(270, X, Y));
                        framer.bullets.add(new Bullet(280, X + 5, Y));
                        framer.bullets.add(new Bullet(290, X + 10, Y));
                        framer.bullets.add(new Bullet(300, X + 10, Y));
                        firingTimer = System.nanoTime();
                        break;
                    default:
                        break;
                }
            }

        }
        long e = (System.nanoTime() - recoveryTimer) / 1000000;
        if (e > 2000) {
            recovering = false;
            recoveryTimer = 0;
        }
    }

    public void draw(Graphics2D g) {
        if (recovering) {
            g.setStroke(new BasicStroke(3));
            g.setColor(C1);
            g.drawOval(X - 4 * R, Y - 4 * R, 8 * R, 8 * R);

        }
        
        try{
            ship = ImageIO.read(new File("ship_186.png"));
        }catch(IOException IOE){
            
        }
        PW=ship.getWidth();
        PH=ship.getHeight();
        g.drawImage(ship, null, X-PW/2, Y-PH/2);
        /*g.setColor(C1);
        g.fillOval(X - R, Y - R, 2 * R, 2 * R);
        g.setStroke(new BasicStroke(3));
        g.setColor(C1.darker());
        g.drawOval(X - R, Y - R, 2 * R, 2 * R);
        g.setStroke(new BasicStroke(1));*/
    }

    public static void gainLife() {
        Lives++;
    }

    public static void gainRocket() {
        rocketsPack++;
    }

    public static void loseLife() {
        Lives--;
        recovering = true;
        recoveryTimer = System.nanoTime();
        if (powerLevel > 0) {
            powerLevel--;
        }
        power = 0;
        if (Lives <= 0) {

            new framer().gameOver(score);

        }
    }

    public static void increasePower(int i) {
        if (powerLevel + 1 < reqPower.length) {
            power += i;
            if (power >= 2 * reqPower[powerLevel]) {
                power -= 2 * reqPower[powerLevel];
                powerLevel++;
            }
        } else {
            power = 0;
        }
    }

    public static void setBeginingX(int BX) {
        beginingX = BX;
    }

    public static void setBeginingY(int BY) {
        beginingY = BY;
    }

    public static void setStopingX(int SX) {
        stopingX = SX;
    }

    public static void setStopingY(int SY) {
        stopingY = SY;
    }

    public static void setLeft(boolean b) {
        left = b;
    }

    public static void setRight(boolean b) {
        right = b;
    }

    public static void setUp(boolean b) {
        up = b;
    }

    public static void setDown(boolean b) {
        down = b;
    }

    public static void setfiring(boolean b) {
        firing = b;
    }

    public static void setLives(int L) {
        Lives = L;
    }

    public static void setScore(int S) {
        score = S;
    }

    public static void addScore(int i) {
        score += i;
    }

    public static int getX() {
        return X;
    }

    public static int getY() {
        return Y;
    }

  /*  public static int getR() {
        return R;
    }*/
    public static int getHeight() {
        return PH;
    }
    public static int getWidth() {
        return PW;
    }

    public static int getLives() {
        return Lives;
    }

    public static boolean isRecovering() {
        return recovering;
    }

    public static int getScore() {
        return score;
    }

    public static int getPower() {
        return power;
    }

    public static int getPowerLevel() {
        return powerLevel;
    }

    public static int getReqPower() {

        return reqPower[powerLevel];
    }

    public static boolean getFiring() {
        return firing;
    }

    public static int getRocketsPack() {
        return rocketsPack;
    }

    public static boolean getLeft() {
        return left;
    }

    public static boolean getRight() {
        return right;
    }

    public static boolean getUp() {
        return up;
    }

    public static boolean getDown() {
        return down;
    }

    public static void shootRocket(boolean b) {
        shootingR = b;
    }
    
    public static BufferedImage getship(){
        return ship;
    }
    
}
