/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.beans.EventHandler;
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
    double value = 2;
    double releasevalue = 0;
    keyHandler(Camera camera, String name){
        this.camera = camera;
        this.name = name;
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
        else if(event.getEventType().getName() == "KEY_RELEASED"){
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
    }
    public void PlayerKeyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP){
            player.physics.setAccelerationY(-value);
            //System.out.println("W pressed");
        }
        else if(event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN){
            player.physics.setAccelerationY(value);
            //System.out.println("S pressed");
        }
    }
    public void PlayerKeyReleased(KeyEvent event){
        if(event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP){
            player.physics.setAccelerationY(releasevalue);
            //System.out.println("W released");
        }
        else if(event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN){
            player.physics.setAccelerationY(-releasevalue);
            //System.out.println("S released");
        }
    }
}
