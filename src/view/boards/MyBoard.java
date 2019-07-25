package view.boards;

import logic.gameplay.BoardMap;
import view.ViewUtils;

import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MyBoard extends BaseBoard {
    private ArrayList<ShipPane> shipPanes;
    private BoardMap myMap;
    private boolean finalized;

    public MyBoard() {
        super();
        setBorder(new LineBorder(ViewUtils.plainBorder, 1));
        shipPanes = new ArrayList<>();
        finalized = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(ViewUtils.plainBorder);
        for (int j = 0; j < getHeight(); j += (getHeight() / 10)) {
            g.drawLine(0, j, getWidth(), j);
            g.drawLine(j, 0, j, getHeight());
        }
        if (!finalized) return;

        Graphics2D g2d = (Graphics2D) g;
        // drawing ships
        for (ShipPane s : shipPanes) {
            g2d.setPaint(ViewUtils.plainFore);
            g2d.fill(new Rectangle2D.Double(s.getX() + 2, s.getY() + 2, s.getWidth() - 4, s.getHeight() - 4));
            g2d.setPaint(ViewUtils.plainBorder);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(new Rectangle2D.Double(s.getX(), s.getY(), s.getWidth(), s.getHeight()));
        }

        // drawing sunken cells
        for (Point p : myMap.getSunkenCells()) {
            int x = p.x * 40;
            int y = p.y * 40;
            g2d.setPaint(ViewUtils.hitFore);
            g2d.fill(new Rectangle2D.Double(x + 2, y + 2, 40 - 4, 40 - 4));
            g2d.setPaint(ViewUtils.hitBorder);
            g2d.draw(new Rectangle2D.Double(x, y, 40, 40));
            g2d.drawLine(x, y, x + 40, y + 40);
            g2d.drawLine(x + 40, y, x, y + 40);
        }
    }

    public void addShip (ShipPane ship) {
        shipPanes.add(ship);
        outer : for (int x = 0; x < 340; x += 40)
            for (int y = 0; y < 340; y += 40) {
                ship.setLocation(x, y);
                if (canMove(ship)) break outer;
            }
        add(ship);
        repaint();
    }

    public boolean canMove(ShipPane ship) {
        Rectangle bounds = ship.getBounds();
        bounds.grow(2, 2);
        for (ShipPane shipPane : shipPanes)
            if (bounds.intersects(shipPane.getBounds()) && !shipPane.equals(ship)) return false;
        return true;
    }

    public boolean canRotate(ShipPane ship) {
        Rectangle bounds = ship.getBounds();
        bounds = new Rectangle(bounds.x, bounds.y, bounds.height, bounds.width);
        if (bounds.x + bounds.width > 400 || bounds.y + bounds.height > 400) return false;
        bounds.grow(2, 2);
        for (ShipPane shipPane : shipPanes)
            if (bounds.intersects(shipPane.getBounds()) && !shipPane.equals(ship)) return false;
        return true;
    }

    public ShipPane getFocusedShip() {
        for (ShipPane s : shipPanes)
            if (s.hasFocus()) return s;
        return null;
    }

    public ArrayList<ShipPane> getShipPanes() {
        return this.shipPanes;
    }

    public void reset() {
        for (ShipPane s : shipPanes) remove(s);
        shipPanes = new ArrayList<>();
        repaint();
    }

    public void confirmArrange(BoardMap myMap) {
        this.myMap = myMap;
        for (ShipPane sh : shipPanes)
            remove(sh);
        finalized = true;
        repaint();
    }
}
