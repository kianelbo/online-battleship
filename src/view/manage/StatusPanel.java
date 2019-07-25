package view.manage;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class StatusPanel extends ManagePanel {
    private ArrayList<ShipIcon> myIcons, opIcons;

    public StatusPanel(String myName, String opName) {
        super();

        JPanel[] shipIconPanels = new JPanel[4];
        myIcons = new ArrayList<>();
        opIcons = new ArrayList<>();

        // my status
        leftShipSchema.setBorder(new TitledBorder(myName));
        for (int i = 0; i < 4; i++) {
            shipIconPanels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            leftShipSchema.add(shipIconPanels[i]);
        }
        for (int l = 4; l > 0; l--) {
            for (int c = 0; l + c < 5; c++) {
                ShipIcon icon = new ShipIcon(l);
                myIcons.add(icon);
                shipIconPanels[4 - l].add(icon);
            }
        }

        // enemy status
        add(rightShipSchema);
        rightShipSchema.setBorder(new TitledBorder(opName));
        for (int i = 0; i < 4; i++) {
            shipIconPanels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            rightShipSchema.add(shipIconPanels[i]);
        }
        for (int l = 4; l > 0; l--) {
            for (int c = 0; l + c < 5; c++) {
                ShipIcon icon = new ShipIcon(l);
                opIcons.add(icon);
                shipIconPanels[4 - l].add(icon);
            }
        }
        rightShipSchema.repaint();
    }

    public void destroyIcon(boolean me, int l) {
        for (ShipIcon sh : (me ? myIcons : opIcons))
            if (sh.getLength() == l && !sh.isDestroyed()) {
                sh.destroy();
                return;
            }
    }
}
