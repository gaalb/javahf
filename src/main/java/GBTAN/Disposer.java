package GBTAN;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

/**
 * Handles post-game cleanup when the game window is closed.
 * Specifically, this class saves the current game state to a file and exits the application.
 */
public class Disposer extends WindowAdapter {

    /**
     * The game instance to save upon window closure.
     */
    private final Game game;

    /**
     * Creates a new Disposer associated with the specified game instance.
     *
     * @param game the game instance whose state will be saved on window closure.
     */
    public Disposer(Game game) {
        this.game = game;
    }

    /**
     * Called when the game window is closed.
     * Saves the current game state to a JSON file in the saves folder,
     * with a filename based on the current date and time.
     *
     * @param e the window event triggering this method.
     * @see GameSave#saveToFile(File)
     * @see GameSettings#SAVES_FOLDER
     */
    @Override
    public void windowClosed(WindowEvent e) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String dateTimeString = now.format(formatter);
        // The name of the resulting save file will include the year, month, day, hour, and minute, e.g., 20241107_2303
        GameSave save = new GameSave(game);
        save.saveToFile(new File(GameSettings.SAVES_FOLDER, dateTimeString + ".json"));
        System.exit(0);
    }
}
