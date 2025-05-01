package a;

import javax.swing.*;
import java.awt.*;


public class TestGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Window");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JLabel("GUI is working!"));
            frame.setVisible(true);
        });
    }
}
