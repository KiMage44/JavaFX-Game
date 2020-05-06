/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import static java.lang.System.exit;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Physics {
    double mass = 2.0;
    double accelerationX;
    double accelerationY;
    float velocityX;
    StringProperty xvel = new SimpleStringProperty();
    float velocityY;
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
        {this.velocityX = (float) value; xvel.set(String.valueOf((int)value));}
    public synchronized double getVelocityX()
        {return this.velocityX;}
    
    public synchronized void setVelocityY(double value)
        {this.velocityY = (float) value;}
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
}