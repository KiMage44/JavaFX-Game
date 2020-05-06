/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 *
 * @author tchoa
 */
public class Player extends Entity{
    ArrayList<Image> imagelist = new ArrayList<Image>();
    int imageCounter = 0;
    int bulletCount = 10;
    boolean hitfinish = false;
    StringProperty bullets = new SimpleStringProperty();
    Player(int x, int y, int width, int height, String name) throws FileNotFoundException{
        super(x, y, width, height, name);
        this.setVisual(directory+"\\GameArt\\adventurer-crnr-grb-01.png");
        System.out.println("Player physics made");
        bullets.set(String.valueOf(bulletCount));
        imagelist.add(new Image(new FileInputStream(directory+"\\GameArt\\adventurer1.png")));
        imagelist.add(new Image(new FileInputStream(directory+"\\GameArt\\adventurer2.png")));
        imagelist.add(new Image(new FileInputStream(directory+"\\GameArt\\adventurer3.png")));
        imagelist.add(new Image(new FileInputStream(directory+"\\GameArt\\adventurer4.png")));
        imagelist.add(new Image(new FileInputStream(directory+"\\GameArt\\adventurer5.png")));
        
    }
    //This is to prevent entity class's updatelocation from being called. Player is updated in camera class
    @Override
    public void updateLocation(double time){
    }
    //updates character visual on screen.
    public void updateAnimation() throws FileNotFoundException{
        String i = directory+"\\GameArt\\adventurer-crnr-grb-01.png";
        String j = directory+"\\GameArt\\Wall.png";
        String h = directory+"\\GameArt\\Player.png";
        if(imageCounter == 1)
            this.setVisual(i);
        else if(imageCounter == 2)
            this.setVisual(j);
        else if(imageCounter == 3)
            this.setVisual(h);
        else
            imageCounter = 0;
        //System.out.println("Visual changed");
    }
    public void dropBulletCount(){
        bulletCount--;
        bullets.set(String.valueOf(bulletCount));
    }
}
