/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Camera extends Entity{
    Player player;
    Pane pane = new Pane();
    Group GUI = new Group();
    double lowerVelCap = 0;
    Camera(int x, int y, int width, int height, String name, int screenwidth, int screenheight, String directory) throws FileNotFoundException{
        super(x, y, width, height, name);
        this.pane.setPrefWidth(screenwidth);
        this.pane.setPrefHeight(screenheight);
        this.pane.setLayoutX(x);
        this.pane.setLayoutY(y);
        System.out.println("Camera created.");
    }
    @Override
    public void setVisual(String value){
    
    }
    //The visual data for the player displayed on the screen.
    public void createGUI(int screenwidth,int screenheight, int score) throws FileNotFoundException{
        HBox bulletBox = new HBox();
        bulletBox.setPrefWidth(300);
        bulletBox.setPrefHeight(screenheight*0.1);
        bulletBox.setLayoutX(0);
        bulletBox.setLayoutY(screenheight*0.9);
        ImageView gunIcon = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\gun.png")));
        gunIcon.setFitHeight(100);
        gunIcon.setFitWidth(100);
        bulletBox.getChildren().add(gunIcon);
        Label bullets = new Label();
        bullets.setFont(new Font("CASTELLAR",30));
        bullets.setTextFill(Color.RED);
        bullets.setPrefSize(100, 100);
        bullets.textProperty().bindBidirectional(this.player.bullets);
        bulletBox.getChildren().add(bullets);
        
        HBox scoreBox = new HBox();
        scoreBox.setMaxWidth(100);
        scoreBox.setMaxHeight(30);
        scoreBox.setMinWidth(100);
        scoreBox.setMinHeight(30);
        scoreBox.setLayoutX(screenwidth*0.9);
        scoreBox.setLayoutY(0);
        for(int i = 0; i<score; i++){
            ImageView star = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\ScoreStar.png")));
            star.setFitHeight(50);
            star.setFitWidth(50);
            scoreBox.getChildren().add(star);
        }
        HBox healthBox = new HBox();
        healthBox.setLayoutX(0);
        healthBox.setLayoutY(0);
        healthBox.setPrefSize(300, 50);
        Label health = new Label();
        health.setText("Health: ");
        health.setTextFill(Color.WHITE);
        health.setFont(new Font("CASTELLAR",30));
        Label healthLabel = new Label();
        healthLabel.setText(String.valueOf(this.player.health));
        healthLabel.textProperty().bindBidirectional(this.player.healthProperty);
        healthLabel.setTextFill(Color.WHITE);
        healthLabel.setFont(new Font("CASTELLAR",30));
        healthBox.getChildren().addAll(health,healthLabel);
        
        
        HBox velocityBox = new HBox();
        velocityBox.setPrefHeight(50);
        velocityBox.setPrefWidth(400);
        velocityBox.setLayoutX(screenwidth-velocityBox.getPrefWidth());
        velocityBox.setLayoutY(screenheight-velocityBox.getPrefHeight());
        Label velocity = new Label();
        velocity.setText("Velocity:  ");
        velocity.setTextFill(Color.WHITE);
        velocity.setFont(new Font("CASTELLAR",30));
        velocityBox.getChildren().add(velocity);
        Label velocityLabel = new Label();
        velocityLabel.setText("0");
        velocityLabel.setTextFill(Color.WHITE);
        velocityLabel.setFont(new Font("CASTELLAR",30));
        velocityLabel.textProperty().bindBidirectional(this.player.physics.xvel);
        velocityBox.getChildren().add(velocityLabel);
        Label speedmeamnt = new Label();
        speedmeamnt.setText("m/s");
        speedmeamnt.setTextFill(Color.WHITE);
        speedmeamnt.setFont(new Font("CASTELLAR",30));
        velocityBox.getChildren().add(speedmeamnt);
        /*Label lives = new Label();
        lives.setPrefHeight(30);
        lives.setPrefWidth(100);
        lives.setLayoutX(screenwidth-(lives.getPrefWidth()*3));
        lives.setLayoutY(screenheight-lives.getPrefHeight());
        lives.setText("Lives: 1");
        lives.setTextFill(Color.WHITE);
        lives.setFont(new Font("Arial",30));*/
        
        //this.GUI.getChildren().add(lives);
        this.GUI.getChildren().add(scoreBox);
        this.GUI.getChildren().add(velocityBox);
        this.GUI.getChildren().add(bulletBox);
        this.GUI.getChildren().add(healthBox);
    }
    @Override
    public void updateLocation(double time){
        this.player.physics.setAccelerationX(this.physics.getAccelerationX());
        this.player.physics.calculateNetX(time);
        this.player.physics.calculateNetY(time);
        PlayerSyncX(time);
        PlayerSyncY(time);
        
    }
    //Syncs camera movement to player movement
    private void PlayerSyncX(double time){
        this.physics.setVelocityX(this.player.physics.getVelocityX());
        this.physics.calculateNetX(time);
    }
    //Syncs camera movement to react to player movements
    private void PlayerSyncY(double time){
        double TopB = this.getTop()+(this.getBottom()*0.3);
        double BottomB = this.getBottom()*0.7;
        //System.out.println("Player Y Velocity:"+this.player.physics.getVelocityY());
        //System.out.println("Camera Y Velocity: "+this.physics.getVelocityY());
        //System.out.println("Player Y Acceleration:"+this.player.physics.getAccelerationY());
        //System.out.println("Camera Y Acceleration: "+this.physics.getAccelerationY());
        if(this.player.getY() > BottomB && this.player.physics.getVelocityY() > 0){
            //System.out.println("Player has fallen to far down, forcing camera to follow");
            this.physics.setVelocityY(this.player.physics.getVelocityY());
        }
        else if(this.player.getY() < TopB && this.player.physics.getVelocityY() < 0){
            //System.out.println("Player has risen to far up, forcing camera to follow");
            this.physics.setVelocityY(this.player.physics.getVelocityY());
        }
        else{
            this.physics.setVelocityY(0);
            this.physics.setAccelerationY(0);
            //System.out.println("Player in middle of Camera, no need for special changes.");
        
        }
        this.physics.calculateNetY(time);
    }
    @Override
    //relocates player, gui, and camera at once.
    public void updateVisual(){
        this.pane.relocate(-this.getX(), -this.getY());
        this.GUI.relocate(this.getX(), this.getY());
        this.player.entityVisual.relocate(this.player.getX(), this.player.getY());
    }
    @Override
    public void setX(double value){
        this.x = value;
    }
    @Override
    public void setY(double value){
        this.y = value;
    }
    @Override
    public double getX(){
        return this.x;
    }
    @Override
    public double getY(){
        return this.y;
    }
    @Override
    public void setHeight(double value){
        this.height = value;
        this.pane.setPrefHeight(value);
    }
    @Override
    public void setWidth(double value){
        this.width = value;
        this.pane.setPrefWidth(value);
    }   
    public void setPlayer(Player player){
        this.player = player;
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
    public void CamerawallCollisions(double gamewidth, double gameheight){
        if(this.getLeftSide() < 0){
            this.setX(0);
            this.physics.setVelocityX(0);
        }
        if(this.getRightSide() > gamewidth){
            this.setX(gamewidth-this.width);
            this.physics.setVelocityX(0);
        }
        if(this.getTop() < 0){
            this.setY(0);
            this.physics.setVelocityY(0);
        }
        if(this.getBottom() > gameheight){
            this.setY(gameheight-this.height);
            this.physics.setVelocityY(0);
        }
        if(this.player.getX()< this.getX()){
            this.player.setX(this.getX());
        }
    }
}
