/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author tchoa
 */
public class JavaFXGame extends Application {
    int gamewidth;
    int gameheight;
    int screenwidth;
    int screenheight;
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        this.screenwidth = (int) primaryScreenBounds.getWidth();
        this.screenheight = (int) primaryScreenBounds.getHeight();
        //gameheight-(gameheight/5);
       levelOne(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }
    private void levelOne(Stage stage) throws FileNotFoundException{
        this.gamewidth = 20000;
        this.gameheight = 2000;
        Level levelOne = new Level(stage, screenwidth, screenheight, gamewidth, gameheight);
        levelOne.createEntity(5000, 400, 100, 50, "Object");
        levelOne.createEntity(700, 250, 300, 100, "Object");
        levelOne.createEntity(1200, 300, 50, 100, "Object");
        levelOne.createEntity(1050, 900, 70, 80, "Object");
        levelOne.createEntity(700, 450, 150, 30, "FinishLine");
        levelOne.display();
    }
    
}
