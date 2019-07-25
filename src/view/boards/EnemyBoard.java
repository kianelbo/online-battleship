package view.boards;

import view.mainframe.MainGameFrame;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


public class EnemyBoard extends BaseBoard {
    private MainGameFrame mainFrame;
    private Cell[][] cells;

    public EnemyBoard(MainGameFrame mainFrame) {
        super();

        this.mainFrame = mainFrame;

        cells = new Cell[10][10];
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 10; y++) {
                cells[x][y] = new Cell(this, x, y);
                cells[x][y].setBounds(x * 40, y * 40, 40, 40);
                add(cells[x][y]);
            }
    }

    public void hit(int x, int y) throws IOException {
        switch (mainFrame.sendHit(x, y)) {
            case 0:         // hit empty cell
                cells[x][y].reveal(false);
                break;
            case 1:         // hit ship cell
                cells[x][y].reveal(true);
                break;
            case 2:         // ship completely destroyed
                cells[x][y].reveal(true);
                ArrayList<Point> shipCells = new ArrayList<>();
                shipCells.add(new Point(x, y));
                findShipCell(shipCells, x, y, +1, 0);
                findShipCell(shipCells, x, y, -1, 0);
                findShipCell(shipCells, x, y, 0, +1);
                findShipCell(shipCells, x, y, 0, -1);
                for (Point shipCell : shipCells)
                    revealNeighbours(shipCell.x, shipCell.y);
        }
    }

    private void findShipCell(ArrayList<Point> list, int x, int y, int x_dir, int y_dir) {
        try {
            if (cells[x + x_dir][y + y_dir].isOccupied()) {
                list.add(new Point(x + x_dir, y + y_dir));
                findShipCell(list, x + x_dir, y + y_dir, x_dir, y_dir);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private void revealNeighbours(int x, int y) {
        try {
            cells[x - 1][y - 1].reveal(false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            cells[x][y - 1].reveal(false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            cells[x + 1][y - 1].reveal(false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            cells[x - 1][y].reveal(false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            cells[x + 1][y].reveal(false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            cells[x - 1][y + 1].reveal(false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            cells[x][y + 1].reveal(false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            cells[x + 1][y + 1].reveal(false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }
}
