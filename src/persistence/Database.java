package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


public class Database {
    private final String tableName = "scores";
    private final Connection conn;
    private final HashMap<String, Integer> highScores;
    
    public Database(){
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost/scores?"
                    + "serverTimezone=UTC&user=root&password=root");
        } catch (Exception ex) {
            System.out.println("No connection");
        }
        this.conn = c;
        highScores = new HashMap<>();
        loadHighScores();
    }

    public ArrayList<HighScore> getHighScores(){
        ArrayList<HighScore> scores = new ArrayList<>();
        loadHighScores();
        for (String name : highScores.keySet()){
            HighScore h = new HighScore(name, highScores.get(name));
            scores.add(h);
            System.out.println(h);
        }
        return scores;
    }

    private void loadHighScores(){
        try (Statement stmt = conn.createStatement()) {
            highScores.clear();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " ORDER BY Score DESC LIMIT 10");
            while (rs.next()){
                String name = rs.getString("Name");
                int Score = rs.getInt("Score");
                highScores.put(name, Score );
            }
        } catch (Exception e){ System.out.println("loadHighScores error: " + e.getMessage());}
    }
 
    public int storeToDatabase(String name){
        try (Statement stmt = conn.createStatement()){
            String s = "INSERT INTO " + tableName + 
                    " (Name, Score) " + 
                    "VALUES('" + name+ "', " + 1 + 
                    ") ON DUPLICATE KEY UPDATE Score= Score + 1"  ;
            return stmt.executeUpdate(s);
        } catch (Exception e){
            System.out.println("storeToDatabase error");
        }
        
        return 0;
    }
    
    
}
