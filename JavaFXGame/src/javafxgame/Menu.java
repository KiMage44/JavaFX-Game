/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import javafx.scene.layout.Pane;

public class Menu extends Pane{
    Menu(String name, int x, int y, int width, int height){
        System.out.println("It must be nice");
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setLayoutX(x);
        this.setLayoutY(y);
    }
}
