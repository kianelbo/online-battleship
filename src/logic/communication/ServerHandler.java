package logic.communication;

import view.connection.HostWaitingWindow;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerHandler implements Runnable {
    private ServerSocket serverSocket;
    private String name;
    private HostWaitingWindow serverWindow;
    private boolean isListening;

    public ServerHandler(String port, String name, HostWaitingWindow serverWindow) {
        this.name = name;
        this.serverWindow = serverWindow;
        isListening = false;

        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
            new Thread(this).start();
        } catch (IOException e) {
            serverWindow.onErrorOccurred(e.getMessage());
        }
    }

    public void endListening() {
        isListening = false;
    }

    @Override
    public void run() {
        isListening = true;
        while (isListening) {
            try {
                Messenger messenger = new Messenger(serverSocket.accept(), name, serverWindow);
                String opDetails = messenger.getOpponent();
                serverWindow.onConnectionReceived(opDetails, messenger);
                messenger.start();
            } catch (IOException e) {
                serverWindow.onErrorOccurred(e.getMessage());
                break;
            }
        }
        endListening();
    }
}
