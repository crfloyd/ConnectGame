package com.connectm;

import com.connectm.controller.GameController;
import com.connectm.model.GameState;
import com.connectm.view.ConnectMView;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Entry point for the Connect M game application. Initializes the game with command-line arguments
 * and sets up the graphical user interface using Swing.
 */
public class Main {
    private static final int CELL_SIZE = 80;    // Pixel size of each grid cell
    private static final int HEADER_SIZE = 100; // Pixel size of the header area

    /**
     * Launches the Connect M game by validating arguments and initializing the UI on the Event Dispatch Thread.
     *
     * @param args Command-line arguments: N (board size), M (discs to connect), H (first player: 0=AI, 1=human)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Set up the modern look and feel
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                System.err.println("Failed to set look and feel: " + e.getMessage());
                e.printStackTrace();
            }

            // Validate and parse command-line arguments
            if (!validateArgs(args)) {
                System.exit(1);
            }
            int boardSize = Integer.parseInt(args[0]);
            int discsToWin = Integer.parseInt(args[1]);
            int firstPlayer = Integer.parseInt(args[2]);

            // Initialize game components
            GameState gameState = new GameState(boardSize, discsToWin, firstPlayer);
            ConnectMView view = new ConnectMView(gameState, CELL_SIZE, HEADER_SIZE);
            GameController controller = new GameController(gameState, view);

            // Set up the main window
            JFrame frame = createMainFrame(view);
            frame.setVisible(true);
        });
    }

    /**
     * Validates the command-line arguments for the game.
     *
     * @param args Array of arguments: [N, M, H]
     * @return true if arguments are valid (3 ≤ N ≤ 10, 2 ≤ M ≤ N, H = 0 or 1), false otherwise
     */
    private static boolean validateArgs(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java -jar ConnectM.jar <N> <M> <H>");
            return false;
        }

        try {
            int n = Integer.parseInt(args[0]);
            int m = Integer.parseInt(args[1]);
            int h = Integer.parseInt(args[2]);

            if (n < 3 || n > 10 || m < 2 || m > n || (h != 0 && h != 1)) {
                System.err.println("Invalid parameters. Ensure:");
                System.err.println("  3 ≤ N ≤ 10  (board size)");
                System.err.println("  2 ≤ M ≤ N  (discs to connect)");
                System.err.println("  H = 0 (AI first) or 1 (Human first)");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.err.println("Arguments must be integers: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates and configures the main JFrame for the game, centering the view within it.
     *
     * @param view The ConnectMView component to display in the frame
     * @return The configured JFrame ready to be shown
     */
    private static JFrame createMainFrame(ConnectMView view) {
        JFrame frame = new JFrame("Connect M");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Center the view with padding
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        wrapper.add(view, gbc);

        frame.add(wrapper, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on screen
        return frame;
    }
}