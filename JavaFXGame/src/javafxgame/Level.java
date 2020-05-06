/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author tchoa
 */
public class Level{
    Stage stage;
    Camera camera;
    int gamewidth;
    int gameheight;
    int screenheight;
    ImageView background;
    boolean completed = false;
    Group world = new Group();
    String directory;
    ArrayList<Entity> totalEntityList = new ArrayList<Entity>();
    ArrayList<Entity> entityList = new ArrayList<Entity>();
    ArrayList<Enemies> enemyList = new ArrayList<Enemies>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    Level(int gamewidth, int gameheight, int screenheight, String directory) throws FileNotFoundException{
        this.screenheight = screenheight;
        this.directory = directory;
        this.gamewidth = gamewidth;
        this.gameheight = gameheight;
    }
    //Responsible for creating static entities, enemies, or entities with independent movement.
    public Entity createEntity(int x, int y, int width, int height, String name) throws FileNotFoundException{
        if(name == "Camera"){
            this.camera = new Camera(x,y,width,height,name,this.gamewidth, this.screenheight, this.directory);
            System.out.println("Camera created at "+x+" "+y);
        }
        else if(name.equals("Player")){
            Player newPlayer = new Player(x,y,width,height,name);
            camera.setPlayer(newPlayer);
            this.totalEntityList.add(newPlayer);
        }
        else if(name.equals("Enemy")){
            Enemies newEnemy = new Enemies(x,y,width,height,name,this);
            this.enemyList.add(newEnemy);
            this.entityList.add(newEnemy);
            System.out.println("New Enemy created at "+x+" "+y);
            return newEnemy;
        }
        else{
            Entity newEntity = new Entity(x,y,width,height,name);
            this.entityList.add(newEntity);
            newEntity.setVisual(this.directory+"\\GameArt\\entity1.png");
            System.out.println("New Entity created at "+x+" "+y);
            return newEntity;
        }
        return null;
    }
    //Responsible for creating entities that have a predefined static movememnt 
    public Entity createEntity(int x, int y, int width, int height, String name, double xAcc, double yAcc) throws FileNotFoundException{
        Entity newEntity = new Entity(x,y,width,height,name, xAcc, yAcc);
        this.entityList.add(newEntity);
        newEntity.setVisual(this.directory+"\\GameArt\\entity2.png");
        System.out.println("New Entity created at "+x+" "+y);
        return newEntity;
    }
    //creates bullets resulting from player action
    public void createBullet(double mouseLocationX, double mouseLocationY) throws FileNotFoundException{
        Bullet newBullet = new Bullet(this.camera, mouseLocationX, mouseLocationY);
        this.bullets.add(newBullet);
        this.world.getChildren().add(newBullet.entityVisual);
    }
    //creates bullets resulting from enemy action
    public void createBullet(Enemies enemy, double time) throws FileNotFoundException{
        Bullet newBullet = new Bullet(this.camera.player,enemy, time);
        this.bullets.add(newBullet);
        this.world.getChildren().add(newBullet.entityVisual);
    }
    //takes input and changes the level's background image
    public void setBackground(String value) throws FileNotFoundException{
        Image image = new Image(new FileInputStream(this.directory+value));
        this.background = new ImageView(image);
        this.background.setFitHeight(this.gameheight);
        this.background.setFitWidth(this.gamewidth);
        world.getChildren().add(this.background);
    }
    //compiles all entities existing in the level into the camera's pane, then returns the pane to the caller
    public Pane buildVisuals() throws FileNotFoundException{
        System.out.println(entityList.size());
        for(int i = 0; i< entityList.size(); i++){
            world.getChildren().add(entityList.get(i).getVisual());
        }
        world.getChildren().add(this.camera.player.entityVisual);
        camera.pane.getChildren().add(world);
        camera.pane.getChildren().add(camera.GUI);
        //System.out.println();
        return camera.pane;
    }
    //returns the visible entities for the level.
    public Pane getPane(){
            return camera.pane;
    }
    public void destroyLevel(){
        this.background = null;
        for(int i = 0; i< totalEntityList.size(); i++){
            totalEntityList.set(i,null);
        }
        for(int i = 0; i< entityList.size(); i++){
            entityList.set(i,null);
        }
        for(int i = 0; i< enemyList.size(); i++){
            enemyList.set(i,null);
        }
        for(int i = 0; i< bullets.size(); i++){
            bullets.set(i,null);
        }
        this.background = null;
        System.gc();
    }
}
