package ui;

import gamestates.Gamestate;
import utilz.loadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import static utilz.Constants.UI.Buttons.*;

public class MenuButton {
    private int xPOS,yPos,rowIndex,index;
    private int xOffsetCenter=B_WIDTH/2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver,mousePressed;
    private Rectangle bounds;
    public MenuButton(int xPos,int yPos,int rowIndex, Gamestate state)
    {
        this.xPOS=xPos;
        this.yPos=yPos;
        this.rowIndex=rowIndex;
        this.state=state;
        loadImgs();
        initBounds();

    }

    private void initBounds() {
        bounds=new Rectangle(xPOS-xOffsetCenter,yPos,B_WIDTH,B_Height);
    }

    private void loadImgs() {
        imgs=new BufferedImage[3];
        BufferedImage temp= loadSave.GetSpriteAtlas(loadSave.Menu_Buttons);
        for(int i=0;i<imgs.length;i++)
        {
            imgs[i]=temp.getSubimage(i*B_WIDTH_DEFAULT,rowIndex*B_HEIGHT_DEFAULT,B_WIDTH_DEFAULT,B_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g)
    {
        g.drawImage(imgs[index],xPOS-xOffsetCenter,yPos,B_WIDTH,B_Height,null);
    }

    public void update()
    {
        index=0;
        if(mouseOver)
            index=1;
        if(mousePressed)
            index=2;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public void applyGamestate()
    {
        Gamestate.state=state;
    }

    public void resetBools()
    {
        mouseOver=false;
        mousePressed=false;
    }

    public Gamestate getState(){
        return state;
    }


}
