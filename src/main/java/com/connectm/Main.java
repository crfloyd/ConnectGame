package com.connectm;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Set FlatLaf for a modern look.
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Connect M");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create the board panel with headers (e.g., 7 columns, 80px cell, 100px header).
            BoardPanelWithHeaders boardPanel = new BoardPanelWithHeaders(7, 80, 110);

            // Wrap the board panel in a container that centers it both horizontally and vertically.
            // We use BoxLayout with vertical and horizontal glue.
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.setBackground(Color.WHITE);

            centerPanel.add(Box.createVerticalGlue());

            JPanel horizontalPanel = new JPanel();
            horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.X_AXIS));
            horizontalPanel.setBackground(Color.WHITE);
            horizontalPanel.add(Box.createHorizontalGlue());
            horizontalPanel.add(boardPanel);
            horizontalPanel.add(Box.createHorizontalGlue());

            centerPanel.add(horizontalPanel);
            centerPanel.add(Box.createVerticalGlue());

            frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
