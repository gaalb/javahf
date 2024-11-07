package GBTAN;

import javax.swing.*;
import java.io.File;
import java.awt.*;

public class PreGameMenu extends JDialog {
    private File playerFile;
    private File saveFile;

    public PreGameMenu(Frame owner) {
        super(owner, true);
        setTitle("Profile Selection");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        JPanel firstPanel = new JPanel();
        JButton loadProfileButton = new JButton("Load Profile");
        JButton defaultProfileButton = new JButton("Default Profile");
        firstPanel.add(loadProfileButton);
        firstPanel.add(defaultProfileButton);

        JPanel secondPanel = new JPanel();
        JButton loadSaveButton = new JButton("Load Save");
        JButton newGameButton = new JButton("New Game");
        secondPanel.add(loadSaveButton);
        secondPanel.add(newGameButton);

        cardPanel.add(firstPanel, "First");
        cardPanel.add(secondPanel, "Second");

        add(cardPanel);
        pack();
        setLocationRelativeTo(null);

        loadProfileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("players");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                playerFile = fileChooser.getSelectedFile();
                cardLayout.show(cardPanel, "Second");
                setTitle("Save File Selection");
            }
        });

        defaultProfileButton.addActionListener(e -> {
            playerFile = GameSettings.DEFAULT_PLAYER_FILE;
            cardLayout.show(cardPanel, "Second");
            setTitle("Save File Selection");
        });

        loadSaveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("saves");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                saveFile = fileChooser.getSelectedFile();
                dispose();
            }
        });

        newGameButton.addActionListener(e -> {
            saveFile = GameSettings.DEFAULT_SAVE_FILE;
            dispose();
        });

        setVisible(true);
    }

    public File getPlayerFile() {
        return playerFile;
    }

    public File getSaveFile() {
        return saveFile;
    }
}
