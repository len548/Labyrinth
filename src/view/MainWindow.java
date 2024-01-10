package view;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import model.Direction;
import model.Game;

public class MainWindow extends JFrame{
    private final Game game;
    private Board board;
    private final JLabel gameStatLabel;
    private Timer timePassed;
    private Timer dragonTimer;
    private int dragonSpeed = 500;
    private JLabel timeLabel = new JLabel("00:00:0");
    
    public MainWindow() throws IOException{
        game = new Game();
        
        setTitle("Labyrinth");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("res/dragon.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Menu");
        JMenu menuGameLevel = new JMenu("Level");
        createGameLevelMenuItems(menuGameLevel);

        JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Ranking") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoreWindow(game.getHighScores(), MainWindow.this);
            }
        });
        
        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        JMenuItem menuGameRestart = new JMenuItem(new AbstractAction("Restart") {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart(game.getGameID());
            }
        });


        menuGame.add(menuGameLevel);
        menuGame.add(menuHighScores);
        menuGame.add(menuGameRestart);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);
        
        setLayout(new BorderLayout(0, 10));
        gameStatLabel = new JLabel("label");

        add(gameStatLabel, BorderLayout.NORTH);
        try { add(board = new Board(game), BorderLayout.CENTER); 
        } catch (IOException ex) {}
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke); 
                if (!game.isLevelLoaded()) return;
                int kk = ke.getKeyCode();
                Direction d = null;
                switch (kk){
                    case KeyEvent.VK_A:  d = Direction.LEFT; break;
                    case KeyEvent.VK_D: d = Direction.RIGHT; break;
                    case KeyEvent.VK_W:    d = Direction.UP; break;
                    case KeyEvent.VK_S:  d = Direction.DOWN; break;
                    case KeyEvent.VK_ESCAPE: restart(game.getGameID());
                }
                board.repaint();
                if (d != null && game.stepPlayer(d)){
                    if (game.isSolved()){
                        String msg = "Congratulations!";
                        JOptionPane.showMessageDialog(MainWindow.this, msg, "Game solved", JOptionPane.INFORMATION_MESSAGE);
                        endGame(true);
                    } 
                } 
            }
        });
        
        restart(1);
    }
 
    private void restart(int level){
        if (dragonTimer != null) dragonTimer.cancel();
        if (timePassed != null) timePassed.cancel();
        startTime();
        game.loadGame(level);
        board.refresh();
        scheduleDragon();
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    
    private void startTime(){
        timePassed = new Timer();
        timeLabel.setText("0:00:00");
        timePassed.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                String[] milSecHour = timeLabel.getText().split(":");
                int mili = Integer.parseInt(milSecHour[2]);
                int sec = Integer.parseInt(milSecHour[1]);
                int hour = Integer.parseInt(milSecHour[0]);

                mili += 1;

                if (mili == 10) 
                {
                    sec += 1;
                    mili = 0;
                }

                if (sec == 59) 
                {
                    hour += 1;
                    sec = 0;
                }

                timeLabel.setText(hour + ":" + (sec < 9 ? "0" + sec : sec) + ":" + mili);
                add(timeLabel, BorderLayout.NORTH);
            }
        }, 0, 100);
    }
    
    private void scheduleDragon(){
        dragonTimer = new Timer();
        dragonTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                game.stepDragon();
                board.repaint();
                if (game.isGameOver()){
                    String msg = "You are caught bythe Dragon!";
                    JOptionPane.showMessageDialog(MainWindow.this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    endGame(false);
                }
            }
        }, 500, dragonSpeed);
    }
    
    private void endGame(boolean win){
        dragonTimer.cancel();
        timePassed.cancel();
        if (win) {
            String name =  JOptionPane.showInputDialog("Enter your name:");
            game.scoreUpdate(name);
        }
        
    }
    
    private void createGameLevelMenuItems(JMenu menu){
        for (Integer i : game.getLevels()){
            JMenuItem item = new JMenuItem(new AbstractAction("Level-" + i) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    restart(i);
                }
            });
            menu.add(item);
        }
    }
  
    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {}
    }    
}
