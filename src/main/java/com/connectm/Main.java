package com.connectm;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Connect M");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);

            // Create our custom board panel with headers.
            BoardPanelWithHeaders boardPanel = new BoardPanelWithHeaders(7, 80, 30);

            // Wrap in a panel that centers the board.
            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setBackground(Color.WHITE);
            centerPanel.add(boardPanel);

            frame.add(centerPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}