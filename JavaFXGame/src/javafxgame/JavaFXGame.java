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
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
    int gamewidth = 20000;
    int gameheight = 1200;
    String directory = System.getProperty("user.dir");
    String extra = "\\src\\javafxgame\\";
    boolean completed = false;
    boolean started;
    boolean failed;
    boolean introshown = false;
    boolean eventsHandled = false;
    int score = 0;
    int difficulty = 1;
    double firstTime;
    
    IntegerProperty currentPane = new SimpleIntegerProperty(); // Main Menu = 0, currentLevel = 1, Pause = 2, Endscreen = 3, introscreen = 4
    ArrayList<Menu> menus = new ArrayList<Menu>();
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        System.out.println(directory);
        this.stage = primaryStage;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        this.screenwidth = (int) primaryScreenBounds.getWidth();
        this.screenheight = (int) primaryScreenBounds.getHeight()-100;
        this.currentPane.set(0);
        this.scene = new Scene(createMainMenu(null),screenwidth,screenheight);
        firstLoop(null);
        
    }
    public static void main(String[] args) {
        launch(args);
    }
    //The initial setup for the scene. I put it into its own method so the game can easily return to the menu when needed
    private void firstLoop(Level currentLevel) throws FileNotFoundException{
        //System.out.println(this.scene.getRoot());
        this.scene.setRoot(createMainMenu(currentLevel));
        //Listener for difficulty selection in the main menu. runs level at difficulty chosen
        this.currentPane.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
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
                    if(currentLevel == null)
                        gameLoop(levelCreate());
                    else
                        gameLoop(levelSwap(currentLevel));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }
    //Creates the level, then returns it to the scene that needed it.
    private Level levelCreate() throws FileNotFoundException{
        Level levelCreate = new Level(gamewidth, gameheight,this.screenheight, this.directory);
        String backgroundPath = null;
        //Generic Background setup for the level. 
        //The main differences between each level are the types, number, and positioning of enemies, based on the difficulty of the level.
        if(difficulty == 1)
            backgroundPath = "\\GameArt\\Background.png";
        else if(difficulty == 2)
            backgroundPath = "\\GameArt\\Background2.png";
        else if(difficulty == 3)
            backgroundPath = "\\GameArt\\Background.png";
        levelCreate.setBackground(backgroundPath);
        //Creates the camera and players that are required for every level.
        levelCreate.createEntity(0, 0, 1920, 1080 , "Camera");
        levelCreate.createEntity(120, (int) (levelCreate.camera.getY()+(levelCreate.camera.height/2)), 50, 50, "Player"); 
        levelCreate.camera.createGUI(screenwidth,screenheight, this.score);
        //Generates enemies in random positions based on difficulty. Random generation is for replayability, as well as to keep it fresh.
        generateEnemies(levelCreate);
        
        //Different finish line for the 3rd level. Spoiler, you die regardless in this one.
        if(difficulty == 3){
            Entity finish = levelCreate.createEntity((int) (gamewidth*0.9), 0, 100, gameheight, "FinishLine");
            finish.setVisual(this.directory+"\\GameArt\\laval.png");
        }
        else
            levelCreate.createEntity((int) (gamewidth*0.9), 0, 100, gameheight, "FinishLine");
        levelCreate.buildVisuals();
        return levelCreate;
    }
    private Level levelSwap(Level currentLevel) throws FileNotFoundException{
        currentLevel.destroyLevel();
        return levelCreate();
    }
    //Generator for enemies in given level. # and types change as difficulty increases.
    private void generateEnemies(Level level) throws FileNotFoundException{
        Random rand = new Random();
        Entity rotated;
        int enemyCount = 30;
        int distBetwEntity = 700;
        for(int i = 1; i <= 15*difficulty; i++){
            int x = rand.nextInt(distBetwEntity*(i+1))+(distBetwEntity*i);
            int y = rand.nextInt(gameheight-(gameheight/10))+((gameheight/10)-100);
            int width = rand.nextInt(100)+50;
            int height = rand.nextInt(100)+50;
            int yVel = rand.nextInt(90)+10;
            int enemyType = rand.nextInt(10);
            if(difficulty == 1)
                level.createEntity(x, y, width, height,"Barricade");
            else if(difficulty == 2){
                if(enemyType%2 == 0)
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
                    level.createEntity(x,screenheight-50,width,height,"Enemy");
            }
        }
    }
    
    
    
    //The standard gameloop. 
    private void gameLoop(Level level) throws FileNotFoundException{
        //fail/succeed/intital states
        introshown = false;
        started = false;
        failed = false;
        completed = false;
        //Initial setup for the level prior to starting the actual loop.
        Level currentLevel = level;
        scene.setRoot(currentLevel.getPane());
        //Adds eventhandlers for independent camera/player movement and shooting function
        keyHandler CameraInput = new keyHandler(currentLevel.camera,currentLevel.camera.name,this.currentPane);
        keyHandler PlayerInput = new keyHandler(currentLevel.camera.player,currentLevel.camera.player.name);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, CameraInput);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, CameraInput);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, PlayerInput);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, PlayerInput);
        //System.out.println("Apple");
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //System.out.println("Potato"+currentLevel.camera.player.bulletCount);
                double x = event.getX();
                double y = event.getY();
                try {
                    
                    if(currentLevel.camera.player.bulletCount > 0){
                        currentLevel.createBullet(x,y);
                        //System.out.println("Future Candy");
                        currentLevel.camera.player.dropBulletCount();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        });
        stage.setScene(scene);
        stage.show();
        
        //The actual gameloop, stored inside an animationTimer.
        //I used the system time as the game timer instead of the animation's timer, made it easier to work with for calculating velocity.
        AnimationTimer animation = new AnimationTimer(){
            double lastTime;
            double currentTime;
            double elapsedTimeSeconds;
            double lastAnimation = 0;
            @Override
            //the actual gameloop
            public void handle(long now){
                //Check for death state
                if(currentLevel.camera.player.getHealth() <= 0){
                    System.out.println("Health has dropped below zero");
                    failed = true;
                }
                //check for fail state
                if(failed)
                {
                    this.stop();
                    currentPane.set(3);
                }
                //Check for first time through animation loop. Had to use WAAYY more globals than i wanted to, but it was the only way i could get around the inner
                //class restriction.
                if(!started){
                    firstTime = System.currentTimeMillis();
                    lastTime = firstTime;
                    started = true;
                    System.out.println("Program Started: "+firstTime);
                }
                //Shows the intro if the level has first been activated, for story purposes
                if(!introshown)
                    currentPane.set(4);
                currentTime = System.currentTimeMillis();
                //System.out.println("milis: "+currentTime);
                elapsedTimeSeconds = ((currentTime-lastTime)/100.0);
                //System.out.println(elapsedTimeSeconds);
                //updates locations of enemies
                updateLocations(currentLevel, elapsedTimeSeconds);
                //Check for wall collisions on all entities, then check for collisions between player/bullets/entities
                wallCollisions(currentLevel);
                EntityCollision(currentLevel);
                //handles enemies shooting function
                try {
                    updateAI(currentLevel, elapsedTimeSeconds);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                //updates the display to render each object in the visible scene.
                updateVisuals(currentLevel);
                //updates player animation for next frame. Unfortunatley, the player was the only thing i was able to animate.
                if(currentTime-lastAnimation > 50){
                    //System.out.println("Changing player image");
                    try {
                    updateAnimations(currentLevel);
                    } catch (FileNotFoundException ex) {
                    Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    lastAnimation = currentTime;
                    }
                //Checks for win state, goes to endscreen if player finishes race
                if(currentLevel.camera.player.hitfinish){
                    completed = true;
                    currentPane.set(3);}
                lastTime = currentTime;
            }
        };
        //Listener added on gameloop creation that allows pause/endgame functionality.
        currentPane.addListener(new ChangeListener() {                          //Changes pane for pause/fail states, and effectively halts gametime
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //System.out.println(currentPane);
                if(newValue.toString().equals("0")){
                    animation.stop();
                    try {
                        scene.setRoot(createMainMenu(currentLevel));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    currentPane.removeListener(this);
                }
                if(newValue.toString().equals("1")){
                    System.out.println("Start back up game");
                    started = false;
                    animation.start();
                }
                if(newValue.toString().equals("2")){
                    boolean eventsHandled = false;
                    animation.stop();
                    System.out.println("Stopped");
                    try {
                        scene.setRoot(createPauseMenu(currentLevel));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
                if(newValue.toString().equals("3")){
                    animation.stop();
                    currentPane.removeListener(this);
                    double completionTime = firstTime-System.currentTimeMillis();
                    try {
                        scene.setRoot(createEndScreen(currentLevel,completionTime));
                        
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
                if(newValue.toString().equals("4")){
                    animation.stop();
                    introshown = true;
                    try {
                        scene.setRoot(buildIntro(currentLevel));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }

                }
            }
        });
        animation.start();
    }
    
    //Adds the event handlers for the current scene
    //Updates the locations of all elements currently in the level.
    private void updateLocations(Level currentLevel, double time){
        for(int i = 0; i< currentLevel.entityList.size(); i++){
            currentLevel.entityList.get(i).updateLocation(time);
        }
        for(int i = 0; i<currentLevel.bullets.size(); i++)
            currentLevel.bullets.get(i).updateLocation(time);
        currentLevel.camera.updateLocation(time);
    }
    //Calculates reactions to objects, including the camera, colliding with a wall
    private void wallCollisions(Level currentLevel){
        Collisions collider = new Collisions(this.gamewidth,this.gameheight);
        for(int i = 0; i< currentLevel.entityList.size(); i++){
                boolean audio = collider.Entity_WallCollisions(currentLevel.entityList.get(i));
        }
        for(int i = 0;i < currentLevel.bullets.size();i++)
            collider.Entity_WallCollisions(currentLevel.bullets.get(i));
        currentLevel.camera.CamerawallCollisions(this.gamewidth, this.gameheight);
        boolean audio = collider.Entity_WallCollisions(currentLevel.camera.player);
    }
    //Handles player-entity, player-bullet,and bullet-entity collisions.
    private void EntityCollision(Level currentLevel){
        int counter;
        Collisions collider = new Collisions(this.gamewidth,this.gameheight);
        for(int i = 0; i< currentLevel.entityList.size(); i++){
            Entity currentEntity = currentLevel.entityList.get(i);
            collider.Entity_EntityCollisions(currentLevel.camera.player, currentEntity);
        }
        for(int i = 0; i< currentLevel.bullets.size(); i++){
            Bullet currentBullet = currentLevel.bullets.get(i);
            collider.Bullet_PlayerCollisions(currentBullet,currentLevel.camera.player);
            for(int z = 0; z< currentLevel.entityList.size(); z++){
                //System.out.println("Bullet "+i+" to entity "+z);
                Entity currentEntity = currentLevel.entityList.get(z);
                collider.Bullet_EntityCollisions(currentBullet, currentEntity);
                
            }
        }
        for(int i = 0; i< currentLevel.bullets.size(); i++){
            if(currentLevel.bullets.get(i).getX() == -1000){
                currentLevel.bullets.remove(currentLevel.bullets.get(i));i = 0;}
        }
    }
    //updates the actual shown graphics after collisions have been calculated
    private void updateVisuals(Level currentLevel){
        for(int i = 0; i< currentLevel.entityList.size(); i++){
                currentLevel.entityList.get(i).updateVisual();
            }
        currentLevel.camera.updateVisual();
    }
    //updates enemy shooting AI
    private void updateAI(Level currentLevel, double time) throws FileNotFoundException{
        Collisions collider = new Collisions(this.gamewidth,this.gameheight);
        for(int i = 0; i<currentLevel.enemyList.size(); i++){
            collider.updateAI(currentLevel,currentLevel.enemyList.get(i), time);
        }
    }
    //updates player animation.
    private void updateAnimations(Level currentLevel) throws FileNotFoundException{
        if(currentLevel.camera.player.physics.getVelocityX() != 0){
            if(currentLevel.camera.player.imageCounter < currentLevel.camera.player.imagelist.size()){
                currentLevel.camera.player.entityVisual.setImage(currentLevel.camera.player.imagelist.get(currentLevel.camera.player.imageCounter));
                currentLevel.camera.player.imageCounter++;
            }
            else
                currentLevel.camera.player.imageCounter = 0;
        }
        else
            currentLevel.camera.player.entityVisual.setImage(currentLevel.camera.player.imagelist.get(0));
    }
    
    
    
    //Generates the Main Menu shown at the start of the game
    private Pane createMainMenu(Level currentLevel) throws FileNotFoundException{
        if(score == 3)
            return createFinalScreen();
        Menu main = new Menu("Main",0,0,this.screenwidth,this.screenheight);
        ImageView backg = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Wall.png")));
        backg.setFitHeight(screenheight);
        backg.setFitWidth(screenwidth);
        main.getChildren().add(backg);
        
        Label theGauntlet = new Label();
        theGauntlet.setLayoutY(screenheight*0.3);
        theGauntlet.setPrefSize(300, 200);
        theGauntlet.setLayoutX((screenwidth/2)-theGauntlet.getPrefWidth()/2);
        theGauntlet.setText("The Gauntlet");
        theGauntlet.setFont(Font.font("CASTELLAR", 30));
        theGauntlet.setTextFill(Color.WHITE);
        HBox scoreBox = new HBox();
        scoreBox.setMaxWidth(100);
        scoreBox.setMaxHeight(30);
        scoreBox.setMinWidth(100);
        scoreBox.setMinHeight(30);
        scoreBox.setLayoutX(screenwidth*0.9);
        scoreBox.setLayoutY(0);
        for(int i = 0; i<score; i++){
            ImageView star = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\ScoreStar.png")));
            star.setFitHeight(50);
            star.setFitWidth(50);
            scoreBox.getChildren().add(star);
        }
        HBox buttonBox = new HBox();
        buttonBox.setPrefWidth(screenwidth);
        buttonBox.setPrefHeight(screenheight/10);
        buttonBox.setLayoutY(screenheight*0.7);
        buttonBox.setLayoutX(0);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(screenwidth*0.05);
        
        ImageView button1 = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Level1.png")));
        button1.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(1);
            }});
        
        ImageView button2 = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Level2.png")));
        button2.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(2);
            }});
        
        
        ImageView button3 = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Level3.png")));
        button3.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(3);
            }});
        if(score >=0)buttonBox.getChildren().add(button1);
        if(score >= 1)buttonBox.getChildren().add(button2);
        if(score >= 2)buttonBox.getChildren().add(button3);
        main.getChildren().add(theGauntlet);
        main.getChildren().add(scoreBox);
        main.getChildren().add(buttonBox);
        return main;
    }
    
    //Builds the story intro shown prior to every level.
    private Menu buildIntro(Level currentLevel) throws FileNotFoundException{
    Menu Intro = new Menu("Intro",0,0,this.screenwidth,this.screenheight);
    ImageView backg = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Wall.png")));
    backg.setLayoutX(0);
    backg.setLayoutY(0);
    backg.setFitHeight(this.screenheight);
    backg.setFitWidth(this.screenwidth);
    Intro.getChildren().add(backg);
    ImageView returnGame = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\PlayGame.png")));
        returnGame.setFitHeight(100);
        returnGame.setFitWidth(300);
        returnGame.setLayoutX((screenwidth/2)-(returnGame.getFitWidth()/2));
        returnGame.setLayoutY((screenheight*0.6));
        returnGame.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                
                scene.setRoot(currentLevel.getPane());
                currentPane.set(1);
            }
        });
    Label introText = new Label();
    introText.setLayoutX(0);
    introText.setLayoutY(screenheight*0.3);
    introText.setPrefSize(screenwidth, 300);
    introText.setAlignment(Pos.CENTER);
    introText.setFont(Font.font("CASTELLAR", 20));
    introText.setLineSpacing(20);
    introText.setTextFill(Color.WHITESMOKE);
    
    if(difficulty == 1){
        introText.setText("Hello, and welcome back to the Grant-Heissen testing facility. \nYou have been comfortable, I hope? \nWell, regardless, today is your first evaluation day,\n"
        + "\nso be sure to give it your all. Come back ạ̴̪͗l̴̲̏̾i̷͙̿̈v̸̡̱̂͋é̶͇, and maybe I will let you in on a secret.");
    }
    else if(difficulty == 2){
        introText.setText("Ah, it seems you've made it through our first little trial.\nSome, however, weren't as fortunate.\nOh well, we'll have to find more i suppose."
                + "\nAh yes, i promised you a secret. My favorite color is purple. Interesting, isn't it?\nWell, off you go. Enjoy the gauntlet!");
    }
    else if(difficulty == 3){
        introText.setText("You truly are a remarkable one.\n You've made it to our third trial! ALot of you have... We'll have to make this one a bit harder.\n Can't have just"
                + " any mutant back up on the streets, eh?");
    }
    Intro.getChildren().add(introText);
    Intro.getChildren().add(returnGame);
    return Intro;
    }
    //Generates the Pause screen shown when a player pauses the game
    private Pane createPauseMenu(Level currentLevel) throws FileNotFoundException{
        Menu pause = new Menu("Pause",0,0,this.screenwidth,this.screenheight);
        ImageView backg = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Wall.png")));
        backg.setFitHeight(screenheight);
        backg.setFitWidth(screenwidth);
        pause.getChildren().add(backg);
        
        ImageView pauseDisplay = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Paused.png")));
        pauseDisplay.setFitHeight(200);
        pauseDisplay.setFitWidth(600);
        pauseDisplay.setLayoutX((screenwidth/2)-(pauseDisplay.getFitWidth()/2));
        pauseDisplay.setLayoutY((screenheight*0.2));

        
        ImageView returnGame = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\ReturnToGame.png")));
        returnGame.setFitHeight(100);
        returnGame.setFitWidth(300);
        returnGame.setLayoutX((screenwidth/2)-(returnGame.getFitWidth()/2));
        returnGame.setLayoutY((screenheight*0.6));
        returnGame.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                
                scene.setRoot(currentLevel.getPane());
                currentPane.set(1);
            }
        });
        ImageView exitMenu = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\ExitMenu.png")));
        exitMenu.setFitHeight(100);
        exitMenu.setFitWidth(300);
        exitMenu.setLayoutX((screenwidth/2)-(exitMenu.getFitWidth()/2));
        exitMenu.setLayoutY((screenheight*0.7));
        exitMenu.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(0);
                try {
                    if(currentLevel == null)
                        firstLoop(null);
                    else
                        firstLoop(currentLevel);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        });
        ImageView exitGame = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\ExitGame.png")));
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
        
        pause.getChildren().add(pauseDisplay);
        pause.getChildren().add(exitGame);
        pause.getChildren().add(exitMenu);
        pause.getChildren().add(returnGame);
        return pause;
    }
    //Generates the End screen shown when a level is completed
    private Pane createEndScreen(Level currentLevel, double timeCompleted) throws FileNotFoundException{
        if(completed)
            score++;
        Menu end = new Menu("End",0,0,this.screenwidth,this.screenheight);
        ImageView backg = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Wall.png")));
        backg.setFitHeight(screenheight);
        backg.setFitWidth(screenwidth);
        end.getChildren().add(backg);
        
        //Shown on failure
        ImageView failed = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Failed.png")));
        failed.setFitWidth(500);
        failed.setFitHeight(100);
        failed.setLayoutX((screenwidth/2)-(failed.getFitWidth()/2));
        failed.setLayoutY(screenheight*0.3);
        
        //Win image, shown only on success
        ImageView win = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Win.png")));
        win.setFitWidth(500);
        win.setFitHeight(100);
        win.setLayoutX((screenwidth/2)-(win.getFitWidth()/2));
        win.setLayoutY(screenheight*0.1);
        
        VBox timesDisplay = new VBox();
        timesDisplay.setLayoutX(screenwidth*0.4);
        timesDisplay.setLayoutY(screenheight*0.4);
        timesDisplay.setPrefSize(400, 200);
        String[] names = {"John","Sarah","Rachel","Markus","Juan"};
        Label yourTime = new Label();
        yourTime.setText("You: "+timeCompleted+"Result: passed");
        timesDisplay.getChildren().add(yourTime);
        
        for(int i = 1; i<= 3-(score); i++){
            Label text = new Label();
            if(i == 5-(score-1))
                text.setText(names[i-1]+": "+ (timeCompleted-(100*i))+"Result: failed");
            else
                text.setText(names[i-1]+": "+ (timeCompleted-(100*i))+"Result: passed");
            timesDisplay.getChildren().add(text);
        }
        HBox scoreBox = new HBox();
        scoreBox.setMaxWidth(300);
        scoreBox.setMaxHeight(100);
        scoreBox.setMinWidth(300);
        scoreBox.setMinHeight(100);
        scoreBox.setPrefWidth(300);
        scoreBox.setPrefHeight(100);
        scoreBox.setLayoutX(screenwidth*0.9);
        scoreBox.setLayoutY(0);
        for(int i = 0; i<score; i++){
            ImageView star = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\ScoreStar.png")));
            star.setFitHeight(50);
            star.setFitWidth(50);
            scoreBox.getChildren().add(star);
        }
        
        ImageView exitMenu = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\ExitMenu.png")));
        exitMenu.setFitHeight(100);
        exitMenu.setFitWidth(300);
        exitMenu.setLayoutX((screenwidth/2)-(exitMenu.getFitWidth()/2));
        exitMenu.setLayoutY((screenheight*0.7));
        exitMenu.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(0);
                try {
                    if(currentLevel == null)
                        firstLoop(null);
                    else
                        firstLoop(currentLevel);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        });
        end.getChildren().add(exitMenu);
        if(completed){
            difficulty++;
            end.getChildren().add(scoreBox);
            end.getChildren().add(win);
            end.getChildren().add(timesDisplay);
        }
        else{
            end.getChildren().add(failed);
        }   
        return end;
    }
    //Shown after all levels have been beaten, the final ending for the game.
    private Pane createFinalScreen() throws FileNotFoundException{
        Menu finalM = new Menu("Final",0,0,this.screenwidth,this.screenheight);
        ImageView backg = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\Wall.png")));
        backg.setFitHeight(screenheight);
        backg.setFitWidth(screenwidth);
        finalM.getChildren().add(backg);
        
        Label finalText = new Label();
        finalText.setLayoutX(0);
        finalText.setLayoutY(screenheight*0.3);
        finalText.setPrefSize(screenwidth, 300);
        finalText.setAlignment(Pos.CENTER);
        finalText.setFont(Font.font("CASTELLAR", 20));
        finalText.setLineSpacing(20);
        finalText.setTextFill(Color.WHITESMOKE);
        finalText.setText("Ah, it seems you fell into the lava trap, didn't you?\n So desperate to win you didn't even consider whether it is was possible.\n"
                + "Well, to be honest, there was never any chance of escape. I lied. \nFor you, there's only one way to exit this game."
                + "But to do it, you'll have to leave this 'character' of yours behind. Hehe.");
        ImageView exitGame = new ImageView(new Image(new FileInputStream(this.directory+"\\GameArt\\ExitGame.png")));
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
        finalM.getChildren().add(exitGame);
        finalM.getChildren().add(finalText);
        
        return finalM;
    }
}

