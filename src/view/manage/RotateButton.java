package view.manage;

import view.boards.MyBoard;
import view.boards.ShipPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RotateButton extends JButton {

    public RotateButton(MyBoard field) {
        super(new ImageIcon("assets\\rotate.png"));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setSize(40, 40);
        setFocusable(false);

        KeyStroke keyRotate = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK);
        Action performRotate = new AbstractAction("Rotate") {
            public void actionPerformed(ActionEvent e) {
                ShipPane ship = field.getFocusedShip();
                if (ship == null) return;
                ship.rotate();
            }
        };
        getActionMap().put("performRotate", performRotate);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyRotate, "performRotate");
        addActionListener(performRotate);
    }
}
