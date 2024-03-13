package main;

import inputs.Keyboardinputs;
import inputs.Mouseinputs;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import static main.Game.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;


public class GamePanel extends JPanel {
    private Mouseinputs mouseinputs;
    private Game game;



    public GamePanel(Game game) {
        mouseinputs = new Mouseinputs(this);
        this.game=game;
        SetPanelSize();
        addKeyListener(new Keyboardinputs(this));
        addMouseListener(mouseinputs);
        addMouseMotionListener(mouseinputs);

    }


    private void SetPanelSize() {
        Dimension dimension = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(dimension);
        System.out.println("Width"+GAME_WIDTH+" "+" Height"+GAME_HEIGHT);
    }

    public void updateGame()
    {

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);


    }

    public Game getGame()
    {
        return game;
    }
}
