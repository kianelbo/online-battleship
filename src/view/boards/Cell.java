package view.boards;

import view.ViewUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Cell extends JButton implements MouseListener, ActionListener {
    // revealed = !enabled
    private boolean occupied;
    private int x, y;
    private EnemyBoard board;

    public Cell(EnemyBoard board, int x, int y) {
        super("");
        mouseExited(null);
        addActionListener(this);
        addMouseListener(this);

        this.board = board;
        this.x = x;
        this.y = y;
    }

    public void reveal(boolean occupied) {
        if (isEnabled())
            this.occupied = occupied;
        setEnabled(false);
        repaint();
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isEnabled())
            if (occupied) paintHit(g);
            else paintMissed(g);
        else
            paintPlain();
    }

    private void paintPlain() {
        setBackground(Color.WHITE);
    }

    private void paintMissed(Graphics g) {
        setBorder(new LineBorder(ViewUtils.plainBorder, 1));
        setBackground(ViewUtils.plainFore);
        g.fillArc(17, 17, 10, 10, 0, 360);
    }

    private void paintHit(Graphics g) {
        setBorder(new LineBorder(ViewUtils.hitBorder, 3));
        setBackground(ViewUtils.hitFore);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(ViewUtils.hitBorder);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(0, 0, getWidth(), getHeight());
        g2.drawLine(getWidth(), 0, 0, getHeight());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            board.hit(x, y);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isEnabled()) setBorder(new LineBorder(Color.GREEN.darker(), 3));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (isEnabled()) setBorder(new LineBorder(ViewUtils.plainBorder, 1));
    }
}
