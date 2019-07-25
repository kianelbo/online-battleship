package view.manage;

import view.ViewUtils;

import javax.swing.*;
import java.awt.*;

public class ShipIcon extends JPanel {
    private JLabel[] blocks;
    private int length;
    private boolean destroyed;

    public ShipIcon(int n) {
        super(new GridLayout(1, n, 1, 0));
        length = n;
        destroyed = false;
        blocks = new JLabel[length];
        for (int i = 0; i < length; i++) {
            blocks[i] = new JLabel("     ");
            blocks[i].setOpaque(true);
            blocks[i].setBackground(ViewUtils.plainBorder);
            add(blocks[i]);
        }
        repaint();
    }

    public void destroy() {
        for (int i = 0; i < length; i++)
            blocks[i].setBackground(Color.RED);
        destroyed = true;
        repaint();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public int getLength() {
        return this.length;
    }
}
