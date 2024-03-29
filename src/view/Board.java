package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JPanel;
import model.Game;
import model.LevelItem;
import model.Position;
import res.ResourceLoader;

public class Board extends JPanel {
    private Game game;
    private final Image player, wall, empty, dark, dragon, exit;
    private double scale;
    private int scaled_size;
    private final int tile_size = 32;
    
    public Board(Game g) throws IOException{
        game = g;
        scale = 1.0;
        scaled_size = (int)(scale * tile_size);
        player = ResourceLoader.loadImage("res/player.png");
        wall = ResourceLoader.loadImage("res/wall.png");
        empty = ResourceLoader.loadImage("res/empty.png");
        dark = ResourceLoader.loadImage("res/dark.png");
        dragon = ResourceLoader.loadImage("res/dragon.png");
        exit = ResourceLoader.loadImage("res/exit.png");
    }
    
    public boolean setScale(double scale){
        this.scale = scale;
        scaled_size = (int)(scale * tile_size);
        return refresh();
    }
    
    public boolean refresh(){
        if (!game.isLevelLoaded()) return false;
        Dimension dim = new Dimension(game.getLevelCols() * scaled_size, game.getLevelRows() * scaled_size);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (!game.isLevelLoaded()) return;
        Graphics2D gr = (Graphics2D)g;
        int w = game.getLevelCols();
        int h = game.getLevelRows();
        Position p = game.getPlayerPos();
        Position d = game.getDragonPos();
        for (int y = 0; y < h; y++){
            for (int x = 0; x < w; x++){
                Image img = null;
                LevelItem li = game.getItem(y, x);
                switch (li){
                    case WALL: img = wall; break;
                    case EMPTY: img = empty; break;
                    case EXIT: img = exit; break;
                }
                if (d.x == x && d.y == y){
                    img = dragon;
                }
                if (p.x == x && p.y == y){
                    img = player;
                }
                if (!isVisible(x, y, p)){
                    img = dark;
                }
                if (img == null) continue;
                gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, null);
            }
        }
    }
    
    private boolean isVisible(int x, int y, Position p){
        return Math.abs(p.x - x) < 3 && Math.abs(p.y - y) < 3;
    }
}
