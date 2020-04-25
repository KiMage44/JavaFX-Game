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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author tchoa
 */
public class Level {
    Stage stage;
    Camera camera;
    int screenwidth;
    int screenheight;
    int gamewidth;
    int gameheight;
    ArrayList<Entity> entityList = new ArrayList<Entity>();
    Level(Stage stage, int screenwidth, int screenheight, int gamewidth, int gameheight) throws FileNotFoundException{
        this.stage = stage;
        this.screenwidth = screenwidth;
        this.screenheight = screenheight;
        this.gamewidth = gamewidth;
        this.gameheight = gameheight;
        createEntity(0, 0, screenwidth, screenheight, "Camera");
        createEntity(30, (int) (this.camera.getY()+(this.camera.height/2)), 50, 50, "Player"); 
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
    public void display() throws FileNotFoundException{
        Image backg = new Image(new FileInputStream("C:\\Users\\tchoa\\Documents\\GitHub\\JavaFX-Game\\JavaFXGame\\src\\javafxgame\\GameArt\\Game.png"));
        ImageView image = new ImageView(backg);
        image.setFitHeight(this.gameheight);
        image.setFitWidth(this.gamewidth);
        Group world = new Group();
        world.getChildren().add(image);
        for(int i = 0; i< entityList.size(); i++){
            world.getChildren().add(entityList.get(i).getVisual());
        }
        camera.pane.getChildren().add(world);
        camera.pane.getChildren().add(this.camera.player.entityVisual);
        
        Scene scene = new Scene(camera.pane,this.screenwidth,this.screenheight);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new keyHandler(this.camera, this.camera.name));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new keyHandler(this.camera, this.camera.name));
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new keyHandler(this.camera.player, this.camera.player.name));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new keyHandler(this.camera.player,this.camera.player.name));
        stage.setScene(scene);
        stage.show();
        gameLoop();
    }
    private void gameLoop(){
        AnimationTimer animation = new AnimationTimer(){
            boolean started = false;
            double firstTime;
            double lastTime;
            double currentTime;
            double elapsedTimeSeconds;
            @Override
            public void handle(long now) {
                if(!started){
                    firstTime = System.currentTimeMillis();
                    lastTime = firstTime;
                    started = true;
                    //System.out.println("Program Started: "+firstTime);
                }
                currentTime = System.currentTimeMillis();
                //System.out.println("milis: "+currentTime);
                elapsedTimeSeconds = ((currentTime-lastTime)/100.0);
                //System.out.println(elapsedTimeSeconds);
                updateLocations(elapsedTimeSeconds);
                wallCollisions();
                updateVisuals();
                lastTime = currentTime;
            }
        };
        animation.start();
    }
    private void updateLocations(double time){
        for(int i = 0; i< entityList.size(); i++){
            entityList.get(i).updateLocation(time);
        }
        this.camera.updateLocation(time);
    }
    private void wallCollisions(){
        for(int i = 0; i< entityList.size(); i++){
                entityList.get(i).physics.wallCollisions(this.gamewidth, this.gameheight);
        }
        this.camera.CamerawallCollisions(this.gamewidth, this.gameheight);
        this.camera.player.physics.wallCollisions(this.gamewidth, this.gameheight);
    }
    private void updateVisuals(){
        for(int i = 0; i< entityList.size(); i++){
                entityList.get(i).updateVisual();
            }
        this.camera.updateVisual();
    }
}
