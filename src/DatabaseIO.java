import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseIO  {

    // database URL
    static final String DB_URL = "jdbc:mysql://localhost/world";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "Okayokayokay123!";


    public ArrayList<Media> readData(boolean ismovie) {
        ArrayList<Media> mediaList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // STEP 2: Open a connection
            System.out.println("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 3: Execute a query
            System.out.println("Creating statement...");
            if (ismovie) {
                String sql = "SELECT * FROM my_streaming.movie";
                stmt = conn.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery(sql);

                // Extract data from the result set
                while (rs.next()) {
                    // Retrieve by column name
                    String name = rs.getString("Name");
                    String year = rs.getString("year");
                    String genre = rs.getString("genre");
                    ArrayList<String> category = new ArrayList<>(Arrays.asList(genre));
                    double rating = rs.getDouble("rating");
                    Movie movie = new Movie(name, category, year, rating);
                    mediaList.add(movie);
                }
            } else {
                String sql = "SELECT * FROM my_streaming.series";
                stmt = conn.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery(sql);

                // STEP 4: Extract data from the result set
                while (rs.next()) {
                    // Retrieve by column name
                    String name = rs.getString("Name");
                    String genre = rs.getString("genre");
                    String year = rs.getString("year");
                    String seasons = rs.getString("Seasons");
                    double rating = rs.getDouble("rating");
                    ArrayList<String> allgenres = new ArrayList<>(Arrays.asList(genre.split(",")));
                    ArrayList<String> allseasons = new ArrayList<>(Arrays.asList(seasons.split(",")));
                    Serie series = new Serie(name,allgenres,year,rating,allseasons);
                    mediaList.add(series);
                }
            }

            // STEP 5: Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // Finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                // Nothing we can do
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return mediaList;
    }



}
