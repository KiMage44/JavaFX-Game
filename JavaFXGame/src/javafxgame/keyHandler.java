/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.beans.EventHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *s
 * @author tchoa
 */
class keyHandler extends Thread implements javafx.event.EventHandler<KeyEvent>{
    Player player;
    Camera camera;
    String name;
    IntegerProperty screenChange;
    double value = 2;
    double Vvalue = 20;
    double releasevalue = 0;
    keyHandler(Camera camera, String name, IntegerProperty screenChange){
        this.camera = camera;
        this.name = name;
        this.screenChange = screenChange;
    }
    keyHandler(Player player, String name){
        this.player = player;
        this.name = name;
    }
    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().getName() == "KEY_PRESSED"){
            if(this.name == "Camera")
                CameraKeyPressed(event);
            else if (this.name == "Player")
                PlayerKeyPressed(event);
        }
        if(event.getEventType().getName() == "KEY_RELEASED"){
            if(this.name == "Camera")
                CameraKeyReleased(event);
            else if (this.name == "Player")
                PlayerKeyReleased(event);
        }
    }
    public void CameraKeyPressed(KeyEvent event){
        
        if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT){
            camera.physics.setAccelerationX(-value);
           // System.out.println("A pressed");
        }
        else if(event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT){
            camera.physics.setAccelerationX(value);
            //System.out.println("D pressed");
        }
    }
    public void CameraKeyReleased(KeyEvent event){
        if(event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT){
            camera.physics.setAccelerationX(releasevalue);
            //System.out.println("A released");
        }
        else if(event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT){
            camera.physics.setAccelerationX(-releasevalue);
            //System.out.println("D released");
        }
        if(event.getCode() == KeyCode.ESCAPE){
           if(this.screenChange.get() == 1)
               this.screenChange.set(4);
           else
               this.screenChange.set(1);
        }
    }
    public void PlayerKeyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP){
            player.physics.setVelocityY(-Vvalue);
            //System.out.println("W pressed");
        }
        else if(event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN){
            player.physics.setVelocityY(Vvalue);
            //System.out.println("S pressed");
        }
    }
    public void PlayerKeyReleased(KeyEvent event){
        if(event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP){
            player.physics.setVelocityY(0);
            //System.out.println("W released");
        }
        else if(event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN){
            player.physics.setVelocityY(0);
            //System.out.println("S released");
        }
    }
}