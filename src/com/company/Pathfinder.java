package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Pathfinder {
    private Button[][] buttonGrid;
    private int size;
    private int startx;
    private int starty;
    private int endx;
    private int endy;
    private ArrayList<Node> closed = new ArrayList<>();
    private ArrayList<Node> open = new ArrayList<>();
    private Node[][] nodes;



    public Pathfinder(Button[][] grid) {
        this.buttonGrid = grid;
        this.size = buttonGrid.length;
        this.nodes = new Node[size][size];
    }

    public void setNodes() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if (Color.BLACK.equals(buttonGrid[i][j].getBackground())) {
                    nodes[i][j] = new Node(j, i, true, null);
                } else if (Color.WHITE.equals(buttonGrid[i][j].getBackground())) {
                    nodes[i][j] = new Node(j, i, false, null);
                }
            }
        }
    }

    public Node getStart() {
        for(int i = 0; i < buttonGrid.length; i++) {
            for(int j = 0; j < buttonGrid.length; j++) {
                if(Color.CYAN.equals(buttonGrid[i][j].getBackground())) {
                    nodes[i][j] = new Node(j, i, false, null);
                    startx = j;
                    starty = i;
                    return  nodes[i][j];
                }
            }
        }
        return null;
    }

    public Node getEnd() {
        for(int i = 0; i < buttonGrid.length; i++) {
            for(int j = 0; j < buttonGrid.length; j++) {
                if(Color.BLUE.equals(buttonGrid[i][j].getBackground())) {
                    nodes[i][j] = new Node(j, i, false, null);
                    endx = j;
                    endy = i;
                    return  nodes[i][j];
                }
            }
        }
        return null;
    }

    public Node getLowestDistance(ArrayList<Node> open) {
        Node lowestNode = open.get(0);

        for(int i = 0; i < open.size(); i++) {
            if(open.get(i).getDistanceToEnd() < lowestNode.getDistanceToEnd()) {
                lowestNode = open.get(i);
            }
        }
        System.out.println("lowest Node:" + lowestNode.getDistanceToEnd());
        System.out.println();
        System.out.println();
        return lowestNode;
    }

    public float shortestPathToEnd(int x, int y) {
        return 10 * (Math.abs(x - endx) + Math.abs(y - endy));
    }

    public Node findNodeByCoordinates(int x, int y) {
        if(x >= nodes.length || y >= nodes.length) return null;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(nodes[i][j].getyCord() == y && nodes[i][j].getxCord() == x) return nodes[i][j];
            }
        }
        return null;
    }

    public boolean isDiagonal(int x, int y){
        if(x != 0 && y != 0) return true;

        return false;
    }

    public float newDistanceToStart(float distanceToStart, boolean isDiagonal) {
        if(isDiagonal) return distanceToStart + 14;
        return distanceToStart + 10;
    }

    public ArrayList<Node> findPath() throws InterruptedException {
        setNodes();         //NodeArray initialisieren
        Node goal = getEnd();       //Ziel Node finden
        Node current = getStart();      //Start Node finden

        open.add(current);      //Start Node auf openList setzen
        while (!open.isEmpty()) {

            current = getLowestDistance(open);      //Node mit niedrigster Distanz zum Ziel aus OpenList finden
            open.remove(current);       //ausgewählte Node aus openList löschen
            closed.add(current);        //ausgewählte Node auf closedList setzen
            buttonGrid[current.getyCord()][current.getxCord()].setBackground(Color.RED);        //geschlossene Node in GUI Rot färben

            Thread.sleep(100);      //100 millisekunden warten

            if (current.equals(goal)) {         //überprüfen ob ausgewählte Node der Ziel Node entspricht
                ArrayList<Node> path = new ArrayList<>();
                while (current != null) {       //alle Nodes
                    path.add(current);
                    current = current.getOrigin();
                }
                visualizePath(path);
                return path;
            }

            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    int nextY = current.getyCord() + y;
                    int nextX = current.getxCord() + x;

                    if (findNodeByCoordinates(nextX, nextY) != null ) {
                        if (findNodeByCoordinates(nextX, nextY).getSolid() || closed.contains(nodes[nextY][nextX]))
                            continue;

                        float newDistanceToStart = newDistanceToStart(current.getDistanceToStart(), isDiagonal(x, y));
                        if (!open.contains(nodes[nextY][nextX])) {
                            if (nodes[nextY][nextX].equals(goal)) {
                                ArrayList<Node> path = new ArrayList<>();
                                while (current != null) {
                                    path.add(current);
                                    current = current.getOrigin();
                                }
                                visualizePath(path);
                                return path;
                            }
                            open.add(nodes[nextY][nextX]);
                            buttonGrid[nextY][nextX].setBackground(Color.GREEN);
                        } else if (newDistanceToStart >= nodes[nextY][nextX].getDistanceToStart()) continue;

                        nodes[nextY][nextX].setDistanceToStart(newDistanceToStart);
                        nodes[nextY][nextX].setDistanceToEnd(shortestPathToEnd(nextX,nextY));
                        nodes[nextY][nextX].setTotalDistance(nodes[nextX][nextY].getDistanceToStart() + nodes[nextX][nextY].getDistanceToEnd());
                        nodes[nextY][nextX].setOrigin(current);
                    }
                }
            }
        }
        return null;
    }

    private void visualizePath(ArrayList<Node> path) {
        for(int i = 0; i < path.size(); i++) {
            buttonGrid[path.get(i).getyCord()][path.get(i).getxCord()].setBackground(Color.CYAN);
        }
    }

    public Button[][] getGrid() {
        return buttonGrid;
    }

    public void setGrid(Button[][] grid) {
        this.buttonGrid = grid;
    }

    @Override
    public String toString() {
        return "Pathfinder{" +
                "grid=" + Arrays.toString(buttonGrid) +
                '}';
    }
}
