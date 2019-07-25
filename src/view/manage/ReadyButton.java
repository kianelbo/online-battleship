package view.manage;

import logic.gameplay.BoardMap;
import view.boards.MyBoard;
import view.boards.ShipPane;
import view.mainframe.MainGameFrame;

import javax.swing.*;

public class ReadyButton extends JButton {
    private MyBoard arrangeBoard;
    private MainGameFrame mainFrame;

    public ReadyButton(MyBoard field, MainGameFrame frame) {
        super("Ready");
        setBounds(510, 140, 80, 30);

        this.arrangeBoard = field;
        this.mainFrame = frame;

        addActionListener(e -> {
            if (arrangeBoard.getShipPanes().size() != 10) {
                JOptionPane.showMessageDialog(null, "Add all of your ships to the field", "Arrangement is not complete", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BoardMap myMap = new BoardMap();
            for (ShipPane sh : arrangeBoard.getShipPanes())
                myMap.addShip(sh.getOccupiedCells());
            mainFrame.finalizeArrange(myMap);
        });
    }
}
