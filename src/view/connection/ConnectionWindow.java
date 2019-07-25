package view.connection;

import view.ViewUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectionWindow extends JFrame {
    private JTextField nameField, portField, ipField;
    private boolean isHost;
    private String myIP;

    public ConnectionWindow() {
        super("Online Battleship");
        setLayout(null);
        setSize(360, 300);
        ViewUtils.makeCenter(this);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            myIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            myIP = "cannot obtain ip";
        }

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 30, 40, 30);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(80, 30, 250, 30);
        add(nameField);

        isHost = true;
        JRadioButton hostRadio = new JRadioButton("Host", true);
        hostRadio.setBounds(20, 80, 60, 30);
        hostRadio.addItemListener(new RadioHandler(true));
        add(hostRadio);
        JRadioButton guestRadio = new JRadioButton("Guest", false);
        guestRadio.setBounds(180, 80, 60, 30);
        guestRadio.addItemListener(new RadioHandler(false));
        add(guestRadio);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(hostRadio);
        radioGroup.add(guestRadio);

        JLabel ipLabel = new JLabel("IP:");
        ipLabel.setBounds(20, 120, 20, 30);
        add(ipLabel);
        ipField = new JTextField(myIP);
        ipField.setBounds(40, 120, 140, 30);
        ipField.setEnabled(false);
        add(ipField);

        Starter startAction = new Starter();

        JLabel portLabel = new JLabel("Port:");
        portLabel.setBounds(200, 120, 40, 30);
        add(portLabel);
        portField = new JTextField();
        portField.setBounds(250, 120, 80, 30);
        portField.addActionListener(startAction);
        add(portField);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(250, 180, 80, 30);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        JButton startButton = new JButton("Start");
        startButton.setBounds(150, 180, 80, 30);
        startButton.addActionListener(startAction);
        add(startButton);

        setVisible(true);
    }


    private class RadioHandler implements ItemListener {
        private boolean state;

        RadioHandler(boolean s) {
            state = s;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            isHost = state;
            if (isHost) {
                ipField.setEnabled(false);
                ipField.setText(myIP);
            } else {
                ipField.setEnabled(true);
                ipField.setText("");
            }
        }
    }

    private class Starter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (nameField.getText().isEmpty() || portField.getText().isEmpty() || ipField.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "Please fill the missing fields.", "invalid input", JOptionPane.ERROR_MESSAGE);
            else if (Integer.parseInt(portField.getText()) < 2000)
                JOptionPane.showMessageDialog(null, "Port number must be greater than 2000.", "invalid port", JOptionPane.ERROR_MESSAGE);
            else {
                if (isHost) new HostWaitingWindow(portField.getText(), nameField.getText());
                else new ClientWaitingWindow(ipField.getText(), portField.getText(), nameField.getText());
                ConnectionWindow.this.dispose();
            }
        }
    }
}
