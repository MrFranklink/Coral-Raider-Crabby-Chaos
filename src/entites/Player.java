package entites;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.loadSave;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static utilz.Constants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {
    private BufferedImage[][] Animations;
    private boolean left,right,jump;
    private boolean moving=false,attacking=false;

    private int[][] lvlData;
    private float xDrawOffset=21* Game.SCALE;
    private float yDrawOffset=4*Game.SCALE;

    //Jump,Gravity
    private float jumpSpeed= -2.25f*Game.SCALE;
    private float fallSpeedAfterCollision=0.5f *Game.SCALE;


    //StatusBar Ui

    private BufferedImage statusBarImg;

    private int statusBarWidth=(int) (192 * Game.SCALE);
    private int statusBarHeight=(int) (58 * Game.SCALE);
    private int statusBarX=(int) (10 * Game.SCALE);
    private int statusBarY=(int) (10 * Game.SCALE);

    private int healthBarWidth=(int) (150 * Game.SCALE);
    private int healthBarHeight=(int) (4 * Game.SCALE);
    private int healthBarXStart=(int) (34 * Game.SCALE);
    private int healthBarYStart=(int) (14 * Game.SCALE);


    private int healthWidth=healthBarWidth;


    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;

    //AttackBox_kill



    private int flipX=0;
    private int flipW=1;
    private boolean attackChecked;
    private Playing playing;

    private int tileY=0;
    private boolean powerAttackActive;
    private int powerAttackTick;
    private int powerGrowSpeed=15;
    private int powerGrowTick;



    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y,width,height);
        this.playing=playing;
        this.state=IDLE;
        this.maxHealth=100;
        this.currentHealth=maxHealth;
        this.walkSpeed=Game.SCALE*1.0f;
        loadAnimation();
        initHitBox(20,27);
        iniitAttackBox();
    }

    public void setSpawn(Point spawn)
    {
        this.x=spawn.x;
        this.y=spawn.y;
        hitbox.x=x;
        hitbox.y=y;
    }

    private void iniitAttackBox() {

        attackBox=new Rectangle2D.Float(x,y,(int) (20* Game.SCALE),(int)(20*Game.SCALE));
        resetAttackBox();
    }

    public void update()
    {
        updateHealthBar();
        updatePowerBar();
        if(currentHealth<=0)
        {
            if(state!=Dead)
            {
                state=Dead;
                aniTick=0;
                aniIndex=0;
                playing.setPlayingDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            }else if(aniIndex==GetSpriteAmount(Dead)-1 && aniTick>=ANI_SPEED-1)
            {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            }else
                updateAnimationTick();

            return;
        }

        updateAttacKBox();

        updatePos();
        if(moving) {
            checkPotionTouched();
            checkSpikeTouched();
            tileY=(int)(hitbox.y/Game.TILES_SIZE);

            if (powerAttackActive) {
                powerAttackTick++;
                if (powerAttackTick >= 35) {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }

        }
        if(attacking || powerAttackActive)
            checkAttack();
        updateAnimationTick();
        setAnimation();

    }

    private void checkSpikeTouched() {
        playing.checkSpikeTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecked || aniIndex!=1)
            return;
        attackChecked=true;

        if (powerAttackActive)
            attackChecked = false;

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();

    }

    private void updateAttacKBox()
    {
        if (right && left) {
            if (flipW == 1) {
                attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
            } else {
                attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
            }

        } else if (right || (powerAttackActive && flipW == 1))
            attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
        else if (left || (powerAttackActive && flipW == -1))
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);

        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {

        healthWidth=(int) ((currentHealth/ (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar() {
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    public void render(Graphics g,int lvlOffset)
    {
        g.drawImage(Animations[state][aniIndex],(int)(hitbox.x-xDrawOffset)-lvlOffset+flipX,(int)(hitbox.y-yDrawOffset),width*flipW,height,null);
//        drawHitbox(g,lvlOffset);
        drawAttackBox(g,lvlOffset);
        drawUI(g);
    }
    private void drawUI(Graphics g) {
        //background ui
        g.drawImage(statusBarImg,statusBarX,statusBarY,statusBarWidth,statusBarHeight,null);
        //health ui
        g.setColor(Color.red);
        g.fillRect(statusBarX+healthBarXStart,statusBarY+healthBarYStart,healthWidth,healthBarHeight);

        //power ui

        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }


    private void updatePos() {
        moving=false;
        if(jump)
            jump();

//        if(!left && !right && !inAir)return;


        if (!inAir)
            if (!powerAttackActive)
                if ((!left && !right) || (right && left))
                    return;


        float xSpeed=0;

        if(left && !right) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }

         if(right && !left) {
             xSpeed += walkSpeed;
             flipX=0;
             flipW=1;
         }

        if (powerAttackActive) {
            if ((!left && !right) || (left && right)) {
                if (flipW == -1)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;
            }

            xSpeed *= 3;
        }

         if(!inAir)
             if(!IsEntityOnFloor(hitbox,lvlData))
                 inAir=true;


         if(inAir && !powerAttackActive) {
                if(CanMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData))
                {
                    hitbox.y+=airSpeed;
                    airSpeed+=GRAVITY;
                    updateXPos(xSpeed);
                }
                else
                {
                    hitbox.y=GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                    if(airSpeed>0)
                        resetInAir();
                    else
                        airSpeed=fallSpeedAfterCollision;
                    updateXPos(xSpeed);
                }

         }
         else
             updateXPos(xSpeed);

         moving=true;

    }

    private void jump() {
        if(inAir)
            return;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir=true;
        airSpeed=jumpSpeed;
    }

    private void resetInAir() {
        inAir=false;
        airSpeed=0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width, hitbox.height,lvlData))
        {
            hitbox.x+=xSpeed;
        }
        else
        {
            hitbox.x=GetEntityXPosNextToWall(hitbox,xSpeed);
            if(powerAttackActive)
            {
                powerAttackActive=false;
                powerAttackTick=0;
            }
        }


    }

    public void changeHealth(int value)
    {
        currentHealth+=value;

        if(currentHealth<=0)
        {
            currentHealth=0;
//            gameOver();
        }else if(currentHealth>=maxHealth)
        {
            currentHealth=maxHealth;
        }

    }



    public void kill() {
        currentHealth=0;
    }

    public void changePower(int value) {
        powerValue += value;
        if (powerValue >= powerMaxValue)
            powerValue = powerMaxValue;
        else if (powerValue <= 0)
            powerValue = 0;
    }



    private void setAnimation() {
        int startAni=state;
        if(moving)
        {
            state=RUNNING;
        }
        else
        {
            state=IDLE;
        }

        if(inAir) {
            if (airSpeed < 0)
                state = JUMP;
            else
                state = FALLING;
        }

        if (powerAttackActive) {
            state = ATTACK;
            aniIndex = 1;
            aniTick = 0;
            return;
        }


        if(attacking)
        {
            state=ATTACK;
            if(startAni!=ATTACK)
            {
                aniIndex=1;
                aniTick=0;
                return;
            }
        }

        if(startAni!=state)
        {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick=0;
        aniIndex=0;
    }

    private void updateAnimationTick() {
        aniTick++;
        if(aniTick>=ANI_SPEED)
        {
            aniTick=0;
            aniIndex++;
            if(aniIndex>=GetSpriteAmount(state))
            {
                aniIndex=0;
                attacking=false;
                attackChecked=false;
            }

        }

    }


    private void loadAnimation() {

           BufferedImage img= loadSave.GetSpriteAtlas(loadSave.Player_Atlas);
            Animations=new BufferedImage[7][8];
            for(int i=0;i<Animations.length;i++) {
                for (int j = 0; j < Animations[i].length; j++) {
                    Animations[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
                }
            }

            statusBarImg=loadSave.GetSpriteAtlas(loadSave.STATUS_BAR);



    }

    public void loadLvlData(int[][] lvlData)
    {
        this.lvlData=lvlData;
        if(!IsEntityOnFloor(hitbox,lvlData))
            inAir=true;
    }

    public void restDirBoolean()
    {
        left=false;
        right=false;

    }

    public void setAttacking(boolean attacking)
    {
        this.attacking=attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
    public boolean isJump() {
        return jump;
    }
    public void setJump(boolean jump) {
        this.jump= jump;
    }

    public void resetAll() {
        restDirBoolean();
        inAir=false;
        attacking=false;
        airSpeed=0f;
        moving=false;
        state=IDLE;
        currentHealth=maxHealth;
        hitbox.x=x;
        hitbox.y=y;

        resetAttackBox();

        if(!IsEntityOnFloor(hitbox,lvlData))
            inAir=true;
    }


    private void resetAttackBox() {
        if (flipW == 1) {
            attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
        } else {
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
        }
    }

    public int getTileY()
    {
        return tileY;
    }


    public void powerAttack() {
        if (powerAttackActive)
            return;
        if (powerValue >= 60) {
            powerAttackActive = true;
            changePower(-60);
        }
    }
}
