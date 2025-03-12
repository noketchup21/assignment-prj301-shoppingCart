package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mylib.DBUtils;

/**
 *
 * @author THINH
 */
public class GamesDAO {
    
    public static List<GamesDTO> list(String keyword, String sortCol) {
        List<GamesDTO> gameList = new ArrayList<GamesDTO>();
        try {
            Connection con = DBUtils.getConnection();
            String sql = "SELECT gameId, title, description, price, publisher, releaseDate, coverImageUrl, isDlc FROM games";
            if (keyword != null && !keyword.isEmpty()) {
                sql += " WHERE title like ?";
            }
            if (sortCol != null && !sortCol.isEmpty()) {
                sql += " ORDER BY " + sortCol + " ASC";
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            if (keyword != null && !keyword.isEmpty()) {
                stmt.setString(1, "%" + keyword + "%");
            }
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int gameId = rs.getInt("gameId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String publisher = rs.getString("publisher");
                Date releaseDate = rs.getDate("releaseDate");
                String coverImageUrl = rs.getString("coverImageUrl");
                int isDlc = rs.getInt("isDlc");
                
                GamesDTO games = new GamesDTO();
                games.setTitle(title);
                games.setDescription(description);
                games.setGameId(gameId);
                games.setPrice(price);
                games.setPublisher(publisher);
                games.setReleaseDate(releaseDate);
                games.setUrl(coverImageUrl);
                games.setIsDLC(isDlc);
                
                gameList.add(games);
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error in servlet. Details:" + ex.getMessage());
            ex.printStackTrace();
        }
        return gameList;
    }
    
    public GamesDTO getGameById(int gameId) {
        GamesDTO game = null;
        try {
            Connection con = DBUtils.getConnection();
            String sql = "SELECT gameId, title, description, price, publisher, releaseDate, isDLC FROM games WHERE gameId = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String publisher = rs.getString("publisher");
                Date releaseDate = rs.getDate("releaseDate");
                int isDLC = rs.getInt("isDLC");
                
                game = new GamesDTO(gameId, title, description, publisher, price, releaseDate, isDLC);
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error in getGameById. Details: " + ex.getMessage());
            ex.printStackTrace();
        }
        return game;
    }

    /*
    
        public StudentDTO load(int id){

        String sql = "select id, firstname, lastname, age from student where id = ?";
    
        try {
            
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);                      
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                                    
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    int age = rs.getInt("age");

                    StudentDTO student = new StudentDTO();
                    student.setId(id);
                    student.setFirstname(firstname);
                    student.setLastname(lastname);
                    student.setAge(age);
                    return student;
            }
        }
        catch (SQLException ex) {
            System.out.println("Query Student error!" + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    
     */
    public static GamesDTO load(int id) {
        String sql = "SELECT title, price, coverImageUrl from games where gameId = ?";
        
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String title = rs.getString("title");
                double price = rs.getDouble("price");
                String coverImageUrl = rs.getString("coverImageUrl");
                GamesDTO game = new GamesDTO();
                
                game.setGameId(id);
                game.setTitle(title);
                game.setPrice(price);
                game.setUrl(coverImageUrl);
                
                return game;
            }
            
        } catch (SQLException ex) {
            System.out.println("Query Student error!" + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    //   public static GamesDTO getGameQuantity(int id)
    
}
