package GBTAN;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

public class Disposer extends WindowAdapter {
    Game game;
    public Disposer (Game game) {
        this.game = game;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String dateTimeString = now.format(formatter);
        GameSave save = new GameSave(game);
        save.saveToFile(new File(GameSettings.SAVES_FOLDER, dateTimeString+".json"));
        System.exit(0);
    }
}
