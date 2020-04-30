/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import static java.lang.System.exit;

public class Physics {
    double mass = 2.0;
    double accelerationX;
    double accelerationY;
    double velocityX;
    double velocityY;
    double netForceX;
    double netForceY;
    Entity entity;
    Physics(Entity entity){
        this.entity = entity;
    }
    /*public synchronized void addForceX(double value)
        {this.netForceX += value;}
    public synchronized void addForceY(double value)
        {this.netForceY += value;}
    
    public synchronized void setForceX(double value)
        {this.netForceX = value;}
    public synchronized double getForceX()
        {return this.netForceX;}
    
    public synchronized void setForceY(double value)
        {this.netForceY = value;}
    public synchronized double getForceY()
        {return this.netForceY;}*/
    
    
    public synchronized void setAccelerationX(double value)
        {this.accelerationX = value;}
    public synchronized double getAccelerationX()
        {return this.accelerationX;}
    
    public synchronized void setAccelerationY(double value)
        {this.accelerationY = value;}
    public synchronized double getAccelerationY()
        {return this.accelerationY;}
    
    public synchronized void setVelocityX(double value)
        {this.velocityX = value;}
    public synchronized double getVelocityX()
        {return this.velocityX;}
    
    public synchronized void setVelocityY(double value)
        {this.velocityY = value;}
    public synchronized double getVelocityY()
        {return this.velocityY;}
    
    public synchronized void calculateNetX(double time){
        //System.out.println(time);
        //this.setAccelerationX(this.netForceX/this.mass);
        this.setVelocityX(this.getVelocityX()+(this.accelerationX*time));
        this.entity.setX(this.entity.getX()+this.velocityX*time);
        //if(this.entity.name == "Player")
            //System.out.println("Calculating physics for "+this.entity.name+": \nAcceleration: "+this.accelerationX+"\nVelocity: "+this.velocityX+"\nX: "+this.entity.getX()+" Time: "+time);
        //this.ResetForcesX();
    }
    public synchronized void calculateNetY(double time){
        //this.setAccelerationY(this.netForceY/this.mass);a
        this.setVelocityY(this.getVelocityY()+(this.accelerationY*time));
        this.entity.setY(this.entity.getY()+this.velocityY*time);
        //this.ResetForcesY();
    }
    
