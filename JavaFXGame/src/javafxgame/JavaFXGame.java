/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

/* To Do:
    Get images for all entities
    create a frame cycle for entity animations
    Background music
    Rework Collisions
    Fix Obstacle Generation
    Make Enemies shoot bullets
*/
public class JavaFXGame extends Application {
    Stage stage;
    Scene scene;
    int screenwidth;
    int screenheight;
    int gamewidth;
    int gameheight;
    String directory = System.getProperty("user.dir");
    
    boolean started;
    boolean failed;
    int score;
    int difficulty = 3;
    
    IntegerProperty currentPane = new SimpleIntegerProperty(); // Main Menu = 0, currentLevel = 1, Pause = 2, Endscreen = 3
    ArrayList<Menu> menus = new ArrayList<Menu>();
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        this.stage = primaryStage;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        this.screenwidth = (int) primaryScreenBounds.getWidth();
        this.screenheight = (int) primaryScreenBounds.getHeight();
        
        createMainMenu();
        createPauseMenu();
        createEndScreen();
        this.currentPane.set(0);
        this.scene = new Scene(getScreen(null),screenwidth,screenheight);
        firstLoop();
        
    }
    public static void main(String[] args) {
        launch(args);
    }
    private void firstLoop(){
        this.currentPane.set(0);
        this.currentPane.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println("POGGERS");
                if(newValue.toString().equals("1")){
                    difficulty = 1;
                    currentPane.removeListener(this);
                }
                if(newValue.toString().equals("2")){
                    difficulty = 2;
                    currentPane.removeListener(this);
                }
                if(newValue.toString().equals("3")){
                    difficulty = 3;
                    currentPane.removeListener(this);
                }
                try {
                    gameLoop(levelOne());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }
    //Generates the Main Menu shown at the start of the game
    private void createMainMenu() throws FileNotFoundException{
        Menu main = new Menu("Main",0,0,this.screenwidth,this.screenheight);
        
        HBox buttonBox = new HBox();
        buttonBox.setPrefWidth(screenwidth);
        buttonBox.setPrefHeight(screenheight/10);
        buttonBox.setLayoutY(screenheight*0.7);
        buttonBox.setLayoutX(0);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(screenwidth*0.05);
        
        ImageView button1 = new ImageView(new Image(new FileInputStream(this.directory+"\\src\\javafxgame\\GameArt\\Level1.png")));
        button1.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(1);
            }});
        buttonBox.getChildren().add(button1);
        
        ImageView button2 = new ImageView(new Image(new FileInputStream(this.directory+"\\src\\javafxgame\\GameArt\\Level2.png")));
        button2.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(2);
            }});
        buttonBox.getChildren().add(button2);
        
        ImageView button3 = new ImageView(new Image(new FileInputStream(this.directory+"\\src\\javafxgame\\GameArt\\Level3.png")));
        button3.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(3);
            }});
        buttonBox.getChildren().add(button3);
        
        main.getChildren().add(buttonBox);
        menus.add(main);
    }
    //Generates the Pause screen shown when a player pauses the game
    private void createPauseMenu() throws FileNotFoundException{
        Menu pause = new Menu("Pause",0,0,this.screenwidth,this.screenheight);
        ImageView exitGame = new ImageView(new Image(new FileInputStream(this.directory+"\\src\\javafxgame\\GameArt\\ExitGame.png")));
        exitGame.setFitHeight(100);
        exitGame.setFitWidth(300);
        exitGame.setLayoutX((screenwidth/2)-(exitGame.getFitWidth()/2));
        exitGame.setLayoutY((screenheight*0.8));
        exitGame.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                System.exit(0);
            }
        });
        ImageView exitMenu = new ImageView(new Image(new FileInputStream(this.directory+"\\src\\javafxgame\\GameArt\\ExitMenu.png")));
        exitMenu.setFitHeight(100);
        exitMenu.setFitWidth(300);
        exitMenu.setLayoutX((screenwidth/2)-(exitMenu.getFitWidth()/2));
        exitMenu.setLayoutY((screenheight*0.6));
        exitMenu.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(0);
                firstLoop();
            }
        });
        
        pause.getChildren().add(exitGame);
        pause.getChildren().add(exitMenu);
        menus.add(pause);
    }
    //Generates the End screen shown when a level is completed
    private void createEndScreen(){
        Menu end = new Menu("End",0,0,this.screenwidth,this.screenheight);
        menus.add(end);
    }
    //Creates the first level, then adds it to the global list
    private Level levelOne() throws FileNotFoundException{
        this.gamewidth = 20000;
        this.gameheight = 1200;
        Level levelOne = new Level(gamewidth, gameheight, this.directory);
        String backgroundPath = null;
        if(difficulty == 1)
            backgroundPath = "\\src\\javafxgame\\GameArt\\Background.png";
        else if(difficulty == 2)
            backgroundPath = "\\src\\javafxgame\\GameArt\\Background2.png";
        else if(difficulty == 3)
            backgroundPath = "\\src\\javafxgame\\GameArt\\Background.png";
        levelOne.setBackground(backgroundPath);
        levelOne.createEntity(0, 0, 1920, 1080 , "Camera");
        levelOne.createEntity(30, (int) (levelOne.camera.getY()+(levelOne.camera.height/2)), 25, 25, "Player"); 
        levelOne.camera.createGUI(screenwidth,screenheight,score);
        generateEnemies(levelOne);
        levelOne.createEntity(500, 700, 100, 100, "Enemy");
        levelOne.buildVisuals();
        return levelOne;
    }
    /*Creates the second level, then adds it to the global list
    private void levelTwo() throws FileNotFoundException{
        this.gamewidth = 20000;
        this.gameheight = 1200;
        Level levelTwo = new Level(gamewidth, gameheight, this.directory);
        levelTwo.setBackground("\\src\\javafxgame\\GameArt\\Game.png");
        levelTwo.createEntity(0, 0, 1920, 1080 , "Camera");
        levelTwo.createEntity(30, (int) (levelTwo.camera.getY()+(levelTwo.camera.height/2)), 25, 25, "Player"); 
        levelTwo.camera.createGUI(screenwidth,screenheight, score);
        levelTwo.buildVisuals();
        levels.add(levelTwo);
    }
    //Creates the third level, then adds it to the global list
    private void levelThree() throws FileNotFoundException{
        this.gamewidth = 20000;
        this.gameheight = 1200;
        Level levelThree = new Level(gamewidth, gameheight, this.directory);
        levelThree.setBackground("\\src\\javafxgame\\GameArt\\Game.png");
        levelThree.createEntity(0, 0, 1920, 1080 , "Camera");
        levelThree.createEntity(30, (int) (levelThree.camera.getY()+(levelThree.camera.height/2)), 25, 25, "Player"); 
        levelThree.camera.createGUI(screenwidth,screenheight, score);
        levelThree.buildVisuals();
        levels.add(levelThree);
    }*/
    
    //When called, returns the currentPane that is to be displayed. Lets me easily switch between the pause screen and the normal game, and allows the 
    //option to switch in-between levels if desired
    private Pane getScreen(Level currentLevel){
        switch(currentPane.get()){
            case 0:
                return menus.get(0);
            case 1:
                return currentLevel.getPane();
            case 2:
                return menus.get(1);
            case 3:
                return menus.get(2);
        }
        return null;
    }
    private void generateEnemies(Level level) throws FileNotFoundException{
        Random rand = new Random();
        Entity rotated;
        int enemyCount = 30;
        int distBetwEntity = 700;
        for(int i = 0; i < 20; i++){
            int x = rand.nextInt(distBetwEntity*(i+1))+(distBetwEntity*i);
            int y = rand.nextInt(gameheight-(gameheight/10))+((gameheight/10)-100);
            int width = rand.nextInt(100)+50;
            int height = rand.nextInt(100)+50;
            int yVel = rand.nextInt(90)+10;
            int enemyType = rand.nextInt(10);
            if(difficulty == 1)
                level.createEntity(x, y, width, height,"Barricade");
            else if(difficulty == 2){
                if(enemyType == 2)
                    level.createEntity(x, y, width, height, "Moving_Barricade", 0, yVel);
                else
                    level.createEntity(x, y, width, height,"Barricade");
            }
            else if(difficulty == 3){
                if(enemyType == 0)
                    level.createEntity(x, y, width, height, "Moving_Barricade", 0, yVel);
                else if(enemyType == 1)
                    level.createEntity(x, y, width, height,"Barricade");
                else
                    level.createEntity(x,y,width,height,"Enemy");
            }
        }
    }
    //The standard gameloop
    private void gameLoop(Level level) throws FileNotFoundException{
        //fail/start states
        started = false;
        failed = false;
        Level currentLevel = level;
        scene.setRoot(currentLevel.getPane());
        //Adds eventhandlers for independent camera/player movement
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new keyHandler(currentLevel.camera, currentLevel.camera.name, this.currentPane));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new keyHandler(currentLevel.camera, currentLevel.camera.name, this.currentPane));
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new keyHandler(currentLevel.camera.player, currentLevel.camera.player.name));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new keyHandler(currentLevel.camera.player,currentLevel.camera.player.name));
        stage.setScene(scene);
        stage.show();
        
        AnimationTimer animation = new AnimationTimer(){
            double firstTime;
            double lastTime;
            double currentTime;
            double elapsedTimeSeconds;
           
            @Override
            //the actual gameloop
            public void handle(long now){
                if(currentLevel.camera.getHealth() <= 0){
                    failed = true;
                }
                if(failed)
                {
                    this.stop();
                    currentPane.set(3);
                }
                if(!started){
                    firstTime = System.currentTimeMillis();
                    lastTime = firstTime;
                    started = true;
                    System.out.println("Program Started: "+firstTime);
                }
                currentTime = System.currentTimeMillis();
                //System.out.println("milis: "+currentTime);
                elapsedTimeSeconds = ((currentTime-lastTime)/100.0);
                //System.out.println(elapsedTimeSeconds);
                updateLocations(currentLevel, elapsedTimeSeconds);
                wallCollisions(currentLevel);
                EntityCollision(currentLevel);
                updateAI(currentLevel);
                updateVisuals(currentLevel);
                lastTime = currentTime;
            }
        };
        currentPane.addListener(new ChangeListener() {                          //Changes pane for pause/fail states, and effectively halts gametime
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //System.out.println(currentPane);
                if(newValue.toString().equals("0")){
                    animation.stop();
                    scene.setRoot(getScreen(currentLevel));
                }
                if(newValue.toString().equals("1")){
                    started = false;
                    animation.start();
                    scene.setRoot(getScreen(currentLevel));
                }
                if(newValue.toString().equals("2")){
                    animation.stop();
                    System.out.println("Stopped");
                    scene.setRoot(menus.get(1));
                    System.out.println(scene.getRoot());
                }
                if(newValue.toString().equals("3")){
                    animation.stop();
                    buildEndscreen(failed);
                }
            }
        });
        animation.start();
    }
    //Builds the screen for end-scenarios
    private void buildEndscreen(boolean failed){
        Label result;
        if(failed){
            result = new Label("Failed");
        }
        else{
            result = new Label("Success");
        }
        result.setLayoutX(gamewidth/2);
        result.setLayoutY(gameheight/2);
        Pane endpane = getScreen(null);
        endpane.getChildren().add(result);
        scene.setRoot(endpane);
    }
    //Updates the locations of all elements currently in the level.
    private void updateLocations(Level currentLevel, double time){
        for(int i = 0; i< currentLevel.entityList.size(); i++){
            currentLevel.entityList.get(i).updateLocation(time);
        }
        currentLevel.camera.updateLocation(time);
    }
    //Calculates reactions to objects, including the camera, colliding with a wall
    private void wallCollisions(Level currentLevel){
        Collisions collider = new Collisions(this.gamewidth,this.gameheight);
        for(int i = 0; i< currentLevel.entityList.size(); i++){
                boolean audio = collider.Entity_WallCollisions(currentLevel.entityList.get(i));
        }
        currentLevel.camera.CamerawallCollisions(this.gamewidth, this.gameheight);
        boolean audio = collider.Entity_WallCollisions(currentLevel.camera.player);
        /*if(audio){
            AudioClip hitSound = new AudioClip(""); 
            hitSound.play();
        }*/
    }
    //Handles inter-entity collisions
    private void EntityCollision(Level currentLevel){
        Collisions collider = new Collisions(this.gamewidth,this.gameheight);
        for(int i = 0; i< currentLevel.entityList.size(); i++){
            Entity currentEntity = currentLevel.entityList.get(i);
            boolean audio = collider.Entity_EntityCollisions(currentLevel.camera.player, currentEntity);
        }
        /*if(audio){
            AudioClip hitSound = new AudioClip(""); 
            hitSound.play();
        }*/
    }
    //updates the actual shown graphics after collisions have been calculated
    private void updateVisuals(Level currentLevel){
        for(int i = 0; i< currentLevel.entityList.size(); i++){
                currentLevel.entityList.get(i).updateVisual();
            }
        currentLevel.camera.updateVisual();
    }
    private void updateAI(Level currentLevel){
        for(int i = 0; i<currentLevel.enemyList.size(); i++){
            currentLevel.enemyList.get(i).findPlayer(currentLevel.camera.player);
        }
    }
}
