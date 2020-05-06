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
    Level currentLevel;
    double cooldownTimer = 5;
    double cooldown = cooldownTimer;
    public Enemies(int x, int y, int width, int height, String name, Level currentLevel) throws FileNotFoundException {
        super(x, y, width, height, name);
        this.currentLevel = currentLevel;
        this.setVisual(directory+"\\GameArt\\enemy.png");
        
    }
    public void findPlayer(Player player) throws FileNotFoundException{
        
    }
}
