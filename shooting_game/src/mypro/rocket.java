/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mypro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author ghaiyth
 */
public class rocket {

    //FIELDS 
    private double x, y, dx, dy;
    private int r;
    private double speed, rad, rad1;

    //CONSTRACTOR
    public rocket(double x, double y) {
        this.x = x;
        this.y = y;
        r = 6;
        speed = 5;

    }

    //METHODES
    public boolean update() {
        dx = speed * (((framer.WIDTH / 2) - x) / (Math.sqrt(((x - (framer.WIDTH / 2)) * (x - (framer.WIDTH / 2))) + ((y - (framer.HEIGHT / 2)) * (y - (framer.HEIGHT / 2))))));
        dy = speed * (((framer.HEIGHT / 2) - y) / (Math.sqrt(((x - (framer.WIDTH / 2)) * (x - (framer.WIDTH / 2))) + ((y - (framer.HEIGHT / 2)) * (y - (framer.HEIGHT / 2))))));
        rad = Math.asin(((framer.HEIGHT / 2) - y) / (Math.sqrt(((x - (framer.WIDTH / 2)) * (x - (framer.WIDTH / 2))) + ((y - (framer.HEIGHT / 2)) * (y - (framer.HEIGHT / 2))))));
        rad1 = Math.acos(((framer.WIDTH / 2) - x) / (Math.sqrt(((x - (framer.WIDTH / 2)) * (x - (framer.WIDTH / 2))) + ((y - (framer.HEIGHT / 2)) * (y - (framer.HEIGHT / 2))))));
        x += dx;
        y += dy;
        if ((framer.WIDTH / 2 - 10) <= x && x <= (framer.WIDTH / 2 + 10) && (framer.HEIGHT / 2 - 10) <= y && y <= (framer.HEIGHT / 2 + 10)) {
            framer.explosions.add(new Explosion(x, y, 0, 'E', (int) Math.sqrt((framer.HEIGHT / 2) * (framer.HEIGHT / 2) + (framer.WIDTH / 2) * (framer.WIDTH / 2))));
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.green.darker());
        g.fillOval((int) x - r, (int) y - 2 * r, 2 * r, 2 * r);

        g.setStroke(new BasicStroke(5));
        g.setColor(Color.green.darker());
        g.drawOval((int) x - r, (int) y - 2 * r, 2 * r, 2 * r);

        g.setStroke(new BasicStroke(3));
        g.setColor(Color.BLACK);
        g.drawOval((int) x - r, (int) y - 2 * r, 2 * r, 2 * r);

        g.setStroke(new BasicStroke(1));
    }
}
