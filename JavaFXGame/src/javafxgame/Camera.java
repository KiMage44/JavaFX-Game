/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Camera extends Entity{
    Player player;
    Pane pane = new Pane();
    Camera(int x, int y, int width, int height, String name, int screenwidth, int screenheight) throws FileNotFoundException{
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
    @Override
    public void updateLocation(double time){
        this.player.physics.setAccelerationX(this.physics.getAccelerationX());
   
        
        this.player.physics.calculateNetX(time);
        this.player.physics.calculateNetY(time);
        PlayerSyncX(time);
        PlayerSyncY(time);
        
        
    }
    private void PlayerSyncX(double time){
        if(this.player.getX() < this.getRightSide()*0.2){//System.out.println("Player is at back of camera, stopping camera x movement to allow player to go forward.");  
        }
        if(this.player.getX() > this.getRightSide()*0.2 && this.player.getX() < this.getRightSide()*0.75){
            //System.out.println("Player is around middle of camera, resuming x movement.");
            this.physics.setAccelerationX(this.player.physics.getAccelerationX());
            this.physics.calculateNetX(time);
        }
        else if(this.player.getX() >= this.getRightSide()*0.75){
            //System.out.println("Player is too far ahead, matching camera speed to player speed.");
            this.physics.setVelocityX(this.player.physics.getVelocityX());
            this.physics.calculateNetX(time);
        }   
    }
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
    public void updateVisual(){
        this.pane.relocate(-this.getX(), -this.getY());
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
        
    }
}
