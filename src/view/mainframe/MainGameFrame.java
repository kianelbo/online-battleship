package view.mainframe;

import logic.communication.Messenger;
import logic.gameplay.BoardMap;
import view.ViewUtils;
import view.boards.MyBoard;
import view.boards.EnemyBoard;
import view.chat.ChatPanel;
import view.connection.ConnectionWindow;
import view.manage.ManagePanel;
import view.manage.PreparationPanel;
import view.manage.StatusPanel;

import javax.swing.*;
import java.io.IOException;

public class MainGameFrame extends JFrame {
    private Messenger messenger;
    private BoardContainer boardContainer;
    private BoardMap myMap;
    private MyBoard myBoard;
    private EnemyBoard enemyBoard;
    private ManagePanel bottomPanel;
    private String myName, opName;
    private boolean isMyTurn;
    private volatile boolean enemyReady;

    public MainGameFrame(Messenger messenger, String myName, String opName, boolean turn) {
        super("Online Battleship");
        setSize(900, 756);
        ViewUtils.makeCenter(this);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(null);

        this.messenger = messenger;

        this.myName = myName;
        this.opName = opName;
        isMyTurn = turn;
        enemyReady = false;

        myBoard = new MyBoard();
        boardContainer = new BoardContainer(myBoard);
        boardContainer.setLocation(1, 1);
        add(boardContainer);

        ChatPanel chatPanel = new ChatPanel(opName, messenger);
        chatPanel.setLocation(601, 1);
        add(chatPanel);

        bottomPanel = new PreparationPanel(boardContainer, this);
        bottomPanel.setLocation(1, 510);
        add(bottomPanel);

        setVisible(true);
    }

    public void finalizeArrange(BoardMap map) {
        boardContainer.setBoard(null, "Ready! Waiting for opponent...");

        try {
            messenger.sendReady();
        } catch (IOException ignored) {
        }
        myMap = map;
        myBoard.confirmArrange(myMap);
        enemyBoard = new EnemyBoard(this);
        boardContainer.repaint();

        new Thread(() -> {
            while (!enemyReady) ;
            if (isMyTurn)
                boardContainer.setBoard(enemyBoard, "My Turn");
            else
                boardContainer.setBoard(null, opName + "'s Turn");
        }).start();

        remove(bottomPanel);
        bottomPanel = new StatusPanel(myName, opName);
        bottomPanel.setLocation(1, 510);

        JButton leaveButton = new JButton("leave");
        leaveButton.setBounds(510, 140, 80, 30);
        leaveButton.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) try {
                messenger.sendLeave();
                MainGameFrame.this.dispose();
                new ConnectionWindow();
            } catch (IOException ignored) {
            }
        });
        bottomPanel.add(leaveButton);
        add(bottomPanel);
        repaint();
    }

    public void enemyIsReady() {
        enemyReady = true;
    }

    public int sendHit(int x, int y) throws IOException {
        int result = messenger.sendHit(x, y);
        int resultCode = result % 10;
        if (resultCode == 0)
            changeTurn();
        if (resultCode == 2)
            ((StatusPanel) bottomPanel).destroyIcon(false, result / 10);
        if (resultCode == 3) new Thread(() -> {
            try {
                Thread.sleep(500);
                victory(true);
            } catch (InterruptedException ignored) {
            }
        }).start();
        return resultCode;
    }

    public int receiveHit(int x, int y) {
        myBoard.repaint();
        int result = myMap.checkHit(x, y);
        int resultCode = result % 10;
        if (resultCode == 0)
            changeTurn();
        if (resultCode == 2)
            ((StatusPanel) bottomPanel).destroyIcon(true, result / 10);
        if (resultCode == 3)
            new Thread(() -> victory(false)).start();

        return result;
    }

    public void onEnemyLeave() {
        JOptionPane.showMessageDialog(this, "Your opponent left!");
        this.dispose();
        new ConnectionWindow();
    }

    private void changeTurn() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }

            isMyTurn = !isMyTurn;
            if (isMyTurn)
                boardContainer.setBoard(enemyBoard, "My Turn");
            else
                boardContainer.setBoard(myBoard, opName + "'s Turn");
        }).start();
    }

    private void victory(boolean won) {
        if (won) {
            JOptionPane.showMessageDialog(this, "YOU WON :)", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            try {
                messenger.sendEnd();
            } catch (IOException ignored) {
            }
        } else {
            JOptionPane.showMessageDialog(this, "YOU LOST :(", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
        new ConnectionWindow();
        this.dispose();
    }
}
