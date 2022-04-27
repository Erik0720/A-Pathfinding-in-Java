package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Gui {
    private int size;
    private String name;
    private boolean hasStart;
    private boolean hasEnd;


    public Gui(int size, String name) {
        this.size = size;
        this.name = name;
    }

    public void createGUI(int size) {
        JFrame frame = new JFrame();
        frame.setSize(800,900);
        frame.setTitle(name);
        frame.setLocationRelativeTo(null);      //Fenster erscheint Relativ zur Mitte des Bildschirms
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);      //Programm wird durch schließen des Fensters beendet
        frame.setResizable(false);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(size, size));
        gridPanel.setSize(800,800);

        Button[][] grid = new Button[size][size];       //Größe des zu visualisierenden Arrays
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {          //Initialisierung des Arrays
                grid[i][j] = new Button();
                grid[i][j].setBackground(Color.WHITE);
                grid[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Button button = (Button) e.getSource();

                        if(e.getModifiers() == 0) {
                            Color background = button.getBackground();

                            if (Color.BLACK.equals(background)) {
                                button.setBackground(Color.WHITE);
                            } else if (Color.BLUE.equals(background)) {
                                hasEnd = false;
                                button.setBackground(Color.BLACK);
                            } else if (Color.CYAN.equals(background)) {
                                hasStart = false;
                                button.setBackground(Color.BLACK);
                            } else if (Color.WHITE.equals(background)) {
                                button.setBackground(Color.BLACK);
                            }
                        }

                        else {
                            if(button.getBackground() != Color.BLUE && button.getBackground() != Color.CYAN) {
                                if(!hasStart) {
                                    hasStart = true;
                                    button.setBackground(Color.CYAN);

                                }
                                else if(!hasEnd) {
                                    hasEnd = true;
                                    button.setBackground(Color.BLUE);
                                }
                                System.out.println(e.getModifiers());
                            }
                        }
                    }

                });

                grid[i][j].setBackground(Color.WHITE);
                gridPanel.add(grid[i][j]);
            }
        }

        Button send = new Button("send");
        send.setSize(100,40);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pathfinder pathfinder = new Pathfinder(grid);
                try {
                    pathfinder.findPath();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }


            }
        });

        JPanel borderPanel = new JPanel();
        borderPanel.add(gridPanel);
        borderPanel.setLayout(new BorderLayout());
        borderPanel.add(send, BorderLayout.SOUTH);
        frame.add(borderPanel);
        frame.setVisible(true);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Gui{" + "size=" + size + ", name='" + name + '\'' + '}';
    }
}
