package logic.communication;

import view.chat.ChatPanel;
import view.mainframe.MainGameFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Stack;

public class Messenger extends Thread {
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private String myName;
    private IClient clientWindow;
    private MainGameFrame gameWindow;
    private ChatPanel chatPanel;
    private Stack<String> rcvHitStack = new Stack<>();

    public Messenger(String address, int port, String name, IClient clientWindow) throws IOException {
        this.clientWindow = clientWindow;
        socket = new Socket(address, port);
        this.myName = name;
        outStream = new DataOutputStream(socket.getOutputStream());
        inStream = new DataInputStream(socket.getInputStream());

        outStream.writeUTF(this.myName + " (" + socket.getLocalAddress() + ":" + socket.getLocalPort() + ")");
        outStream.flush();
    }

    public Messenger(Socket socket, String name, IClient serverWindow) throws IOException {
        this.clientWindow = serverWindow;
        this.socket = socket;
        this.myName = name;

        outStream = new DataOutputStream(socket.getOutputStream());
        inStream = new DataInputStream(socket.getInputStream());
    }

    public void setGameWindow(MainGameFrame gameWindow) {
        this.gameWindow = gameWindow;
    }

    public void setChatPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
    }

    public String getOpponent() throws IOException {
        return inStream.readUTF();
    }

    @Override
    public void run() {
        String received = "";
        while (!received.equals("{END}")) {
            try {
                received = inStream.readUTF();
            } catch (IOException ignored) {
            }

            if (received.startsWith("{HIT}")) {
                String[] recvArgs = received.split(" ");
                int result = gameWindow.receiveHit(Integer.parseInt(recvArgs[1]), Integer.parseInt(recvArgs[2]));
                try {
                    outStream.writeUTF("{DID} " + result);
                    outStream.flush();
                } catch (IOException ignored) {
                }
            }
            else if (received.startsWith("{DID}"))
                rcvHitStack.push(received);
            else if (received.startsWith("{MSG}"))
                chatPanel.receiveMessage(received.substring(received.indexOf('}') + 1));
            else if (received.startsWith("{START}"))
                clientWindow.onStart(received.substring(received.indexOf('}') + 1));
            else if (received.equals("{READY}"))
                gameWindow.enemyIsReady();
            else if (received.equals("{LEAVE}")) {
                gameWindow.onEnemyLeave();
                break;
            } else if (received.equals("{REJECT}")) {
                clientWindow.onReject();
                break;
            }
        }

        try {
            inStream.close();
            outStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendStart(String name) throws IOException {
        outStream.writeUTF("{START}" + name);
        outStream.flush();
    }

    public void sendEnd() throws IOException {
        outStream.writeUTF("{END}");
        outStream.flush();

        inStream.close();
        outStream.close();
        socket.close();
    }

    public void sendLeave() throws IOException {
        outStream.writeUTF("{LEAVE}");
        outStream.flush();

        inStream.close();
        outStream.close();
        socket.close();
    }

    public void sendReject() throws IOException {
        outStream.writeUTF("{REJECT}");
        outStream.flush();

        inStream.close();
        outStream.close();
        socket.close();
    }

    public void sendReady() throws IOException {
        outStream.writeUTF("{READY}");
        outStream.flush();
    }

    public void sendChat(String text) {
        try {
            outStream.writeUTF("{MSG}" + text);
            outStream.flush();
        } catch (IOException ignored) {
        }
    }

    public int sendHit(int x, int y) throws IOException {
        outStream.writeUTF("{HIT} " + x + " " + y);
        outStream.flush();

        while (rcvHitStack.empty()) ;
        String received = rcvHitStack.pop();
        return Integer.parseInt(received.split(" ")[1]);
    }
}
