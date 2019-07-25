package view;

import java.awt.*;

public class ViewUtils {
    public static final Color plainBorder = new Color(0x7594E1),
            plainFore = new Color(0xDCEAFF),
            hitBorder = new Color(0xFF1C1B),
            hitFore = new Color(0xFFC7C7);

    public static void makeCenter(Window frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
    }
}