    private void ResetForcesX(){
        this.netForceX = 0;
    }
    private void ResetForcesY(){
        this.netForceY = 0;
    }
    public void wallCollisions(double gamewidth, double gameheight){
        if(entity.getLeftSide() < 0){
            this.entity.setX(0);
            this.entity.physics.setAccelerationX(-this.entity.physics.getAccelerationX());
            this.entity.physics.setVelocityX(0);
        }
        if(entity.getRightSide() > gamewidth){
            this.entity.setX(gamewidth-this.entity.width);
            this.entity.physics.setAccelerationX(-this.entity.physics.getAccelerationX());
            this.entity.physics.setVelocityX(0);
        }
        if(entity.getTop() < 0){
            this.entity.setY(0);
            this.entity.physics.setAccelerationY(-this.entity.physics.getAccelerationY());
            this.entity.physics.setVelocityY(0);
        }
        if(entity.getBottom() > gameheight){
            this.entity.setY(gameheight-this.entity.height);
            this.entity.physics.setAccelerationY(-this.entity.physics.getAccelerationY());
            this.entity.physics.setVelocityY(0);
        }
    }
    public int EntityCollision(Entity collider){
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;
        boolean collision = false;
        boolean finishline = false;
        String collisionSide = "none";
        if(collider.name == "FinishLine")
            finishline = true;
        if(this.entity.getLeftSide() < collider.getRightSide() && this.entity.getLeftSide() > collider.getLeftSide())
        {left = true;}
        if(this.entity.getRightSide() < collider.getRightSide() && this.entity.getRightSide() > collider.getLeftSide())
        {right = true;}
        if(this.entity.getTop() < collider.getBottom() && this.entity.getTop() > collider.getTop())
        {top = true;}
        if(this.entity.getBottom() < collider.getBottom() && this.entity.getBottom() > collider.getTop())
        {bottom = true;}
        //System.out.println("Left:"+left+" Right:"+right+" Top:"+top+"Bottom:"+bottom);

        if(left || right){
            if(top || bottom){
                System.out.println("Collision occured between "+this.entity.name+" and "+collider.name);
                this.entity.health -= 10;
                System.out.println("Player heatlh: "+this.entity.health);
                if(right && top && bottom)
                {
                    System.out.println("Player collided on right side.");
                    collisionSide = "right";
                        
                }
                else if(left && top && bottom)
                {
                    System.out.println("Player collided on left side.");
                    collisionSide = "left";
                }
                else if(top && right && left)
                {
                    System.out.println("Player collided on top side.");
                    collisionSide = "top";
                }
                else if(bottom && right && left)
                {
                    System.out.println("Player collided on bottom side.");
                    collisionSide = "bottom";
                    
                }
                else if(bottom && right){
                    System.out.println("Player collided on bottom-right corner.");
                    collisionSide = "bottom-right";
                }
                
                else if(top && right){
                    System.out.println("Player collided on top-right corner.");
                    collisionSide = "top-right"; 
                }
                else if(bottom && left){
                    System.out.println("Player collided on bottom-left corner.");
                    collisionSide = "bottom-left";
                    
                }
                else if(top && left){
                    System.out.println("Player collided on top-left corner.");
                    collisionSide = "top-left";
                }
                //this.setAccelerationX(0);
                //this.setVelocityX(-this.getVelocityX());
            }
        }
        if(collisionSide != "none"){
            if(finishline)
                return 1;
            else{
                switch(collisionSide){
                    case "right":
                        this.entity.setX(collider.getX()-this.entity.width);
                        this.setAccelerationX(0);
                        this.setVelocityX(-this.getVelocityX());
                    case "left":
                        this.entity.setX(collider.getRightSide());
                        this.setAccelerationX(0);
                        this.setVelocityX(-this.getVelocityX());
                    case "top":
                        this.entity.setY(collider.getBottom());
                        this.setAccelerationY(0);
                        this.setVelocityY(-this.getVelocityY());
                    case "bottom":
                        this.entity.setY(collider.getY()-this.entity.height);
                        this.setAccelerationY(0);
                        this.setVelocityY(-this.getVelocityY());
                    case "bottom-right":
                        this.entity.setY(collider.getY()-this.entity.height);
                        this.entity.setX(collider.getX()-this.entity.width);
                        this.setAccelerationX(0);
                        this.setAccelerationY(0);
                        this.setVelocityX(-this.getVelocityX());
                        this.setVelocityY(-this.getVelocityY());
                    case "top-right":
                        this.entity.setY(collider.getBottom());
                        this.entity.setX(collider.getLeftSide()-this.entity.width);
                        this.setAccelerationX(0);
                        this.setAccelerationY(0);
                        this.setVelocityX(-this.getVelocityX());
                        this.setVelocityY(-this.getVelocityY());
                    case "bottom-left":
                        this.entity.setY(collider.getY()-this.entity.height);
                        this.entity.setX(collider.getRightSide());
                        this.setAccelerationX(0);
                        this.setAccelerationY(0);
                        this.setVelocityX(-this.getVelocityX());
                        this.setVelocityY(-this.getVelocityY());
                    case "top-left":
                        this.entity.setY(collider.getBottom());
                        this.entity.setX(collider.getRightSide());
                        this.setAccelerationX(0);
                        this.setAccelerationY(0);
                        this.setVelocityX(-this.getVelocityX());
                        this.setVelocityY(-this.getVelocityY());
                }
            }
        }
        return 0;
    }
}
