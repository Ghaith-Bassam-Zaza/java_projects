package mypro;

import java.awt.*;

/**
 *
 * @author ghaiyth
 */
public class Explosion {

    //FIELDS 
    private double x, y;
    private int r, maxRadius;
    private Character damageTybe;
    int a, b;

    //CONSTRACTOR
    public Explosion(double x, double y, int r, Character damageTybe, int max) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.damageTybe = damageTybe;
        this.maxRadius = max;
        a = 255;
        b = 255;
    }

    //METHODES
    public boolean update() {
        if (damageTybe == 'N') {
            r += 2;
        }
        if (damageTybe == 'P') {
            r += 5;
        }
        if (damageTybe == 'E') {
            r += 10;
        }
        if (a > 14) {
            a -= 15;
        }
        if (b > 4) {
            b -= 5;
        }
        return r >= maxRadius;
    }

    public void draw(Graphics2D g) {
        if (damageTybe == 'N') {
            g.setColor(new Color(255, 255, 255, a));
        } else if (damageTybe == 'P') {
            g.setStroke(new BasicStroke(3));
            g.setColor(new Color(255, 0, 0, a));
            g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setColor(new Color(255, 255, 255, a));
        } else if (damageTybe == 'E') {
            g.setStroke(new BasicStroke(3));
            g.setColor(new Color(0, 255, 0, b));
            g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setColor(new Color(255, 255, 255, b));
        }
        g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
        g.setStroke(new BasicStroke(1));
    }

    public int getR() {
        return r;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Character getTybe() {
        return damageTybe;
    }
}
