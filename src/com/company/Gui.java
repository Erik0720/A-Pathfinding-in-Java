package com.company;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GUI extends JFrame {

    private int spacing = 4;
    private int width = 766;
    private int height = 850;
    private int mx;
    private int my;
    private boolean isDragged;
    private boolean isClicked;
    private boolean blocked;
    private boolean started;
    private boolean finished;
    private boolean erased;
    private boolean hasStart;
    private boolean hasEnd;
    private Color[][] grid = new Color[25][25];

    public GUI() {
        this.setTitle("Visualizer");
        this.setSize(width, height);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);

        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                grid[i][j] = Color.GRAY;
            }
        }
    }

    public class Board extends JPanel {

        public void paintComponent(Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.LIGHT_GRAY);

            Button erase = new Button("Erase");
            Button block = new Button("Block");
            Button start = new Button("Start");
            Button finish = new Button("Finish");
            Button send = new Button("Send");
            this.add(erase);
            this.add(block);
            this.add(start);
            this.add(finish);
            this.add(send);
            block.setBounds(50, 752, 45, 26);
            erase.setBounds(200, 752, 45, 26);
            start.setBounds(350, 752, 45, 26);
            finish.setBounds(500, 752, 45, 26);
            send.setBounds(650, 752, 45, 26);
            block.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blocked = true;
                    erased = false;
                    started = false;
                    finished = false;
                }
            });
            erase.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blocked = false;
                    erased = true;
                    started = false;
                    finished = false;
                }
            });
            start.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blocked = false;
                    erased = false;
                    started = true;
                    finished = false;
                }
            });
            finish.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blocked = false;
                    erased = false;
                    started = false;
                    finished = true;
                }
            });
            send.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Pathfinder(grid)).start();
                    send.removeActionListener(this);
                }
            });

            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 25; j++) {
                    g.setColor(grid[i][j]);
                    if (mx >= spacing + i * 30 + 2 * spacing && mx < spacing + i * 30 + 30 && my >= spacing + j * 30 + 30 && my < spacing + j * 30 + 30 - 2 * spacing + 30) {  //überprüfung ob Maus im richtigen Quadrat ist
                        mouseHandling(i, j, g);
                    }
                    g.fillRect(spacing + i * 30, spacing + j * 30, 30 - 2 * spacing, 30 - 2 * spacing);     //fülle das Feld mit neuer Farbe

                }
            }
        }

        public void restartScreen() {

        }
    }

    private void mouseHandling(int i, int j, Graphics g) {
        if (isDragged || isClicked) {
            if (erased) {
                if (grid[i][j] == Color.BLACK) {
                    grid[i][j] = Color.GRAY;
                    g.setColor(grid[j][i]);
                }
            } else if (blocked) {
                if (grid[i][j] != Color.CYAN && grid[i][j] != Color.BLUE) {
                    grid[i][j] = Color.BLACK;
                    g.setColor(grid[j][i]);
                }
            } else if (started) {
                if(!hasStart) {
                    if (grid[i][j] == Color.GRAY || grid[i][j] == Color.BLACK) {
                        grid[i][j] = Color.CYAN;
                        g.setColor(grid[j][i]);
                        hasStart = true;
                    }
                }
            } else if (finished) {
                if(!hasEnd) {
                    if (grid[i][j] == Color.GRAY || grid[i][j] == Color.BLACK) {
                        grid[i][j] = Color.BLUE;
                        g.setColor(grid[i][j]);
                        hasEnd = true;
                    }
                }
            } else g.setColor(Color.LIGHT_GRAY);
            isClicked = false;
            isDragged = false;

        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            mx = e.getX();      //x-koordinate des Cursors
            my = e.getY();      //Y-koordinate des Cursors
            isDragged = true;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mx = e.getX();
            my = e.getY();
        }
    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            isClicked = true;
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public class Pathfinder implements Runnable{
        private int size = 25;
        private int startx;
        private int starty;
        private int endx;
        private int endy;
        private int waitingTime = 200;
        private ArrayList<Node> closed = new ArrayList<>();
        private ArrayList<Node> open = new ArrayList<>();
        private Node[][] nodes = new Node[size][size];



        public Pathfinder(Color[][] grid)  {
        }

        public void setNodes() {
            for(int i = 0; i < size; i++) {
                for(int j = 0; j < size; j++) {
                    if (Color.BLACK.equals(grid[i][j])) {
                        nodes[i][j] = new Node(j, i, true, null);
                    } else if (Color.GRAY.equals(grid[i][j])) {
                        nodes[i][j] = new Node(j, i, false, null);
                    }
                }
            }
        }

        public Node getStart() {
            for(int i = 0; i < grid.length; i++) {
                for(int j = 0; j < grid.length; j++) {
                    if(Color.CYAN.equals(grid[i][j])) {
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
            for(int i = 0; i < grid.length; i++) {
                for(int j = 0; j < grid.length; j++) {
                    if(Color.BLUE.equals(grid[i][j])) {
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

            for (Node node : open) {
                if (node.getTotalDistance() < lowestNode.getTotalDistance()) {
                    lowestNode = node;
                }
            }
            return lowestNode;
        }

        public float shortestPathToEnd(int x, int y) {
            //return 10 * (Math.abs(x - endx) + Math.abs(y - endy));
            return (float)(10 * Math.sqrt((x - endx)*(x - endx) + (y - endy)*(y - endy)));
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
            return x != 0 && y != 0;
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
                grid[current.getyCord()][current.getxCord()] = Color.RED;        //geschlossene Node in GUI Rot färben
                setNodes();


                Thread.sleep(waitingTime);      //100 millisekunden warten

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
                        setNodes();

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
                                grid[nextY][nextX] = Color.GREEN;
                                System.out.println();
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
                grid[path.get(i).getyCord()][path.get(i).getxCord()] = Color.CYAN;
            }
        }

        @Override
        public String toString() {
            return "Pathfinder{" +
                    "grid=" + Arrays.toString(grid) +
                    '}';
        }

        @Override
        public void run() {
            try {
                findPath();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}