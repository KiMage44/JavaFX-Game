/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    int endscreen = 0;
    private IntegerProperty currentScreen = new SimpleIntegerProperty(0);
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        this.screenwidth = (int) primaryScreenBounds.getWidth();
        this.screenheight = (int) primaryScreenBounds.getHeight();
        currentScreen.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue.toString().equals("1"))
                    try {
                        levelOne(primaryStage);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                else if(newValue.toString().equals("2"))
                    System.out.println("Congrats, you've managed to accomplish something.");
                else
                    System.out.println(newValue.toString());
            }
        });
        currentScreen.set(1);
        System.out.println("Ach");
    }
    public static void main(String[] args) {
        launch(args);
    }
    private void levelOne(Stage stage) throws FileNotFoundException{
        this.gamewidth = 20000;
        this.gameheight = 1200;
        Level levelOne = new Level(stage, screenwidth, screenheight, gamewidth, gameheight);
        //levelOne.createEntity(5000, 400, 100, 50, "Object",0,-20);
        //levelOne.createEntity(700, 250, 300, 100, "Object",0,-10);
        //levelOne.createEntity(1200, 300, 50, 100, "Object",0,50);
        //levelOne.createEntity(3000, 900, 50, 100, "Object",0,5);
        levelOne.createEntity((int) (gamewidth*0.8), 0, 100, gameheight, "FinishLine");
        levelOne.display();
    }
    private void Endgame(Stage stage){
        System.out.println("Potato");
        EndScreen screen = new EndScreen();
        Scene scene = new Scene(screen);
        stage.show();
        
    }
}
