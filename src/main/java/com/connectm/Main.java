package com.connectm;

import com.connectm.controller.GameController;
import com.connectm.model.GameState;
import com.connectm.view.ConnectMView;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (args.length < 3) {
                System.out.println("Usage: java -jar ConnectM.jar <N> <M> <H>");
                System.exit(1);
            }

            int boardSize = Integer.parseInt(args[0]);
            int M = Integer.parseInt(args[1]);
            int firstPlayer = Integer.parseInt(args[2]);

            // Validate inputs
            if (boardSize < 3 || boardSize > 10 || M < 2 || M > boardSize || (firstPlayer != 0 && firstPlayer != 1)) {
                System.out.println("Invalid parameters. Ensure:");
                System.out.println(" 3 ≤ N ≤ 10, 2 ≤ M ≤ N, H is 0 (AI first) or 1 (Human first)");
                System.exit(1);
            }

            GameState gameState = new GameState(boardSize, M, firstPlayer);

            // Create the view with cell size 80 and header size 100.
            ConnectMView view = new ConnectMView(gameState, 80, 100);

            // Create the controller.
            GameController controller = new GameController(gameState, view);

            // Create a wrapper panel with GridBagLayout to center the view.
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setBackground(Color.WHITE);
            // Adding an empty border ensures equal margins around the view.
            wrapper.setBorder(new EmptyBorder(50, 50, 50, 50));

            // Add the view to the wrapper.
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            // Do not stretch the view.
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            // Give extra space so that the wrapper can expand.
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            wrapper.add(view, gbc);

            // Create the frame and add the wrapper in the center.
            JFrame frame = new JFrame("Connect M");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(wrapper, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
