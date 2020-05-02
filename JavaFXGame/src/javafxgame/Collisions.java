/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

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
    public boolean Entity_EntityCollisions(Entity entity, Entity collider){
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;
        boolean collision = false;
        boolean finishline = false;
        String collisionSide = "none";
        boolean hit = false;
        if(collider.name == "FinishLine")
            finishline = true;
        if(entity.getLeftSide() < collider.getRightSide() && entity.getLeftSide() > collider.getLeftSide())
        {left = true;hit = true;}
        if(entity.getRightSide() < collider.getRightSide() && entity.getRightSide() > collider.getLeftSide())
        {right = true;hit = true;}
        if(entity.getTop() < collider.getBottom() && entity.getTop() > collider.getTop())
        {top = true;hit = true;}
        if(entity.getBottom() < collider.getBottom() && entity.getBottom() > collider.getTop())
        {bottom = true;hit = true;}
        //System.out.println("Left:"+left+" Right:"+right+" Top:"+top+"Bottom:"+bottom);
        return hit;
    }
    public boolean Entity_EntityCollisions(Player entity, Entity collider){
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;
        boolean collision = false;
        boolean finishline = false;
        String collisionSide = "none";
        boolean hit = false;
        if(collider.name == "FinishLine")
            finishline = true;
        if(entity.getLeftSide() < collider.getRightSide() && entity.getLeftSide() > collider.getLeftSide())
        {left = true;hit = true;}
        if(entity.getRightSide() < collider.getRightSide() && entity.getRightSide() > collider.getLeftSide())
        {right = true;hit = true;}
        if(entity.getTop() < collider.getBottom() && entity.getTop() > collider.getTop())
        {top = true;hit = true;}
        if(entity.getBottom() < collider.getBottom() && entity.getBottom() > collider.getTop())
        {bottom = true;hit = true;}
        //System.out.println("Left:"+left+" Right:"+right+" Top:"+top+"Bottom:"+bottom);
        return hit;
    }
    public boolean Entity_WallCollisions(Entity entity){
        boolean hit = false;
        if(entity.getLeftSide() < 0){
            entity.setX(0);
            entity.physics.setAccelerationX(-entity.physics.getAccelerationX());
            entity.physics.setVelocityX(0);
            hit = true;
        }
        if(entity.getRightSide() > gamewidth){
            entity.setX(gamewidth-entity.width);
            entity.physics.setVelocityX(-entity.physics.getAccelerationX());
            entity.physics.setAccelerationX(0);
            hit = true;
        }
        if(entity.getTop() < 0){
            entity.setY(0);
            entity.physics.setVelocityY(-entity.physics.getAccelerationY());
            entity.physics.setAccelerationY(0);
            hit = true;
        }
        if(entity.getBottom() > gameheight){
            entity.setY(gameheight-entity.height);
            entity.physics.setVelocityY(-entity.physics.getAccelerationY());
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
}
