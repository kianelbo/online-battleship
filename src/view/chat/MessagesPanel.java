package view.chat;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessagesPanel extends JScrollPane {
    private JPanel contentPane;
    private int messageYPosition, messageCount;
    private Dimension scrollDimension;

    public MessagesPanel(JPanel contentPane) {
        super(contentPane);
        messageYPosition = 10;
        messageCount = 0;

        this.contentPane = contentPane;
        scrollDimension = new Dimension(300, 575);
        contentPane.setPreferredSize(scrollDimension);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setBounds(1, 55, 299, 575);
    }

    public void newMessage(String text, String sender) {
        messageCount++;
        MessageBox messageBox = new MessageBox(text, sender);
        messageBox.setLocation((sender.equals("Me") ? 10 : 50), messageYPosition);
        scrollDimension.height = Math.max(575, messageCount * 120);
        contentPane.add(messageBox);
        contentPane.repaint();
        revalidate();
        SwingUtilities.invokeLater(() -> getVerticalScrollBar().setValue(getVerticalScrollBar().getMaximum()));
        messageYPosition += 120;
    }

    private class MessageBox extends JPanel {

        private MessageBox(String text, String sender) {
            super(null);
            setSize(220, 70);
            setBorder(new EtchedBorder(EtchedBorder.LOWERED));

            JLabel nameLabel = new JLabel();
            if (sender.equals("Me")) nameLabel.setText("Me:");
            else {
                nameLabel.setText(":" + sender);
                nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            nameLabel.setBounds(5, 1, 210, 20);
            nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14));
            add(nameLabel);

            JLabel contentLabel = new JLabel(text);
            contentLabel.setBounds(5, 25, 210, 20);
            contentLabel.setFont(contentLabel.getFont().deriveFont(Font.BOLD, 16));
            add(contentLabel);

            JLabel timeLabel = new JLabel(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), SwingConstants.RIGHT);
            timeLabel.setBounds(5, 50, 210, 20);
            add(timeLabel);
        }
    }

}
