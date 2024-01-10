package model;

import java.util.ArrayList;
import java.util.Random;

public class GameLevel {
    public final int        gameID;
    public final int           rows, cols;
    public final LevelItem[][] level;
    public Position            player = new Position(0, 0);
    private boolean            playerReachedExit;
    private boolean            caught;
    public Position            dragon;
    private boolean            paused;
    private Direction          dragonDirection = null;
    
    public GameLevel(ArrayList<String> gameLevelRows, int gameID){
        this.gameID = gameID;
        int c = 0;
        for (String s : gameLevelRows) if (s.length() > c) c = s.length();
        rows = gameLevelRows.size();
        cols = c;
        level = new LevelItem[rows][cols];
        
        playerReachedExit = false;
        paused = false;
        caught = false;
        
        for (int i = 0; i < rows; i++){
            String s = gameLevelRows.get(i);
            for (int j = 0; j < s.length(); j++){
                switch (s.charAt(j)){
                    case '@': player = new Position(j, i);
                              level[i][j] = LevelItem.EMPTY; break;
                    case '#': level[i][j] = LevelItem.WALL; break;
                    
                    case 'E': level[i][j] = LevelItem.EXIT; break;
                    default:  level[i][j] = LevelItem.EMPTY; break;
                }
            }
        }
        dragon = randomEmptyPosition();
        level[dragon.x][dragon.y] = LevelItem.DRAGON;
    }

    public GameLevel(GameLevel gl) {
        gameID = gl.gameID;
        rows = gl.rows;
        cols = gl.cols;
        playerReachedExit = gl.playerReachedExit;
        paused = gl.paused;
        caught = gl.caught;
        
        level = new LevelItem[rows][cols];
        player = new Position(gl.player.x, gl.player.y);
        dragon = gl.randomEmptyPosition();
        for (int i = 0; i < rows; i++){
            System.arraycopy(gl.level[i], 0, level[i], 0, cols);
        }
    }
    
    private Position randomEmptyPosition(){
        ArrayList<Position> emptyCells = new ArrayList();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                LevelItem li = level[i][j];
                if (li == LevelItem.EMPTY) emptyCells.add(new Position(j, i));
            }
        }
        Random random = new Random();
        int randomInd = random.nextInt(emptyCells.size());
        return emptyCells.get(randomInd);
    }
    
    public void pause(){
        paused = !paused;
    }
    
    public boolean isPaused(){
        return paused;
    }
    
    private void dragonCatchPlayer(){
        caught = dragon.isNeibour(player);
    };
    
    public boolean isGameOver() { return caught; }

    public boolean isSolved(){
        return playerReachedExit;
    }
    
    public boolean isValidPosition(Position p){
        return (p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows);
    }
    
    public boolean isFree(Position p){
        if (!isValidPosition(p)) return false;
        LevelItem li = level[p.y][p.x];
        return li == LevelItem.EMPTY;
    }
    
    boolean isExit(Position p){
        if (!isValidPosition(p)) return false;
        LevelItem li = level[p.y][p.x];
        return li == LevelItem.EXIT;
    }
    
    public boolean movePlayer(Direction d){
        if (isSolved() || isPaused()) return false;
        Position curr = player;
        Position next = curr.translate(d);
        if (isExit(next)){
            playerReachedExit = true;
            return true;
        }
        else if (isFree(next)) {
            player = next;
            dragonCatchPlayer();
            return true;
        }
        return false;
    }
    
    public void moveDragon(){
        if (dragonDirection == null) dragonDirection = getRandomDirection();
        Position curr = dragon;
        Position next = curr.translate(dragonDirection);
        while (!isFree(next)) {
            dragonDirection = getRandomDirection();
            next = curr.translate(dragonDirection);
        }
        dragon = next;
        dragonCatchPlayer();
    }
    
    public Direction getRandomDirection(){
        int randomDirection = (int) Math.round(Math.random() * 3) + 1;
        Direction direction = null;
        switch (randomDirection) {
            case 1: direction = Direction.DOWN;
                break;
            case 2: direction = Direction.LEFT;
                break;
            case 3: direction = Direction.RIGHT;
                break;
            case 4: direction = Direction.UP;
        }
        return direction;
    }

    
    public void printLevel(){
        int x = player.x, y = player.y;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (i == y && j == x)
                    System.out.print('@');
                else 
                    System.out.print(level[i][j].representation);
            }
            System.out.println("");
        }
    }

}
