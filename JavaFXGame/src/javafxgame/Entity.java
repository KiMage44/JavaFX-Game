/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author tchoa
 */
//Standard entity class all entities inherit from
public class Entity {
    double x;
    double y;
    double width;
    double height;
    String name;
    String directory = System.getProperty("user.dir");
    int health;
    StringProperty healthProperty = new SimpleStringProperty();
    Physics physics = new Physics(this);
    Image image;
    ImageView entityVisual = new ImageView();
    //for entities with predefined static movement
    Entity(double x, double y, int width, int height, String name, double xVel, double yVel) throws FileNotFoundException{
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.physics.setVelocityX(xVel);
        this.physics.setVelocityY(yVel);
        this.setVisual(this.directory+"\\GameArt\\Wall.png");
        this.setHealth(100);
    }
    //for entities with no movement.
    Entity(double x, double y, int width, int height, String name) throws FileNotFoundException{
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.setVisual(this.directory+"\\GameArt\\Wall.png");
        this.setHealth(100);
    }
    public void setHealth(int value){
        this.health = value;
        this.healthProperty.set(String.valueOf(this.health));
        System.out.println("Set health to "+String.valueOf(this.health));
    }
    public int getHealth(){
        return this.health;
    }
    public void setX(double value){
        this.x = value;
        this.entityVisual.setX(this.x);
    }
    public void setY(double value){
        this.y = value;
        this.entityVisual.setY(this.y);
    }
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public void setHeight(double value){
        this.width = value;
        this.entityVisual.setFitHeight(this.height);
    }
    public void setWidth(double value){
        this.height = value;
        this.entityVisual.setFitWidth(this.width);
    }   
    public void setVisual(String value) throws FileNotFoundException{
        //System.out.println(value);
        Image newIMG = new Image(new FileInputStream(value));
        this.entityVisual.setImage(newIMG);
        this.entityVisual.setFitWidth(this.width);
        this.entityVisual.setFitHeight(this.height);
    }
    public void updateLocation(double time){
        this.physics.calculateNetX(time);
        this.physics.calculateNetY(time);
    }
    public void updateVisual(){
        this.entityVisual.relocate(this.x, this.y);
    }
    public ImageView getVisual(){
        return this.entityVisual;
    }
    
//following are for collision calculations
    public double getTop(){
        return this.getY();
    }
    public double getBottom(){
        return this.getY()+this.height;
    }
    public double getLeftSide(){
        return this.getX();
    }
    public double getRightSide(){
        return this.getX()+this.width;
    }
}
