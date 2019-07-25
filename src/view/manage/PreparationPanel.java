package view.manage;

import view.boards.MyBoard;
import view.mainframe.BoardContainer;
import view.mainframe.MainGameFrame;

import javax.swing.*;

public class PreparationPanel extends ManagePanel {
    private ShipMaker[] shipMakers;

    public PreparationPanel(BoardContainer boardContainer, MainGameFrame frame) {
        super();
        MyBoard field = (MyBoard) boardContainer.getBoard();
        RotateButton rotateButton = new RotateButton(field);
        rotateButton.setLocation(550, 10);
        add(rotateButton);

        ReadyButton readyButton = new ReadyButton(field, frame);
        add(readyButton);

        rightShipSchema = null;

        shipMakers = new ShipMaker[4];
        for (int i = 0; i < 4; i++) {
            shipMakers[i] = new ShipMaker(field, 4 - i);
            leftShipSchema.add(shipMakers[i]);
        }


        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(425, 140, 80, 30);
        resetButton.addActionListener(e -> {
            field.reset();
            for (int i = 0; i < 4; i++) {
                leftShipSchema.remove(shipMakers[i]);
                shipMakers[i] = new ShipMaker(field, 4 - i);
                leftShipSchema.add(shipMakers[i]);
            }
            leftShipSchema.revalidate();
        });
        add(resetButton);
    }
}
