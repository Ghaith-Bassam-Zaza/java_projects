package mypro;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ghaith
 */
public class framer extends JPanel implements Runnable, KeyListener, MouseMotionListener, MouseListener {

    //Fields

    private static boolean enemiesAreSlow = false;
    private static double slowTimer = 0;

    private static int enginHeat, topEngineLength;
    private static boolean shootingAbility;

    public static final int WIDTH = MyPro.WIDTH;
    public static final int HEIGHT = MyPro.HEIGHT;

    private Thread thread;
    
    private static BufferedImage space =null;

    public static boolean running;
    public static boolean pause = false;

    protected Graphics2D g;
    private BufferedImage image;

    private int FBS = 30;
    private double averageFBS;

    public static ArrayList<Enemy> enemies;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<powerUps> powerUp;
    public static ArrayList<Explosion> explosions;
    public static ArrayList<rocket> rockets;
    public static ArrayList<bomb> bombs;

    public static player P;

    private long waveStartTimer, waveStartTimerDiff;
    private static int waveNum;
    private int waveDelay = 2000;
    private boolean waveStart;

    private static Color CE;
    private static Color C;

    private static String CM;
    private static boolean mouseUsage = false;
    Graphics2D g3;

    private static final int dmlx = WIDTH / 2;
    private static final int dmly = HEIGHT / 2;

    private int stopTimerX = 0;
    private int stopTimerY = 0;

    private int mlx;
    private int mly;

