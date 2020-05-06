/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileNotFoundException;
import java.util.Random;
import javafx.scene.Scene;

/**
 *
 * @author tchoa
 */
public class Bullet extends Entity{
    Camera camera;
    Enemies enemy;
    Player player;
    float goalX;
    float goalY;
    Bullet(Camera camera, double targetX, double targetY) throws FileNotFoundException{
        super(camera.player.getX()+camera.player.width, camera.player.getY()+(camera.player.height/2), 20, 20, "Bullet");
        //Computes the velocity for the bullet to head to its target, then adds target's speed as well for tracking
        float speed = 40;
        this.camera = camera;
        this.player = camera.player;
        this.goalX = ((float)this.camera.getX())+((float)targetX);
        this.goalY = ((float)this.camera.getY())+((float)targetY);
        float distX = this.goalX-(float)this.player.getX();
        float distY = this.goalY-(float)this.player.getY();
        float distance = (float) Math.sqrt(Math.pow(this.goalX-this.player.getX(),2)+Math.pow(this.goalY-this.player.getY(),2));
        float xdirection = distX/Math.abs(distX);
        float ydirection = distY/Math.abs(distY);
        float velocityX = (((distX/distance))*speed)+((float)this.player.physics.getVelocityX()*xdirection);
        float velocityY = (((distY/distance))*speed)+((float)this.player.physics.getVelocityY()*ydirection);
        this.physics.setVelocityX(velocityX);
        this.physics.setVelocityY(velocityY);
    }
    Bullet(Player player,Enemies enemy, double time) throws FileNotFoundException{
        //Computes the velocity for the bullet to head to its target, then adds target's speed as well for tracking
        super(enemy.getX()+(enemy.width/2), enemy.getY()-2, 20, 20, "Bullet");
        Random tracking = new Random();
        this.enemy = enemy;
        this.player = player;
        float speed = 40;
        this.goalX = ((float)player.getX());
        this.goalY = ((float)player.getY());
        float distX = this.goalX-(float)this.enemy.getX();
        float distY = this.goalY-(float)this.enemy.getY();
        float distance = (float) Math.sqrt(Math.pow(this.goalX-this.enemy.getX(),2)+Math.pow(this.goalY-this.enemy.getY(),2));
        float xdirection = distX/Math.abs(distX);
        float ydirection = distY/Math.abs(distY);
        float velocityX = (((distX/distance))*speed)+((float)this.player.physics.getVelocityX()*((float)time*tracking.nextInt(20))); //tracking is made random so they don't just shoot in one direction, and be predictable.
        float velocityY = (((distY/distance))*speed)+((float)this.player.physics.getVelocityY()*((float)time*3)*ydirection);
        this.physics.setVelocityX(velocityX);
        this.physics.setVelocityY(velocityY);
    
    }
}
