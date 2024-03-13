package main;


import audio.AudioPlayer;
import entites.Player;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import utilz.loadSave;


import java.awt.*;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET=200;
    private final int UPS_SET=250;

    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;    

    public final static int TILES_DEFAULT_SIZE=32;
    public final static float SCALE=1.5f;
    public final static int TILES_IN_WIDTH=26;
    public final static int TILES_IN_HEIGHT=14;
    public final static int TILES_SIZE=(int)(TILES_DEFAULT_SIZE*SCALE);
    public final static int GAME_WIDTH=TILES_SIZE*TILES_IN_WIDTH;
    public final static int GAME_HEIGHT=TILES_SIZE*TILES_IN_HEIGHT;




    public Game()
    {
        initClasses();
        gamePanel=new GamePanel(this);
        gameWindow=new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();


    }

    public void startGameLoop()
    {
        gameThread=new Thread(this);
       gameThread.start();

    }

    private void initClasses() {
        audioOptions=new AudioOptions(this);
        audioPlayer =new AudioPlayer();
        menu=new Menu(this);
        playing=new Playing(this);
        gameOptions=new GameOptions(this);

    }

    public void update()
    {
        switch (Gamestate.state)
        {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }

    }
    public void render(Graphics g)
    {


        switch (Gamestate.state)
        {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            default:
                break;
        }

    }

    @Override
    public void run() {
        double timePerFrame=1000000000.0/FPS_SET;
        double timePerUpdate=1000000000.0/UPS_SET;

        long previousTime=System.nanoTime();
        int frame=0;
        int update=0;
        double deltaU=0;
        double deltaF=0;

        long LastCheck=System.currentTimeMillis();
        while(true)
        {

            long currTime=System.nanoTime();
            deltaU+=(currTime-previousTime)/timePerUpdate;
            deltaF+=(currTime-previousTime)/timePerFrame;
            previousTime=currTime;

            if(deltaU>=1)
            {
                update();
                update++;
                deltaU--;
            }

            if(deltaF>=1)
            {
                gamePanel.repaint();
                frame++;
                deltaF--;

            }




            if(System.currentTimeMillis()-LastCheck>=1000)
            {
                LastCheck=System.currentTimeMillis();
                System.out.println("Fps:"+frame+"| UPS:"+update);
                frame=0;
                update=0;

            }

        }
    }

        public void windowFocusLost()
        {
            if(Gamestate.state==Gamestate.PLAYING)
                playing.getPlayer().restDirBoolean();
        }

        public Menu getMenu()
        {
            return menu;
        }

        public Playing getPlaying()
        {
            return playing;
        }
        public GameOptions getGameOptions(){
        return gameOptions;
        }


        public AudioOptions getAudioOptions(){return audioOptions; }

        public AudioPlayer getAudioPlayer(){return  audioPlayer;}

}
