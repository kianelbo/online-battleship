package view.manage;

import view.boards.MyBoard;
import view.boards.ShipPane;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ShipMaker extends JPanel {
    private JLabel countLabel;
    private int count;
    private final int size;
    private MyBoard arrangeBoard;

    public ShipMaker(MyBoard board, int n) {
        super(null);
        size = n;
        count = 5 - size;
        arrangeBoard = board;
        setSize(200, 30);

        JButton createButton = new JButton();
        createButton.setBounds(0, 0, 40 * size, 30);
        createButton.setBackground(Color.WHITE);
        createButton.setBorder(new LineBorder(Color.BLACK, 3));
        createButton.addActionListener(e -> {
            if (count == 0) return;
            ShipPane ship = new ShipPane(size, arrangeBoard);
            arrangeBoard.addShip(ship);
            arrangeBoard.revalidate();

            count--;
            countLabel.setText("×" + count);
            countLabel.repaint();
        });
        add(createButton);

        countLabel = new JLabel("×" + count);
        countLabel.setBounds(170, 0, 30, 25);
        add(countLabel);
    }
}
