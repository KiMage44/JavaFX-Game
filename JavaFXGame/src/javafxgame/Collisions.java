/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author tchoa
 */
public class Collisions {
    int gamewidth;
    int gameheight;
    Collisions(int gamewidth, int gameheight) {
        this.gamewidth = gamewidth;
        this.gameheight = gameheight;
        
    }
    //Collisions between Entities and the Player
    public void Entity_EntityCollisions(Player entity, Entity collider){
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;
        boolean finishline = false;
        boolean hitX = false;
        boolean hitY = false;
        if(collider.name == "FinishLine")
            finishline = true;
        //System.out.println("PlayerX: "+entity.getX()+" PlayerY: "+entity.getY());
        //System.out.println("EntityX: "+collider.getX()+" EntityY "+collider.getY());
        //System.out.println();
        if(entity.getLeftSide() < collider.getRightSide() && entity.getLeftSide() > collider.getLeftSide())
        {left = true;}
        if(entity.getRightSide() < collider.getRightSide() && entity.getRightSide() > collider.getLeftSide())
        {right = true;}
        if(entity.getTop() < collider.getBottom() && entity.getTop() > collider.getTop())
        {top = true;}
        if(entity.getBottom() < collider.getBottom() && entity.getBottom() > collider.getTop())
        {bottom = true;}
        //System.out.println("Left:"+left+" Right:"+right+" Top:"+top+"Bottom:"+bottom);
        //System.out.println("hitX: "+hitX+" hitY: "+hitY);
        //System.out.println();
        if(left || right){
            if(bottom || top){
                if(finishline)
                    entity.hitfinish = true;
                else{  
                    entity.setHealth(entity.getHealth()-10);
                    //System.out.println("Player collision.");
                    if(left && right){
                        if(top){
                            entity.setY(entity.getY()+5);
                            entity.physics.setVelocityY(10);}//System.out.println("Top-center");
                        else if(bottom){
                            entity.setY(entity.getY()-10);
                            entity.physics.setVelocityY(-10+(Math.abs(collider.physics.getVelocityY())*-1));}//System.out.println("Bottom-center");
                    }
                    else if(bottom && top){
                        if(right){
                            entity.setX(entity.getX()-5);
                            entity.physics.setVelocityX(-10);}//System.out.println("Right-center");
                        else if(left){
                            entity.setX(entity.getX()+5);
                            entity.physics.setVelocityX(10+(Math.abs(collider.physics.getVelocityY())));}//System.out.println("left-center");
                    }
                    else if(bottom && right)
                    {
                        entity.setX(entity.getX()-5);
                        entity.setY(entity.getY()-5);
                        entity.physics.setVelocityX(-10);
                        entity.physics.setVelocityY(-10);}//System.out.println("bottom-right corner");
                    else if(bottom && left)
                    {
                        entity.setX(entity.getX()+5);
                        entity.setY(entity.getY()-5);
                        entity.physics.setVelocityX(10);
                        entity.physics.setVelocityY(-10);}//System.out.println("bottom-left corner");
                    else if(top && right)
                    {
                        entity.setX(entity.getX()-5);
                        entity.setY(entity.getY()+5);
                        entity.physics.setVelocityX(-10);
                        entity.physics.setVelocityY(10);}//System.out.println("top-right corner");
                    else if(top && left)
                    {
                        entity.setX(entity.getX()+5);
                        entity.setY(entity.getY()+5);
                        entity.physics.setVelocityX(10);
                        entity.physics.setVelocityY(10);}//System.out.println("top-left corner");
                }
                
            }
        }
    }
    public void Bullet_EntityCollisions(Bullet entity, Entity collider){
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;
        if(entity.getLeftSide() < collider.getRightSide() && entity.getLeftSide() > collider.getLeftSide())
        {left = true;}
        if(entity.getRightSide() < collider.getRightSide() && entity.getRightSide() > collider.getLeftSide())
        {right = true;}
        if(entity.getTop() < collider.getBottom() && entity.getTop() > collider.getTop())
        {top = true;}
        if(entity.getBottom() < collider.getBottom() && entity.getBottom() > collider.getTop())
        {bottom = true;}
        
        if(left || right){
            if(bottom || top){
                //System.out.println("Bullet hit target");
                collider.physics.setVelocityY(collider.physics.getVelocityY()*0.7);
                if(collider.name != "Enemy"){
                entity.setX(-1000);
                entity.setY(-1000);
                entity.physics.setVelocityX(0);
                entity.physics.setVelocityY(0);
                }
            }
        }
        //System.out.println("BulletX"+entity.getX());
        //System.out.println("BulletY"+entity.getY());
    }
    public void Bullet_PlayerCollisions(Bullet entity, Player collider){
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;
        if(entity.getLeftSide() < collider.getRightSide() && entity.getLeftSide() > collider.getLeftSide())
        {left = true;}
        if(entity.getRightSide() < collider.getRightSide() && entity.getRightSide() > collider.getLeftSide())
        {right = true;}
        if(entity.getTop() < collider.getBottom() && entity.getTop() > collider.getTop())
        {top = true;}
        if(entity.getBottom() < collider.getBottom() && entity.getBottom() > collider.getTop())
        {bottom = true;}
        
        if(left || right){
            if(bottom || top){
                System.out.println("Bullet hit player");
                collider.physics.setVelocityX(0);
                collider.setHealth(collider.getHealth()-30);
                entity.setX(-1000);
                entity.setY(-1000);
                entity.physics.setVelocityX(0);
                entity.physics.setVelocityY(0);
            }
        }
        //System.out.println("BulletX"+entity.getX());
        //System.out.println("BulletY"+entity.getY());
    }
    public boolean Entity_WallCollisions(Entity entity){
        boolean hit = false;
        if(entity.getLeftSide() < 0){
            entity.setX(0);
            entity.physics.setAccelerationX(-entity.physics.getVelocityX());
            entity.physics.setVelocityX(0);
            hit = true;
        }
        if(entity.getRightSide() > gamewidth){
            entity.setX(gamewidth-entity.width);
            entity.physics.setVelocityX(-entity.physics.getVelocityX());
            entity.physics.setAccelerationX(0);
            hit = true;
        }
        if(entity.getTop() < 0){
            //System.out.println("Object "+entity.name+" hit top");
            entity.setY(0);
            entity.physics.setVelocityY(-entity.physics.getVelocityY());
            entity.physics.setAccelerationY(0);
            hit = true;
        }
        if(entity.getBottom() > gameheight){
            //System.out.println("Object "+entity.name+" hit bottom");
            entity.setY(gameheight-entity.height);
            entity.physics.setVelocityY(-entity.physics.getVelocityY());
            entity.physics.setAccelerationY(0);
            hit = true;
        }
        return hit;
    }
    public boolean Entity_WallCollisions(Player entity){
        boolean hit = false;
        if(entity.getLeftSide() < 0){
            entity.setX(0);
            entity.physics.setAccelerationX(-entity.physics.getAccelerationX());
            entity.physics.setVelocityX(0);
            hit = true;
        }
        if(entity.getRightSide() > gamewidth){
            entity.setX(gamewidth-entity.width);
            entity.physics.setAccelerationX(-entity.physics.getAccelerationX());
            entity.physics.setVelocityX(0);
            hit = true;
        }
        if(entity.getTop() < 0){
            entity.setY(0);
            entity.physics.setAccelerationY(-entity.physics.getAccelerationY());
            entity.physics.setVelocityY(0);
            hit = true;
        }
        if(entity.getBottom() > gameheight){
            entity.setY(gameheight-entity.height);
            entity.physics.setAccelerationY(-entity.physics.getAccelerationY());
            entity.physics.setVelocityY(0);
            hit = true;
        }
        return hit;
    }
    public void Entity_WallCollisions(Bullet entity){
        boolean hit = false;
        if(entity.getLeftSide() < 0){
            hit = true;
        }
        if(entity.getRightSide() > gamewidth){
            hit = true;
        }
        if(entity.getTop() < 0){
            hit = true;
        }
        if(entity.getBottom() > gameheight){
            hit = true;
        }
        if(hit){
            entity.setX(-1000);
            entity.setY(-1000);
            entity.physics.setVelocityX(0);
            entity.physics.setVelocityY(0);
        }
    }
    public void updateAI(Level currentLevel, Enemies collider, double time) throws FileNotFoundException{
        collider.cooldown = collider.cooldown-time;
        //System.out.println("cooldown to fire: "+collider.cooldown);
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;
        if(currentLevel.camera.player.getLeftSide() < collider.getRightSide() && currentLevel.camera.player.getLeftSide() > collider.getLeftSide())
        {left = true;}
        if(currentLevel.camera.player.getRightSide() < collider.getRightSide() && currentLevel.camera.player.getRightSide() > collider.getLeftSide())
        {right = true;}
        if(currentLevel.camera.player.getTop() < collider.getBottom() && currentLevel.camera.player.getTop() > collider.getTop())
        {top = true;}
        if(currentLevel.camera.player.getBottom() < collider.getBottom() && currentLevel.camera.player.getBottom() > collider.getTop())
        {bottom = true;}
        
        if(left || right){
            if(collider.cooldown <= 0){
                collider.cooldown = collider.cooldownTimer;
                System.out.println("Player detected above enemy, firing bullet.");
                currentLevel.createBullet(collider,time);
            }
        }
    }
}
