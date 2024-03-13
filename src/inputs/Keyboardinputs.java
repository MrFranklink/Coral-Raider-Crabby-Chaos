package inputs;

import gamestates.Gamestate;
import main.Game;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import static utilz.Constants.Directions.*;


public class Keyboardinputs implements KeyListener {
    private GamePanel gamePanel;

    public Keyboardinputs(GamePanel gamePanel)
    {
        this.gamePanel=gamePanel;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state)
        {
            case MENU:
                gamePanel.getGame().getMenu().KeyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().KeyPressed(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().KeyPressed(e);
            default:
                break;
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state)
        {
            case MENU:
                gamePanel.getGame().getMenu().KeyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().KeyReleased(e);
                break;
            default:
                break;
        }
    }
}
