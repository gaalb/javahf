package GBTAN;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

public class Disposer extends WindowAdapter {
    // Window adapter that handles post-game spindown, which in this case is saving the game state
    Game game;
    public Disposer (Game game) {
        this.game = game;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String dateTimeString = now.format(formatter);
        // The name of the resulting save file will be the year, month, day, hour and minute, e.g. 20241107_2303
        GameSave save = new GameSave(game);
        save.saveToFile(new File(GameSettings.SAVES_FOLDER, dateTimeString+".json"));
        System.exit(0);
    }
}
