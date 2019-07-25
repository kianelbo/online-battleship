package logic.gameplay;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardMap {
    private boolean[][] revealed;
    private Ship[][] ownerShip;
    private ArrayList<Ship> shipsList;
    private ArrayList<Point> sunkenCells;

    public BoardMap() {
        revealed = new boolean[10][10];
        for (int i = 0; i < 10; i++) for (int j = 0; j < 10; j++) revealed[i][j] = false;

        ownerShip = new Ship[10][10];
        shipsList = new ArrayList<>();
        sunkenCells = new ArrayList<>();
    }

    public void addShip(Point[] points) {
        Ship ship = new Ship(points);
        shipsList.add(ship);

        for (Point p : points)
            ownerShip[p.x][p.y] = ship;
    }

    public ArrayList<Point> getSunkenCells() {
        return sunkenCells;
    }

    public int checkHit(int x, int y) {
        revealed[x][y] = true;
        int hitStatus = 0;                                  // nothing hit

        if (ownerShip[x][y] != null) {
            hitStatus++;                                    // cell hit
            sunkenCells.add(new Point(x, y));

            int completeDestroyStatus = ownerShip[x][y].completelyDestroyed();
            if (completeDestroyStatus > 0) {
                hitStatus++;                                // ship completely destroyed
                hitStatus += 10 * completeDestroyStatus;    // size of destroyed ship

                if (allShipsDestroyed())
                    hitStatus++;                            // all ships destroyed (lose!)
            }
        }

        return hitStatus;
    }

    private boolean allShipsDestroyed() {
        for (Ship sh : shipsList)
            if (sh.completelyDestroyed() == 0)
                return false;
        return true;
    }

    private class Ship {
        private ArrayList<Point> cells;

        Ship(Point[] points) {
            cells = new ArrayList<>();
            cells.addAll(Arrays.asList(points));
        }

        private int completelyDestroyed() {
            for (Point p : cells)
                if (!revealed[p.x][p.y])
                    return 0;
            return cells.size();
        }
    }
}
