package view.manage;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;

abstract public class ManagePanel extends JPanel {
    JPanel leftShipSchema, rightShipSchema;


    public ManagePanel() {
        super(null);
        setSize(600, 180);
        setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

        rightShipSchema = new JPanel(new GridLayout(4,1, 0, 5));
        rightShipSchema.setBounds(260, 10, 200, 160);

        leftShipSchema = new JPanel(new GridLayout(4, 1, 0, 5));
        leftShipSchema.setBounds(20, 10, 200, 160);
        add(leftShipSchema);
    }
}
