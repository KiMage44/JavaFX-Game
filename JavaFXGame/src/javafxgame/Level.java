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
    ImageView background;
    Group world = new Group();
    ArrayList<Entity> entityList = new ArrayList<Entity>();
    Level(int gamewidth, int gameheight) throws FileNotFoundException{
        this.gamewidth = gamewidth;
        this.gameheight = gameheight;
    }
    public void createEntity(int x, int y, int width, int height, String name) throws FileNotFoundException{
        if(name == "Camera"){
            this.camera = new Camera(x,y,width,height,name,this.gamewidth, this.gameheight);
            System.out.println("Camera created");
        }
        else if(name == "Player"){
            Player newPlayer = new Player(x,y,width,height,name);
            camera.setPlayer(newPlayer);
        }
        else{
            Entity newEntity = new Entity(x,y,width,height,name);
            this.entityList.add(newEntity);
            System.out.println("New Entity created");
        }
    }
    public void createEntity(int x, int y, int width, int height, String name, double xAcc, double yAcc) throws FileNotFoundException{
        Entity newEntity = new Entity(x,y,width,height,name, xAcc, yAcc);
        this.entityList.add(newEntity);
        System.out.println("New Entity created");
    }
    public void setBackground() throws FileNotFoundException{
        Image image = new Image(new FileInputStream("C:\\Users\\tchoa\\Documents\\GitHub\\JavaFX-Game\\JavaFXGame\\src\\javafxgame\\GameArt\\Game.png"));
        this.background = new ImageView(image);
        this.background.setFitHeight(this.gameheight);
        this.background.setFitWidth(this.gamewidth);
        world.getChildren().add(this.background);
    }
    public Pane buildVisuals() throws FileNotFoundException{
        System.out.println(entityList.size());
        for(int i = 0; i< entityList.size(); i++){
            world.getChildren().add(entityList.get(i).getVisual());
        }
        world.getChildren().add(this.camera.player.entityVisual);
        camera.pane.getChildren().add(world);
        camera.pane.getChildren().add(camera.GUI);
        System.out.println();
        return camera.pane;
    }
    public Pane getPane(){
            return camera.pane;
    }
}
