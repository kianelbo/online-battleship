package view.boards;

import view.ViewUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class ShipPane extends JButton {
    private int length;
    private boolean vertical;
    private MyBoard field;

    public ShipPane(int size, MyBoard field) {
        super("");
        this.length = size;
        setSize(length * 40, 40);
        vertical = false;
        paintPlain();

        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
        addFocusListener(handler);
        this.field = field;
    }

    public void rotate() {
        if (length == 1) return;
        if (field.canRotate(this)) {
            setSize(getHeight(), getWidth());
            repaint();
            vertical = !vertical;
        }
    }

    public Point[] getOccupiedCells() {
        Point[] cells = new Point[length];
        int startX = getX() / 40;
        int startY = getY() / 40;
        cells[0] = new Point(startX, startY);
        for (int i = 1; i < length; i++)
            cells[i] = new Point(vertical ? startX : startX + i, vertical ? startY + i : startY);

        return cells;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(ViewUtils.plainBorder);
        for (int i = 0; i < getWidth(); i += 40)
            g.drawLine(i, 0, i, getHeight());
        for (int i = 0; i < getHeight(); i += 40)
            g.drawLine(0, i, getWidth(), i);
    }

    private void paintPlain() {
        setBorder(new LineBorder(Color.BLUE, 2));
        setBackground(ViewUtils.plainFore);
    }

    private class MouseHandler implements MouseListener, MouseMotionListener, FocusListener {
        private Point location, initialLoc;
        private MouseEvent pressed;

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            pressed = e;
            initialLoc = e.getComponent().getLocation();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!field.canMove((ShipPane) e.getComponent())) e.getComponent().setLocation(initialLoc);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.isShiftDown()) setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (e.isAltDown() || e.isMetaDown()) return;
            Component component = e.getComponent();
            location = component.getLocation(location);
            int x = location.x - pressed.getX() + e.getX();
            int y = location.y - pressed.getY() + e.getY();
            if ((x > 0) && (x + getWidth() - 10 < field.getWidth()) && (y > 0) && (y + getHeight() - 10 < field.getHeight()))
                component.setLocation(x / 40 * 40, y / 40 * 40);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void focusGained(FocusEvent e) {
            setBorder(new LineBorder(Color.BLACK, 2));
        }

        @Override
        public void focusLost(FocusEvent e) {
            paintPlain();
        }
    }
}
