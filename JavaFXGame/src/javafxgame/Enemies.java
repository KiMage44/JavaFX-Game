/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author tchoa
 */
public class Enemies extends Entity{
    ArrayList<Rectangle> eyes = new ArrayList<Rectangle>();
    public Enemies(int x, int y, int width, int height, String name, String directory) throws FileNotFoundException {
        super(x, y, width, height, name, directory);
        this.setVisual(directory+"\\src\\javafxgame\\GameArt\\Player.png");
        createEyes();
        System.out.println("New Enemy created at "+x+" "+y);
    }
    private void createEyes(){
        Rectangle top = new Rectangle(this.x,this.y-100,this.width,100);
        Rectangle bottom = new Rectangle(this.x,this.getBottom(),this.width,100);
        eyes.add(top);
        eyes.add(bottom);
    }
    public void findPlayer(Player player){
        if(eyes.get(0).getLayoutBounds().intersects(player.getX(), player.getY(), player.width, player.height)){
            System.out.println("Fire bullet");
        }
        if(eyes.get(1).getLayoutBounds().intersects(player.getX(), player.getY(), player.width, player.height)){
            System.out.println("Fire bullet");
        }
        
    }
}
