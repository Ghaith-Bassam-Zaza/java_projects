package mypro;

import java.awt.*;

/**
 *
 * @author ghaiyth
 */
public class Enemy {

    //FIELDS
    private double x, y, dx, dy, radian, speed;
    private int rank, type, health, r;
    Color C, C1;
    boolean ready, dead;

    private static boolean slow = false;
    private static boolean freez = false;

    private static double deathX;
    private static double deathY;

    private double Movment_Timer = 0;

    //CONSTRACTOR
    public Enemy(int rank, int type) {

        this.rank = rank;
        this.type = type;

        //NORMAL
        if (type == 1) {
            C = Color.WHITE;
            switch (rank) {
                case 1:
                    C1 = Color.YELLOW.darker();
                    speed = 4;
                    r = 7;
                    health = 1;
                    break;
                case 2:
                    C1 = Color.blue.darker();
                    speed = 4;
                    r = 7;
                    health = 4;
                    break;
                case 3:
                    C1 = Color.red.darker();
                    speed = 8;
                    r = 7;
                    health = 2;
                    break;
                default:
                    break;
            }
        } //ONE DIES ANOTHER COMES
        else if (type == 2) {
            C = Color.yellow;
            switch (rank) {
                case 1:
                    C1 = Color.YELLOW.darker();
                    speed = 4;
                    r = 7;
                    health = 1;
                    break;
                case 2:
                    C1 = Color.blue.darker();
                    speed = 4;
                    r = 7;
                    health = 4;
                    break;
                case 3:
                    C1 = Color.red.darker();
                    speed = 8;
                    r = 7;
                    health = 2;
                    break;
                default:
                    break;
            }
        } //SPLITES
        else if (type == 3) {
            C = Color.ORANGE;
            switch (rank) {
                case 1:
                    C1 = Color.YELLOW.darker();
                    speed = 4;
                    r = 10;
                    health = 1;
                    break;
                case 2:
                    C1 = Color.blue.darker();
                    speed = 4;
                    r = 10;
                    health = 4;
                    break;
                case 3:
                    C1 = Color.red.darker();
                    speed = 8;
                    r = 10;
                    health = 2;
                    break;
                default:
                    break;
            }
        } //COMES FROM #3
        else if (type == 4) {
            C = Color.WHITE;
            switch (rank) {
                case 1:
                    C1 = Color.YELLOW.darker();
                    speed = 4;
                    r = 7;
                    health = 1;
                    break;
                case 2:
                    C1 = Color.blue.darker();
                    speed = 4;
                    r = 7;
                    health = 4;
                    break;
                case 3:
                    C1 = Color.red.darker();
                    speed = 8;
                    r = 7;
                    health = 2;
                    break;
                default:
                    break;
            }
        } //EXPLODES
        else if (type == 5) {
            C = Color.red;
            switch (rank) {
                case 1:
                    C1 = Color.YELLOW.darker();
                    speed = 4;
                    r = 7;
                    health = 1;
                    break;
                case 2:
                    C1 = Color.blue.darker();
                    speed = 4;
                    r = 7;
                    health = 4;
                    break;
                case 3:
                    C1 = Color.red.darker();
                    speed = 8;
                    r = 7;
                    health = 2;
                    break;
                default:
                    break;
            }
        } //SHOOTS BOMBS
        else if (type == 6) {
            C = Color.YELLOW;
            switch (rank) {
                case 1:
                    C1 = Color.YELLOW.darker();
                    speed = 4;
                    r = 12;
                    health = 1;
                    break;
                case 2:
                    C1 = Color.blue.darker();
                    speed = 4;
                    r = 12;
                    health = 4;
                    break;
                case 3:
                    C1 = Color.red.darker();
                    speed = 8;
                    r = 12;
                    health = 2;
                    break;
                default:
                    break;
            }
        }
        //LOCATION OF DEPLOYMENT
        if (type == 4) {
            x = deathX;
            y = deathY;
        } else {
            x = Math.random() * framer.WIDTH / 2 + framer.WIDTH / 4;
            y = -r;
        }
        //MOVENENTS
        double angle = Math.random() * 140 + 20;
        radian = Math.toRadians(angle);

        dx = Math.cos(radian) * speed;
        dy = Math.sin(radian) * speed;

        //DEPLOYMENT BOOLEAN
        ready = false;
        dead = false;
    }

    //METHODES
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public int getType() {
        return type;
    }

    public int getRank() {
        return rank;
    }

    public void hit() {
        health--;
        if (health <= 0) {
            die();
        }
    }

    public void die() {
        dead = true;
        if (type == 2) {
            switch (rank) {
                case 1:
                    framer.enemies.add(new Enemy(1, 1));
                    break;
                case 2:
                    framer.enemies.add(new Enemy(1, 2));
                    framer.enemies.add(new Enemy(2, 1));
                    break;
                case 3:
                    framer.enemies.add(new Enemy(2, 2));
                    framer.enemies.add(new Enemy(3, 1));
                    break;
                default:
                    break;
            }
        }
        if (type == 3) {
            deathX = x;
            deathY = y;
            switch (rank) {
                case 1:
                    framer.enemies.add(new Enemy(2, 4));
                    for (int i = 0; i < 2; i++) {
                        framer.enemies.add(new Enemy(3, 4));
                    }
                    break;
                case 2:
                    for (int i = 0; i < 2; i++) {
                        framer.enemies.add(new Enemy(3, 4));
                    }
                    break;
                case 3:
                    for (int i = 0; i < 2; i++) {
                        framer.enemies.add(new Enemy(2, 4));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void update() {
        //SLOW MOTION
        if (slow) {

            x += dx / 2;

            y += dy / 2;
        } //FREEZING
        else if (freez) {
        } //NORMAL
        else {
            x += dx;
            y += dy;
        }
        if (!ready) {
            if (x > r && y > r && x < framer.WIDTH - r && y < framer.HEIGHT - r) {
                ready = true;
            }
        }
        if ((x < r && dx < 0) || (x > framer.WIDTH - r && dx > 0)) {
            dx = -dx;

        }
        if ((y < r && dy < 0) || (y > framer.HEIGHT - r && dy > 0)) {
            dy = -dy;
        }
        //SHOOTER

        if (type == 6) {
            if ((System.currentTimeMillis() / 1000) - Movment_Timer >= 3) {

                Movment_Timer = System.currentTimeMillis() / 1000;
                framer.bombs.add(new bomb((int) x, (int) y, player.getX(), player.getY()));
            }

        }
    }

    public void draw(Graphics2D g) {
        g.setColor(C);
        g.fillOval((int) x - r, (int) y - r, 2 * r, 2 * r);
        g.setStroke(new BasicStroke(3));
        g.setColor(C1);
        g.drawOval((int) x - r, (int) y - r, 2 * r, 2 * r);
        g.setStroke(new BasicStroke(1));

    }

    public static void slowDown() {
        slow = true;

    }

    public static void fastUp() {
        slow = false;

    }

    public static void freez() {
        freez = true;

    }

    public static void relese() {
        freez = false;

    }

}
