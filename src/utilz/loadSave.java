package utilz;

import entites.Crabby;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static utilz.Constants.EnemyConstants.CRABBY;

public class loadSave {
    public static final String Player_Atlas="player_sprites.png";
    public static final String Level_Atlas="outside_sprites.png";
//    public static final String Level_one_data="level_one_data.png";
    public static final String Menu_Buttons="button_atlas.png";
    public static final String Menu_Background="menu_background.png";
    public static final String Pause_Background="pause_menu.png";
    public static final String Sound_Button="sound_button.png";
    public static final String URM_Button="urm_buttons.png";
    public static final String Volume_Button="volume_buttons.png";
    public static final String Menu_Background_img="background_menu.png";
    public static final String Playing_BG_IMG="playing_bg_img.png";
    public static final String BIG_CLOUD="big_clouds.png";
    public static final String SMALL_CLOUD="small_clouds.png";
    public static final String CRABBY_SPRITE="crabby_sprite.png";
    public static final String STATUS_BAR="health_power_bar.png";
    public static final String COMPLETED_IMG="completed_sprite.png";
    public static final String POTION_ATLAS= "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String OPTION_SCREEN = "options_background.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img=null;
        InputStream is=loadSave.class.getResourceAsStream("/"+fileName);
        try {
             img= ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return img;

    }

    public static BufferedImage[] GetAllLevels()
    {
        URL url=loadSave.class.getResource("/lvls");
        File file=null;

        try {
            file=new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        File[] files=file.listFiles();
        Arrays.sort(files);

        BufferedImage[] imgs=new BufferedImage[files.length];
        for(int i=0;i< imgs.length;i++) {
            try {
                imgs[i]=ImageIO.read(files[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imgs;
    }




}
