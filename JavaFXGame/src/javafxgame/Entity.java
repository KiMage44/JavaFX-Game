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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author tchoa
 */
public class Entity {
    double x;
    double y;
    double width;
    double height;
    String name;
    int health = 100;
    Physics physics = new Physics(this);
    Image image;
    ImageView entityVisual;
    Entity(int x, int y, int width, int height, String name, double xAcc, double yAcc) throws FileNotFoundException{
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.physics.setAccelerationX(xAcc);
        this.physics.setAccelerationY(yAcc);
        this.setVisual("C:\\Users\\tchoa\\Documents\\GitHub\\JavaFX-Game\\JavaFXGame\\src\\javafxgame\\GameArt\\Wall.png");
    }
    Entity(int x, int y, int width, int height, String name) throws FileNotFoundException{
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.setVisual("C:\\Users\\tchoa\\Documents\\GitHub\\JavaFX-Game\\JavaFXGame\\src\\javafxgame\\GameArt\\Wall.png");
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
        this.image = new Image(new FileInputStream(value));
        this.entityVisual = new ImageView(image);
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
