package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import persistence.Database;
import persistence.HighScore;
import res.ResourceLoader;

public class Game {
    private final HashMap<Integer, GameLevel> gameLevels;
    private GameLevel gameLevel = null;
    private final Database database;
    private int solvedMaze; 
    
    public Game() {
        gameLevels = new HashMap<>();
        database = new Database();
        readLevels();
        solvedMaze = 0;
    }

    // ------------------------------------------------------------------------
    // The 'interesting' part :)
    // ------------------------------------------------------------------------

    public void loadGame(int gameID){
        gameLevel = new GameLevel(gameLevels.get(gameID));        
    }
    
    public void printGameLevel(){ gameLevel.printLevel(); }
    
    public boolean stepPlayer(Direction d){
        boolean stepped = (gameLevel.movePlayer(d));
        if (stepped && isLevelSolved()){
            solvedMaze++;
        } 
        return stepped;
    }
    
    public void stepDragon(){
        gameLevel.moveDragon();
    }
    
    public void scoreUpdate(String name){
        database.storeToDatabase(name);
    }
    // ------------------------------------------------------------------------
    // Getter methods
    // ------------------------------------------------------------------------

    public boolean isLevelLoaded(){ return gameLevel != null; }
    public int getLevelRows(){ return gameLevel.rows; }
    public int getLevelCols(){ return gameLevel.cols; }
    public LevelItem getItem(int row, int col){ return gameLevel.level[row][col]; }
    public int getGameID(){ return (gameLevel != null) ? gameLevel.gameID : null; }
    public boolean isSolved(){ return (gameLevel != null && gameLevel.isSolved()); }
    public Set<Integer> getLevels() { return gameLevels.keySet(); }
    public int getNumOfMazes() { return gameLevels.size(); }
    public int getSolvedMazes() { return solvedMaze; }
    public boolean isLevelSolved() { return gameLevel.isSolved(); }
    public boolean isGameOver() { return gameLevel.isGameOver(); }
    public Position getPlayerPos(){ // MAKE IT ~IMMUTABLE
        return new Position(gameLevel.player.x, gameLevel.player.y); 
    }
    public Position getDragonPos(){
        return new Position(gameLevel.dragon.x, gameLevel.dragon.y); 
    }
    
    public ArrayList<HighScore> getHighScores() {
        return database.getHighScores();
    }
    
    // ------------------------------------------------------------------------
    // Utility methods to load game levels from res/levels.txt resource file.
    // ------------------------------------------------------------------------

    private void readLevels(){
        InputStream is;
        is = ResourceLoader.loadResource("res/levels.txt");
        
        try (Scanner sc = new Scanner(is)){
            String line = readNextLine(sc);
            ArrayList<String> gameLevelRows = new ArrayList<>();
            
            while (!line.isEmpty()){
                int id = readGameID(line);
                if (id == -1) return;

                gameLevelRows.clear();
                line = readNextLine(sc);
                while (!line.isEmpty() && line.trim().charAt(0) != ';'){
                    gameLevelRows.add(line);                    
                    line = readNextLine(sc);
                }
                addNewGameLevel(new GameLevel(gameLevelRows, id));
            }
        } catch (Exception e){
            System.out.println("Ajaj");
        }
    }
    
    private void addNewGameLevel(GameLevel gameLevel){
        if (!gameLevels.containsKey(gameLevel.gameID)){
            gameLevels.put(gameLevel.gameID, gameLevel);
        }
    }
  
    private String readNextLine(Scanner sc){
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()){
            line = sc.nextLine();
        }
        return line;
    }
    
    private int readGameID(String line){
        line = line.trim();
        if (line.isEmpty() || line.charAt(0) != ';') return -1;
        Scanner s = new Scanner(line);
        s.next(); // skip ';']
        if (!s.hasNextInt()) return -1;
        int id = s.nextInt();
        return id;
    }    
}
