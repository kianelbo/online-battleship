package view.mainframe;

import view.boards.MyBoard;
import view.boards.BaseBoard;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BoardContainer extends JPanel {
    private JLabel topPlate;
    private BaseBoard board;

    public BoardContainer(MyBoard arrangeBoard) {
        super(null);
        setSize(600, 510);

        topPlate = new JLabel("Please arrange your field:");
        topPlate.setFont(new Font(topPlate.getFont().getName(), Font.PLAIN, 24));
        topPlate.setBounds(15, 15, 500, 40);
        add(topPlate);

        JPanel topAddressBar = new JPanel(new GridLayout(1, 10));
        topAddressBar.setBounds(100, 50, 400, 40);
        for (int i = 1; i < 11; i++)
            topAddressBar.add(getAddressCell((char) (i + 64)));
        add(topAddressBar);

        JPanel leftAddressBar = new JPanel(new GridLayout(10, 1));
        leftAddressBar.setBounds(60, 90, 40, 400);
        for (int j = 1; j < 11; j++)
            leftAddressBar.add(getAddressCell(j));
        add(leftAddressBar);

        board = arrangeBoard;
        board.setLocation(100, 90);
        add(board);

        setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(), LineBorder.createBlackLineBorder()));
    }

    public BaseBoard getBoard() {
        return this.board;
    }

    public void setBoard(BaseBoard b, String whoseTurn) {
        topPlate.setText(whoseTurn);
        if (b != null) {                    // unchanged board
            remove(board);
            this.board = b;
            board.setLocation(100, 90);
            add(board);

        }
        revalidate();
        repaint();
    }

    private JLabel getAddressCell(char c) {
        return new JLabel(String.valueOf(c), SwingConstants.CENTER);
    }

    private JLabel getAddressCell(int n) {
        return new JLabel(String.valueOf(n), SwingConstants.CENTER);
    }
}
