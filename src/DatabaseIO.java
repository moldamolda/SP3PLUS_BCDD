import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseIO {

    // database URL
    static final String DB_URL = "jdbc:mysql://localhost/world";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "2ke&Qa+H_YM*Pa,";

    User user = new User("","");


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
                String sql = "SELECT * FROM streaming.movie";
                stmt = conn.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery(sql);

                // Extract data from the result set
                while (rs.next()) {
                    // Retrieve by column name
                    String name = rs.getString("title");
                    String year = rs.getString("year");
                    String genre = rs.getString("genre");
                    ArrayList<String> category = new ArrayList<>(Arrays.asList(genre));
                    double rating = rs.getDouble("rating");
                    Movie movie = new Movie(name, category, year, rating);
                    mediaList.add(movie);
                }
            } else {
                String sql = "SELECT * FROM streaming.serie";
                stmt = conn.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery(sql);

                // STEP 4: Extract data from the result set
                while (rs.next()) {
                    // Retrieve by column name
                    String name = rs.getString("title");
                    String genre = rs.getString("genre");
                    String year = rs.getString("year");
                    String seasons = rs.getString("season");
                    double rating = rs.getDouble("rating");
                    ArrayList<String> allgenres = new ArrayList<>(Arrays.asList(genre.split(",")));
                    ArrayList<String> allseasons = new ArrayList<>(Arrays.asList(seasons.split(",")));
                    Serie series = new Serie(name, allgenres, year, rating, allseasons);
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

    public void writeData(String username, String password) {
        // database URL
        // interface der snakker med databaser
        //Snablen ned i databasen:
        Connection conn = null;
        // sql statement:
        PreparedStatement stmt = null;

        try {
            //STEP 1: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            System.out.println("Creating statement...");
            String sql = "insert into streaming.user (username,password) values (?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);



            // resultat vi får af SQL'e :

           stmt.executeUpdate();


            //STEP 4: Extract data from result set

            //STEP 5: Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
    }




}
