package mypro;

import java.awt.*;

/**
 *
 * @author ghaiyth
 */
public class powerUps {

    //FIELDS
    public static int speed;
    private double x, y;
    private int type, r;
    Color C, C1;

    //CONSTRACTOR
    public powerUps(double x, double y, int type) {
        speed = 2;
        this.x = x;
        this.y = y;
        this.type = type;
        r = 5;

        //HEALHT++
        if (type == 1) {
            C = Color.pink;
            C1 = Color.red;
        }
        //POWER++
        if (type == 2) {
            C1 = Color.yellow;
            C = Color.green;

        }
        //POWER+2
        if (type == 3) {
            C1 = Color.green;
            C = Color.black;
        }
        //SLOWDOWN
        if (type == 4) {
            C = Color.gray;
            C1 = Color.YELLOW;
        }
        //ROCKET
        if (type == 5) {
            C = Color.green;
            C1 = Color.red;
        }

    }

    //METHODS
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public int getType() {
        return type;
    }

    public boolean update() {
        y += speed;
        return y > framer.HEIGHT + r;
    }

    public void draw(Graphics2D g) {
        if (type == 5) {
            g.setColor(Color.green.darker());
            g.fillOval((int) x - r, (int) y - 2 * r, 2 * r, 2 * r);

            g.setStroke(new BasicStroke(5));
            g.setColor(Color.green.darker());
            g.drawOval((int) x - r, (int) y - 2 * r, 2 * r, 2 * r);

            g.setStroke(new BasicStroke(3));
            g.setColor(Color.BLACK);
            g.drawOval((int) x - r, (int) y - 2 * r, 2 * r, 2 * r);

            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(C);
            g.fillRect((int) x - r, (int) y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(2));
            g.setColor(C1);
            g.drawRect((int) x - r, (int) y - r, 2 * r, 2 * r);
        }
    }
}
