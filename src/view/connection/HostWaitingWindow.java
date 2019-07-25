package view.connection;

import logic.communication.IClient;
import logic.communication.Messenger;
import logic.communication.ServerHandler;
import view.ViewUtils;
import view.mainframe.MainGameFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class HostWaitingWindow extends JFrame implements IClient {
    private JPanel connectionsPane;
    private ServerHandler serverHandler;
    private String name;

    public HostWaitingWindow(String port, String name) {
        super("Waiting for connections...");
        setSize(300, 600);
        ViewUtils.makeCenter(this);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.name = name;
        serverHandler = new ServerHandler(port, name, this);

        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, 300, 100);
        topPanel.add(new JLabel("Received Connections:"));
        JButton cancelButton = new JButton("cancel");
        cancelButton.addActionListener(e -> {
            serverHandler.endListening();
            new ConnectionWindow();
            this.dispose();
        });
        topPanel.add(cancelButton);
        add(topPanel, BorderLayout.NORTH);

        connectionsPane = new JPanel(new GridLayout(5, 1));
        connectionsPane.setBounds(0, 100, 300, 500);
        add(connectionsPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public void onConnectionReceived(String opDetails, Messenger messenger) {
        JPanel connectionPanel = new JPanel(new GridLayout(2, 1));
        connectionPanel.setBorder(new LineBorder(Color.BLACK, 2));
        connectionPanel.add(new JLabel("  " + opDetails), BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton acceptButton = new JButton("Accept");
        acceptButton.addActionListener(e -> {
            try {
                messenger.sendStart(this.name);
                serverHandler.endListening();

                messenger.setGameWindow(new MainGameFrame(messenger, name, opDetails.substring(0, opDetails.indexOf(' ')), true));
                this.dispose();
            } catch (IOException ignored) {
            }
        });
        buttonsPanel.add(acceptButton);
        JButton rejectButton = new JButton("Reject");
        rejectButton.addActionListener(e -> {
            try {
                connectionsPane.remove(connectionPanel);
                connectionsPane.revalidate();
                connectionsPane.repaint();
                messenger.sendReject();
            } catch (IOException ignored) {
            }
        });
        buttonsPanel.add(rejectButton);
        connectionPanel.add(buttonsPanel);
        connectionsPane.add(connectionPanel);
        connectionsPane.revalidate();
        connectionsPane.repaint();
    }

    public void onErrorOccurred(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onReject() {
        connectionsPane.removeAll();
        connectionsPane.revalidate();
    }

    @Override
    public void onStart(String opName) {
    }
}
