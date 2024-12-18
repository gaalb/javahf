import GBTAN.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            PreGameMenu preGameMenu = new PreGameMenu(null);
            GameSave save = new GameSave(preGameMenu.getSaveFile());
            Player player = new Player(preGameMenu.getPlayerFile());
            Game game = new Game(player, save);
            game.startGame();
        });
    }
}
