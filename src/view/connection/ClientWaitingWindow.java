package view.connection;

import logic.communication.IClient;
import logic.communication.Messenger;
import view.ViewUtils;
import view.mainframe.MainGameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWaitingWindow extends JFrame implements ActionListener, IClient {
    private Messenger messenger;
    private String name;

    public ClientWaitingWindow(String address, String port, String name) {
        super("Please wait");
        setSize(300, 300);
        ViewUtils.makeCenter(this);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel("Waiting for the host to join..."));
        getContentPane().add(messagePane);

        JPanel buttonPane = new JPanel();
        JButton button = new JButton("Cancel");
        buttonPane.add(button);
        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        this.name = name;
        try {
            this.messenger = new Messenger(address, Integer.parseInt(port), name, this);
            messenger.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect.", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            new ConnectionWindow();
        }

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            messenger.sendReject();
            new ConnectionWindow();
            this.dispose();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void onReject() {
        JOptionPane.showMessageDialog(this, "Rejected");
        this.dispose();
        new ConnectionWindow();
    }

    @Override
    public void onStart(String opName) {
        messenger.setGameWindow(new MainGameFrame(messenger, this.name, opName, false));
        this.dispose();
    }
}
