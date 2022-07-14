package mypro;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Ghaith
 */
public class bomb {
    //FEILDS

    private double px, py, x, y, dx, dy;
    public static double speed;
    private int r;
    private static boolean freez = false;

    //CONSTRACTOR
    public bomb(int x, int y, int px, int py) {
        this.x = x;
        this.y = y;
        this.px = px;
        this.py = py;
        r = 3;
        speed = 10;
        dx = speed * ((px - x) / (Math.sqrt(((x - px) * (x - px)) + ((y - py) * (y - py)))));
        dy = speed * ((py - y) / (Math.sqrt(((x - px) * (x - px)) + ((y - py) * (y - py)))));

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

    public boolean update() {
        if (!freez) {
            x += dx;
            y += dy;
        }
        if (y + r < 0 || x + r < 0 || x - r > framer.WIDTH || y - r > framer.HEIGHT) {
            return true;
        } else {
            return false;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.red);
        g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }

    public static void freez() {
        freez = true;

    }

    public static void relese() {
        freez = false;

    }
}
