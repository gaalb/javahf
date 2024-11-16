package GBTAN;

import javax.swing.*;
import java.io.File;
import java.awt.*;

/**
 * Represents a dialog menu for selecting a player profile and game save file
 * before starting the game. It provides options to load existing profiles and
 * saves or proceed with default settings. Uses a card layout.
 */
public class PreGameMenu extends JDialog {
    /**
     * The selected player profile file. This will be null if no file is selected.
     */
    private File playerFile;

    /**
     * The selected game save file. This will be null if no file is selected.
     */
    private File saveFile;

    /**
     * Constructs the PreGameMenu dialog. It initializes the UI components
     * and manages the logic for selecting player profiles and save files.
     *
     * @param owner The parent frame of this dialog.
     */
    public PreGameMenu(Frame owner) {
        super(owner, true);
        setTitle("Profile Selection");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // First panel: Player profile selection
        JPanel firstPanel = new JPanel();
        JButton loadProfileButton = new JButton("Load Profile");
        JButton defaultProfileButton = new JButton("Default Profile");
        firstPanel.add(loadProfileButton);
        firstPanel.add(defaultProfileButton);

        // Second panel: Save file selection
        JPanel secondPanel = new JPanel();
        JButton loadSaveButton = new JButton("Load Save");
        JButton newGameButton = new JButton("New Game");
        secondPanel.add(loadSaveButton);
        secondPanel.add(newGameButton);

        cardPanel.add(firstPanel, "First");  // first card is the player profile choice window
        cardPanel.add(secondPanel, "Second"); // second card is the save load choice window

        add(cardPanel);
        pack();
        setLocationRelativeTo(null);

        // Action for loading a player profile
        loadProfileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("players");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                // if we managed to choose a player profile, proceed to the second window (second card)
                playerFile = fileChooser.getSelectedFile();
                cardLayout.show(cardPanel, "Second");
                setTitle("Save File Selection");
            }
        });

        // Action for selecting the default player profile
        defaultProfileButton.addActionListener(e -> {
            // if the user chose the default profile proceed to the second window (second card)
            playerFile = GameSettings.DEFAULT_PLAYER_FILE;
            cardLayout.show(cardPanel, "Second");
            setTitle("Save File Selection");
        });

        // Action for loading a game save
        loadSaveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("saves");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                // if the user managed to choose a save file, proceed to the game = dispose of the JDialogue
                saveFile = fileChooser.getSelectedFile();
                dispose();
            }
        });

        // Action for starting a new game with the default save
        newGameButton.addActionListener(e -> {
            // if the user chose the default=empty save file, proceed to the game = dispose of the JDialogue
            saveFile = GameSettings.DEFAULT_SAVE_FILE;
            dispose();
        });

        setVisible(true);
    }

    /**
     * Returns the selected player profile file.
     *
     * @return The selected player profile file, or null if no file was selected.
     */
    public File getPlayerFile() {
        return playerFile;
    }

    /**
     * Returns the selected game save file.
     *
     * @return The selected game save file, or null if no file was selected.
     */
    public File getSaveFile() {
        return saveFile;
    }
}
