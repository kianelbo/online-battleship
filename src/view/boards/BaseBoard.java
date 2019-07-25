package view.boards;

import javax.swing.*;
import java.awt.*;

abstract public class BaseBoard extends JPanel {

    public BaseBoard() {
        super(null);
        setSize(400, 400);
        setBackground(Color.WHITE);
    }
}
