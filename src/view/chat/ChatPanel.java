package view.chat;

import logic.communication.Messenger;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel implements ActionListener{
    private JTextField messageField;
    private MessagesPanel messagesPanel;
    private String opName;
    private Messenger messenger;

    public ChatPanel(String opName, Messenger messenger) {
        super(null);
        setSize(300, 760);

        this.opName = opName;
        this.messenger = messenger;
        messenger.setChatPanel(this);

        JLabel plate = new JLabel(" Chat to " + opName + ":");
        plate.setBounds(-5, -5, 305, 60);
        plate.setFont(new Font(plate.getFont().getName(), Font.PLAIN, 20));
        plate.setBorder(new LineBorder(Color.black, 4));
        add(plate);

        messageField = new JTextField();
        messageField.setBounds(0, 650, 250, 40);
        add(messageField);
        messageField.addActionListener(this);

        JButton sendButton = new JButton(new ImageIcon("assets\\send.png"));
        sendButton.setOpaque(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setBorderPainted(false);
        sendButton.setFocusPainted(false);
        setFocusable(false);
        sendButton.setBounds(250, 650, 50, 40);
        add(sendButton);
        sendButton.addActionListener(this);

        messagesPanel = new MessagesPanel(new JPanel(null));
        add(messagesPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = messageField.getText();
        messagesPanel.newMessage(text, "Me");
        messageField.setText("");
        messenger.sendChat(text);
    }

    public void receiveMessage(String text) {
        messagesPanel.newMessage(text, opName);
    }
}
