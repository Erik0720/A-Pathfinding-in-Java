package com.company;


import javax.swing.*;

public class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Gui gui = new Gui(40, "Visualization");
        gui.createGUI(40);
    }
}