    //CONSTRACTOR
    public framer() {
        super();
       // setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        

        //objects declare
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        powerUp = new ArrayList<powerUps>();
        explosions = new ArrayList<Explosion>();
        rockets = new ArrayList<rocket>();
        bombs = new ArrayList<bomb>();

        enginHeat = 0;
        topEngineLength = 500 + player.getScore();
        shootingAbility = true;
        CE = Color.GREEN;
        C = Color.black;

        Properties prop = new Properties();
        InputStream input = null;

        //Listeners
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            CM = prop.getProperty("controlMethod");
            mouseUsage = CM == null;

        } catch (IOException ex) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        try {
            space = ImageIO.read(new File("Nebula Aqua-Pink.png"));
            
        } catch (IOException ex) {
            
        }

    }

    //METHODES
    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
            addKeyListener(this);
            MyPro.F1.setEnabled(true);
            Enemy.relese();
            Bullet.relese();
            player.accelerationX = 1;
            player.accelerationY = 1;
            player.SpeedX = 1;
            player.SpeedY = 1;
            powerUps.speed = 2;
            pause = false;
            
        }

    }

    @Override
    public void run() {

        running = true;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g3 = (Graphics2D) image.getGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        P = new player();

        long startTime;
        long URDTimeMillis;
        long waitTime;
        long totalTime = 0;
        long targetTime = 1000 / FBS;

        int frameCount = 0;
        int maxFrameCount = 30;

        waveNum = 0;
        waveStart = true;
        waveStartTimer = 0;
        waveStartTimerDiff = 0;
        //GAMELOOP
        while (running) {

            startTime = System.nanoTime();
            gameDraw();
            gameUpdate();
            gameRender();

            if (System.currentTimeMillis() - slowTimer > 3000 && enemiesAreSlow) {
                C = Color.black;
                enemiesAreSlow = false;
                Enemy.fastUp();
            }
            if (player.getFiring()) {
                if (!pause) {
                    enginHeat += 3;
                }

            } else if (enginHeat > 0) {
                if (!pause) {
                    enginHeat -= 5;
                }
            }
            if (enginHeat > topEngineLength) {
                shootingAbility = false;
                player.setfiring(false);
                CE = Color.red;
            }
            if (enginHeat <= 0) {
                shootingAbility = true;
                CE = Color.green;
            }
            URDTimeMillis = (System.nanoTime() - startTime) / 1000000;

            waitTime = targetTime - URDTimeMillis;
            try {
                Thread.sleep(waitTime);
            } catch (Exception e) {
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == maxFrameCount) {
                averageFBS = 1000.0 / (totalTime / frameCount) / 1000000;
                totalTime = 0;
                frameCount = 0;
            }

        }
    }

    public void gameUpdate() {
        //PLAYER UPDATE
        P.update();
        //BULLETS UPDATE
        for (int i = 0; i < bullets.size(); i++) {
            boolean remove = bullets.get(i).update();
            if (remove) {
                bullets.remove(i);
                i--;
            }
        }
        //ENEMY UPDATE 
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }
        //POWERUPS UPDATE
        for (int i = 0; i < powerUp.size(); i++) {
            boolean remove = powerUp.get(i).update();
            if (remove) {
                powerUp.remove(i);
                i--;
            }
        }
        //EXPLOSIONS UPDATE
        for (int i = 0; i < explosions.size(); i++) {
            boolean remove = explosions.get(i).update();
            if (remove) {
                explosions.remove(i);
                i--;
            }
        }
        //ROCKETS UPDATE
        for (int i = 0; i < rockets.size(); i++) {
            rocket r = rockets.get(i);
            boolean remove = r.update();
            if (remove) {
                rockets.remove(i);
                i--;
            }

        }
        //bombs UPDATE
        for (int i = 0; i < bombs.size(); i++) {
            boolean remove = bombs.get(i).update();
            if (remove) {
                bombs.remove(i);
                i--;
            }
        }
        //BULLET-ENEMY COLLISION
        int BW = Bullet.getHeight();
        int BH = Bullet.getWidth();
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            double bx = b.getX();
            double by = b.getY();
            
            for (int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();
                  
                double dx = Math.abs(ex - bx);
                double dy = Math.abs(ey - by);
                
                if (dx < BW/2 + er && dy < BH/2 +er) {
                    e.hit();
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }
        //EXPLOSION-ENEMY COLLISION
        for (int i = 0; i < explosions.size(); i++) {
            Explosion b = explosions.get(i);
            if (b.getTybe() == 'E') {
                double bx = b.getX();
                double by = b.getY();
                double br = b.getR();
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy e = enemies.get(j);
                    double ex = e.getX();
                    double ey = e.getY();
                    double er = e.getR();

                    double dx = ex - bx;
                    double dy = ey - by;
                    double dist = Math.sqrt(dy * dy + dx * dx);
                    if (dist < br + er) {
                        e.die();
                        break;
                    }
                }
            }
        }
        //PLAYER-ENEMY COLLUSION
        if (!player.isRecovering()) {
            int px = player.getX();
            int py = player.getY();
          //  int pr = player.getR();
          int PW =player.getWidth();
          int PH = player.getHeight();
            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);

                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();

                double dx = Math.abs(ex - px);
                double dy = Math.abs(ey - py);
               // double dist = Math.sqrt(dy * dy + dx * dx);
                if (dx < Math.abs(PW/2 + er) && dy < Math.abs(PH/2 + er)) {
                    player.loseLife();
                    e.hit();
                }
            }
        }
        //PLAYER-BOMBS COLLUSION
        if (!player.isRecovering()) {
            int px = player.getX();
            int py = player.getY();
            int PH = player.getHeight();
            int PW = player.getWidth();
            for (int i = 0; i < bombs.size(); i++) {
                bomb e = bombs.get(i);

                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();

                double dx = Math.abs(ex - px);
                double dy = Math.abs(ey - py);
                if (dx < Math.abs(PW/2 + er) && dy < Math.abs(PH/2 + er)) {
                    player.loseLife();
                    bombs.remove(e);
                    i--;
                }
            }
        }
        //PLAYER-Explosion COLLUSION
        if (!player.isRecovering()) {
            int px = player.getX();
            int py = player.getY();
            int PH = player.getHeight();
            int PW = player.getWidth();
            for (int i = 0; i < explosions.size(); i++) {
                Explosion e = explosions.get(i);
                if (e.getTybe() == 'P') {
                    double ex = e.getX();
                    double ey = e.getY();
                    double er = e.getR();

                    double dx = Math.abs(ex - px);
                    double dy = Math.abs(ey - py);
                    //double dist = Math.sqrt(dy * dy + dx * dx);
                    if (dx < Math.abs( PW/2 + er) && dy < Math.abs(PH/2 + er)) {
                        player.loseLife();

                    }
                }
            }
        }
        //PLAYER - POWERUP COLLUSION
        int px = player.getX();
        int py = player.getY();
        int PH = player.getHeight();
        int PW = player.getWidth();
        for (int i = 0; i < powerUp.size(); i++) {
            powerUps pu = powerUp.get(i);
            double pux = pu.getX();
            double puy = pu.getY(); 
            double dx = Math.abs(pux - px);
            double dy = Math.abs(puy - py);
            if (dx < Math.abs(PW/2 + 0) && dy < Math.abs(PH/2 + 0)) {
                int type = pu.getType();
                switch (type) {
                    case 1:
                        player.gainLife();
                        break;
                    case 2:
                        player.increasePower(1);
                        break;
                    case 3:
                        player.increasePower(2);
                        break;
                    case 4:

                        slowTimer = System.currentTimeMillis();
                        C = Color.GRAY.darker();
                        enemiesAreSlow = true;
                        Enemy.slowDown();
                        break;
                    case 5:
                        player.gainRocket();
                    default:
                        break;
                }

                powerUp.remove(i);

            }
        }
        //CHECK DEAD ENEMIES
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).isDead()) {
                Enemy e = enemies.get(i);

                double random = Math.random();
                if (random < 0.003) {
                    powerUp.add(new powerUps(e.getX(), e.getY(), 1));
                } else if (random < 0.07) {
                    powerUp.add(new powerUps(e.getX(), e.getY(), 3));
                } else if (random < 0.17) {
                    powerUp.add(new powerUps(e.getX(), e.getY(), 2));
                } else if (random < 0.20) {
                    powerUp.add(new powerUps(e.getX(), e.getY(), 4));
                } else if (random < 0.22) {
                    powerUp.add(new powerUps(e.getX(), e.getY(), 5));
                }
                if (e.getType() == 5) {
                    explosions.add(new Explosion(e.getX(), e.getY(), (int) e.getR(), 'P', (int) e.getR() + 100));
                } else {
                    explosions.add(new Explosion(e.getX(), e.getY(), (int) e.getR(), 'N', (int) e.getR() + 30));
                }
                enemies.remove(i);
                i--;
                player.addScore(e.getRank() + e.getType());

            }

        }

        //WAVE ADDING SYSTEM
        if (waveStartTimer == 0 && enemies.isEmpty()) {
            waveNum++;
            waveStart = false;
            waveStartTimer = System.nanoTime();
        } else {
            waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
            if (waveStartTimerDiff > waveDelay) {
                waveStart = true;
                waveStartTimerDiff = 0;
                waveStartTimer = 0;
            }
        }
        if (waveStart && enemies.isEmpty()) {
            addNewEnemies();
        }
        //Mouse Moving detactor
        if (mouseUsage) {
            if (mlx > dmlx) {
                player.setRight(true);
                player.setLeft(false);
                stopTimerX = 0;
            } else if (mlx < dmlx) {
                player.setRight(false);
                player.setLeft(true);
                stopTimerX = 0;
            } else if (stopTimerX < 1) {
                stopTimerX++;
            } else {
                player.setRight(false);
                player.setLeft(false);
                stopTimerX = 0;
            }
            if (mly > dmly) {
                player.setDown(true);
                player.setUp(false);
                stopTimerY = 0;
            } else if (mly < dmly) {
                player.setDown(false);
                player.setUp(true);
                stopTimerY = 0;
            } else if (stopTimerY < 1) {
                stopTimerY++;
            } else {
                player.setDown(false);
                player.setUp(false);
                stopTimerY = 0;
            }
            System.out.println("ML:"+(int)(mlx-dmlx));
        }
        
        //MouseLocationSetter
        try {
            if (!pause) {
                Robot R = new Robot();
                R.mouseMove(dmlx, dmly);
            }

        } catch (AWTException AE) {

        }

    }

    public void gameRender() {
        //BACKGROUND DRAW
        
        
        /* g.setColor(C);
           g.fillRect(0, 0, WIDTH, HEIGHT);*/
        g.drawImage(space, null, 0, 0);

        //PLAYER DRAW
        P.draw(g);
        //BULLETS DRAW
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }
        //BOMBS DRAW
        for (int i = 0; i < bombs.size(); i++) {
            bombs.get(i).draw(g);
        }
        //ENEMY DRAW
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
        //DRAW POWERUPS 
        for (int i = 0; i < powerUp.size(); i++) {
            powerUp.get(i).draw(g);
        }
        //DRAW EXPLOSIONS 
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).draw(g3);
        }
        //ROCKET DRAW
        for (int i = 0; i < rockets.size(); i++) {
            rockets.get(i).draw(g3);
        }
        //DRAW PLAYER POWER
        g.setColor(Color.green);
        g.fillRect(20, 40, player.getPower() * 8, 8);
        g.setColor(Color.green.darker());
        g.setStroke(new BasicStroke(2));
        for (int i = 0; i < 2 * player.getReqPower(); i++) {
            g.drawRect(20 + 8 * i, 40, 8, 8);
        }
        //DRAW EngineHeat
        g.setColor(CE);
        g.fillRect(WIDTH - 200, 20, enginHeat / 3, 15);
        g.setColor(Color.green.darker());
        g.setStroke(new BasicStroke(1));
        for (int i = 0; i < topEngineLength / 9; i++) {
            g.drawRect(WIDTH - 200 + 3 * i, 20, 3, 15);
        }
        g.setStroke(new BasicStroke(1));
        //WAVE START TEXT DRAW
        if (waveStartTimer != 0) {
            g.setFont(new Font("Century Gothic", Font.PLAIN, 50));
            String s = "  -WAVE  " + waveNum + "  - ";
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
            if (alpha > 255) {
                alpha = 255;
            }
            g.setColor(new Color(20, 232, 5, alpha));
            int tall = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
            g.drawString(s, (WIDTH - length) / 2, (HEIGHT - tall) / 2);

        }
        //LIVES DRAW
        for (int i = 0; i < player.getLives(); i++) {
            BufferedImage ship = MyPro.resize(player.getship(),20,20);
            g.drawImage(ship, null,20 + (20 * i), 20);
           /* g.setColor(player.C1);
            g.fillOval(20 + (20 * i), 20, 2 * player.getR(), 2 * player.getR());
            g.setStroke(new BasicStroke(3));
            g.setColor(player.C1.darker());
            g.drawOval(20 + (20 * i), 20, 2 * player.getR(), 2 * player.getR());
            g.setStroke(new BasicStroke(1));*/

        }
        //ROCKETS-PACK DRAW
        for (int i = 0; i < player.getRocketsPack(); i++) {
            g.setColor(Color.green.darker());
            g.fillOval(20 + (20 * i), 50, 10, 10);
            g.fillRect(20 + (20 * i), 55, 10, 10);

        }
        //SCORE DRAW
        g.setFont(new Font("Century Gothic", Font.PLAIN, 25));
        g.drawString("-SCORE:  " + player.getScore() + "-", WIDTH / 4, 40);
    }

    public void gameDraw() {

        Graphics g2 = this.getGraphics();

        g2.drawImage(image, 0, 0, null);
        g2.dispose();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (!mouseUsage) {

            if (keyCode == KeyEvent.VK_LEFT) {
                if (!player.getLeft()) {
                    player.setLeft(true);
                    player.setBeginingX(player.getX());
                }
            }

            if (keyCode == KeyEvent.VK_RIGHT) {
                if (!player.getRight()) {
                    player.setRight(true);
                    player.setBeginingX(player.getX());
                }
            }

            if (keyCode == KeyEvent.VK_UP) {
                if (!player.getUp()) {
                    player.setBeginingY(player.getY());
                    player.setUp(true);
                }

            }

            if (keyCode == KeyEvent.VK_DOWN) {
                if (!player.getDown()) {
                    player.setDown(true);
                    player.setBeginingY(player.getY());
                }
            }

            if (keyCode == KeyEvent.VK_SPACE && shootingAbility == true) {
                player.setfiring(true);
            }
            if (keyCode == KeyEvent.VK_Z) {
                player.shootRocket(true);
            }
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            pause();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!mouseUsage) {

            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT) {
                player.setLeft(false);
                player.setStopingX(player.getX());
            }
            if (keyCode == KeyEvent.VK_RIGHT) {
                player.setRight(false);
                player.setStopingX(player.getX());
            }
            if (keyCode == KeyEvent.VK_UP) {
                player.setUp(false);
                player.setStopingY(player.getY());
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                player.setDown(false);
                player.setStopingY(player.getY());
            }

            if (keyCode == KeyEvent.VK_SPACE) {
                player.setfiring(false);
            }
            if (keyCode == KeyEvent.VK_Z) {
                player.shootRocket(false);
            }
        }
    }
    //MOUSE EVENTS LISTENER

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (mouseUsage) {

            if (e.getButton() == MouseEvent.BUTTON1 && shootingAbility == true) {
                player.setfiring(true);
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                player.shootRocket(true);

            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (mouseUsage) {

            if (e.getButton() == MouseEvent.BUTTON1 && shootingAbility == true) {
                player.setfiring(false);
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                player.shootRocket(false);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseUsage) {
            mlx = MouseInfo.getPointerInfo().getLocation().x;
            mly = MouseInfo.getPointerInfo().getLocation().y;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (mouseUsage) {
            mlx = MouseInfo.getPointerInfo().getLocation().x;
            mly = MouseInfo.getPointerInfo().getLocation().y;
        }
    }

    private void addNewEnemies() {
        enemies.clear();
        if (waveNum == 1) {
            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(1, 1));
            }
        }
        if (waveNum == 2) {
            for (int i = 0; i < 25; i++) {
                enemies.add(new Enemy(1, 1));
            }

        }
        if (waveNum == 3) {
            for (int i = 0; i < 25; i++) {
                enemies.add(new Enemy(1, 1));
            }
            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(2, 1));
            }
        }
        if (waveNum == 4) {
            for (int i = 0; i < 20; i++) {
                enemies.add(new Enemy(1, 1));
            }
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(2, 1));
            }
        }
        if (waveNum == 5) {
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(1, 1));
                enemies.add(new Enemy(2, 1));
            }
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(3, 1));
            }
        }
        if (waveNum == 6) {
            for (int i = 0; i < 15; i++) {
                enemies.add(new Enemy(2, 1));
            }
            for (int i = 0; i < 15; i++) {
                enemies.add(new Enemy(3, 1));
            }
        }
        if (waveNum == 7) {
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(2, 1));
            }
            for (int i = 0; i < 20; i++) {
                enemies.add(new Enemy(3, 1));
            }
        }
        if (waveNum == 8) {

            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(1, 2));
            }
        }
        if (waveNum == 9) {
            for (int i = 0; i < 10; i++) {

                enemies.add(new Enemy(2, 1));
                enemies.add(new Enemy(3, 1));
            }
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(1, 2));
            }
        }
        if (waveNum == 10) {
            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(2, 1));
                enemies.add(new Enemy(3, 1));
            }
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(1, 2));
                enemies.add(new Enemy(2, 2));
            }
        }
        if (waveNum == 11) {
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(3, 1));

                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));

            }
        }
        if (waveNum == 12) {
            for (int i = 0; i < 15; i++) {

                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));

            }
        }
        if (waveNum == 13) {
            for (int i = 0; i < 10; i++) {

                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));
                enemies.add(new Enemy(1, 2));

            }
        }
        if (waveNum == 14) {
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(1, 3));
            }
        }
        if (waveNum == 15) {
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(1, 3));
            }
            for (int i = 0; i < 7; i++) {
                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));
                enemies.add(new Enemy(1, 2));
            }
        }
        if (waveNum == 16) {
            for (int i = 0; i < 4; i++) {
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(2, 3));
                enemies.add(new Enemy(3, 3));

                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));

            }
        }
        if (waveNum == 17) {
            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(2, 3));
                enemies.add(new Enemy(3, 3));
            }
            for (int i = 0; i < 7; i++) {
                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));
                enemies.add(new Enemy(1, 2));

            }
        }
        if (waveNum == 18) {
            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(2, 3));
                enemies.add(new Enemy(3, 3));
            }
            for (int i = 0; i < 2; i++) {

                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));
                enemies.add(new Enemy(1, 2));

            }
        }
        if (waveNum == 19) {
            for (int i = 0; i < 30; i++) {
                enemies.add(new Enemy(1, 5));

            }

        }
        if (waveNum == 20) {
            for (int i = 0; i < 3; i++) {
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(2, 3));
                enemies.add(new Enemy(3, 3));
            }
            for (int i = 0; i < 3; i++) {

                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));
                enemies.add(new Enemy(1, 2));

            }
            for (int i = 0; i < 2; i++) {

                enemies.add(new Enemy(3, 5));
                enemies.add(new Enemy(2, 5));
                enemies.add(new Enemy(1, 5));

            }
        }
        if (waveNum == 21) {
            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(1, 3));
                enemies.add(new Enemy(2, 3));
                enemies.add(new Enemy(3, 3));
            }
            for (int i = 0; i < 5; i++) {

                enemies.add(new Enemy(3, 2));
                enemies.add(new Enemy(2, 2));
                enemies.add(new Enemy(1, 2));

            }
            for (int i = 0; i < 5; i++) {

                enemies.add(new Enemy(2, 5));
                enemies.add(new Enemy(1, 5));

            }
        }
        if (waveNum == 22) {

            for (int i = 0; i < 10; i++) {
                enemies.add(new Enemy(3, 5));

            }
        }
        if (waveNum == 23) {

            for (int i = 0; i < 15; i++) {
                enemies.add(new Enemy(3, 5));

            }
        }
        if (waveNum == 24) {

            for (int i = 0; i < 20; i++) {
                enemies.add(new Enemy(3, 5));

            }
        }
        if (waveNum == 25) {

            for (int i = 0; i < 25; i++) {
                enemies.add(new Enemy(3, 5));

            }
        }
        if (waveNum == 26) {

            for (int i = 0; i < 30; i++) {
                enemies.add(new Enemy(3, 5));

            }
        }
        if (waveNum == 27) {

            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(1, 6));

            }
        }
        if (waveNum == 28) {

            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(2, 6));

            }
        }
        if (waveNum == 29) {

            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(3, 6));

            }
        }

    }

    public void gameOver(int score) {
        pause = true;
        int w = 400;
        int h = 500;
        JLabel header = new JLabel("-GAME OVER-");
        JLabel scoreD = new JLabel("-YOUR SCORE :" + score + "-");
        JLabel waveD = new JLabel("-MAX WAVE:" + waveNum + "-");
        JButton restart = new JButton("RESTART");
        JButton exit = new JButton("EXIT");
        JButton menu = new JButton("MAIN MENU");
        MyPro.OF.setLayout(null);
        MyPro.OF.setSize(w, h);
        MyPro.OF.setUndecorated(true);
        MyPro.OF.setResizable(false);
        MyPro.OF.setAlwaysOnTop(true);
        MyPro.OF.getContentPane().setBackground(Color.BLACK);
        MyPro.OF.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));

        header.setBounds(5, 50, 400, 50);
        header.setFocusable(false);
        header.setForeground(Color.green);
        header.setFont(new Font("Century Gothic", Font.BOLD, 50));

        scoreD.setBounds(25, 100, 400, 50);
        scoreD.setFocusable(false);
        scoreD.setForeground(Color.green);
        scoreD.setFont(new Font("Century Gothic", Font.PLAIN, 25));

        waveD.setBounds(25, 150, 400, 50);
        waveD.setFocusable(false);
        waveD.setForeground(Color.green);
        waveD.setFont(new Font("Century Gothic", Font.PLAIN, 25));

        restart.setBounds(25, 200, 350, 50);
        restart.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        restart.setFocusable(false);
        restart.setForeground(Color.green);
        restart.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        restart.setContentAreaFilled(false);
        restart.addActionListener((ActionEvent r) -> {
            MyPro.OF.dispose();
            MyPro.OF.remove(scoreD);
            MyPro.OF.remove(waveD);
            Enemy.relese();
            Bullet.relese();
            enemies.clear();
            bullets.clear();
            bombs.clear();
            rockets.clear();
            player.accelerationX = 1;
            player.accelerationY = 1;
            player.SpeedX = 1;
            player.SpeedY = 1;
            powerUps.speed = 2;
            pause = false;
            player.power = 0;
            player.powerLevel = 0;
            waveNum = 0;
            player.setLives(3);
            player.setScore(0);
            enginHeat = 0;
            pause = false;
        });

        menu.setBounds(25, 275, 350, 50);
        menu.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        menu.setFocusable(false);
        menu.setForeground(Color.green);
        menu.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        menu.setContentAreaFilled(false);
        menu.addActionListener((ActionEvent r) -> {
            running = false;
            MyPro.F.setVisible(true);
            MyPro.OF.dispose();
            MyPro.F1.dispose();
            player.power = 0;
            player.powerLevel = 0;
            MyPro.OF.remove(scoreD);
            MyPro.OF.remove(waveD);
        });

        exit.setBounds(25, 350, 350, 50);
        exit.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        exit.setFocusable(false);
        exit.setForeground(Color.green);
        exit.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        exit.setContentAreaFilled(false);
        exit.addActionListener((ActionEvent r) -> {
            running = false;
            MyPro.OF.dispose();
            MyPro.F1.dispose();
            System.exit(0);

        });
        MyPro.OF.add(header);
        MyPro.OF.add(scoreD);
        MyPro.OF.add(waveD);
        MyPro.OF.add(restart);
        MyPro.OF.add(exit);
        MyPro.OF.add(menu);
        MyPro.OF.setLocationRelativeTo(null);
        MyPro.OF.setVisible(true);

    }

    public void pause() {
        int w = 400;
        int h = 500;
        JLabel header = new JLabel("-MENU-");
        JButton resume = new JButton("RESUME");
        JButton surender = new JButton("SURENDER");
        MyPro.MF.setLayout(null);
        MyPro.MF.setSize(w, h);
        MyPro.MF.setUndecorated(true);
        MyPro.MF.setResizable(false);
        MyPro.MF.setAlwaysOnTop(true);
        MyPro.MF.getContentPane().setBackground(Color.BLACK);
        MyPro.MF.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));

        header.setBounds(100, 50, 400, 50);
        header.setFocusable(false);
        header.setForeground(Color.green);
        header.setFont(new Font("Century Gothic", Font.BOLD, 50));

        resume.setBounds(50, 125, 300, 50);
        resume.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        resume.setFocusable(false);
        resume.setForeground(Color.green);
        resume.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        resume.setContentAreaFilled(false);
        resume.addActionListener((ActionEvent r) -> {
            MyPro.MF.dispose();
            Enemy.relese();
            Bullet.relese();
            player.accelerationX = 1;
            player.accelerationY = 1;
            player.SpeedX = 1;
            player.SpeedX = 1;
            powerUps.speed = 2;
            pause = false;
        });
        surender.setBounds(50, 200, 300, 50);
        surender.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.green));
        surender.setFocusable(false);
        surender.setForeground(Color.green);
        surender.setFont(new Font("Century Gothic", Font.PLAIN, 50));
        surender.setContentAreaFilled(false);
        surender.addActionListener((ActionEvent r) -> {
            MyPro.MF.dispose();
            gameOver(player.getScore());
        });
        MyPro.MF.add(header);
        MyPro.MF.add(resume);
        MyPro.MF.add(surender);
        MyPro.MF.setLocationRelativeTo(null);
        MyPro.MF.setVisible(true);

        powerUps.speed = 0;
        player.SpeedX = 0;
        player.SpeedY = 0;
        player.accelerationX = 1;
        player.accelerationY = 1;
        Enemy.freez();
        Bullet.freez();
        pause = true;
        player.setfiring(false);
        player.setUp(false);
        player.setRight(false);
        player.setLeft(false);
        player.setDown(false);

    }

}
