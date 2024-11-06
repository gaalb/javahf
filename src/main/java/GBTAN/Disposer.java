package GBTAN;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Disposer extends WindowAdapter {
    Game game;
    public Disposer (Game game) {
        this.game = game;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Player player = game.getGameData().getPlayer();
        System.out.println("CLOSING!");
        if (game.getGameData().getScore() > player.getHighScore()) {
            System.out.println("HIGH SCORE!");
        }
        System.exit(0);
    }
}
