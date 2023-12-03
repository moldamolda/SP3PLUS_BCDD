import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Login {

    FileIO io = new FileIO();
    TextUI ui = new TextUI();
    Streamingservice s1;
    Scanner input = new Scanner(System.in);
    ArrayList<User> users = new ArrayList<>();
    DatabaseIO d1 = new DatabaseIO();

    public ArrayList<String> watchedMovies = new ArrayList<>();
    public ArrayList<String> watchedseries = new ArrayList<>();

    public ArrayList<String> savedmovies = new ArrayList<>();
    public ArrayList<String> savedseries = new ArrayList<>();

    private static final String userfile = "C:\\Users\\danie\\Desktop\\SP3PLUS_BCD\\src\\Users";
    private static final String seriesfile = "C:\\Users\\danie\\Desktop\\SP3PLUS_BCD\\src\\100bedsteserier";
    private static final String moviefile = "C:\\Users\\danie\\Desktop\\SP3PLUS_BCD\\src\\100bedstefilm";

    static String DB_URL;

    //  Database credentials
    static String USER;
    static String PASS;



    public void startMenu() {
        System.out.println("Welcome to betflmix");
        System.out.println("1) Create an account");
        System.out.println("2) login");
        try {
            int choice = Integer.parseInt(input.nextLine());
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                default:
                    System.out.println("Invalid choice please try again");
                    startMenu();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please type a number");
            startMenu();
        }
    }

    public boolean login() {
        String username = getUserInput("Enter your username: ");
        String password = getUserInput("Enter your password: ");
        //checking if the account is valid
        if (isValidAccount(username, password)) {
            System.out.println("login successful!");
            s1 = new Streamingservice(this);
            s1.displayStartMenu(); //displaying start menu if the login was successful

            return true;
        } else {
            System.out.println("Wrong login try again");
            login(); //recursion in the case that the login failed

        }
        return false;
    }

    public void searchByGenre(ArrayList<Media> mediaList, boolean isMovie) {
        System.out.println("Enter the name of the genre you're looking for: ");
        String input = this.input.nextLine().trim();
        boolean mediaFound = false; //creating a mediafound boolean that chooses the path of the genre

        for (Media media : mediaList)//For each loop that cycles through all media objects
        {
            for (String category : media.getCategory())// For each loop that cycles through all media category's
            {
                if (category.toLowerCase().contains(input.toLowerCase())) //Checking if the user's output is the same as a media object's category
                {
                    System.out.println(media);
                    mediaFound = true;
                    break;
                }
            }
        }

        if (!mediaFound) //Seeing whether the media doesn't exist in neither 100bedstefilm.txt or 100bedsteserier.txt
        {
            ui.mediaNotFound();
            searchByGenre(mediaList, isMovie);
        }

        System.out.println("Enter the title of the " + (isMovie ? "movie" : "series") + " you want to see"); //Using the Ternary operator to check whether the media object is a movie or series. If it's a movie then it prints out movie, its it's a series it prints out series.
        String chosenMedia = this.input.nextLine().trim();
        boolean chosenMediaFound = false;

        for (Media media : mediaList) {
            if (chosenMedia.equalsIgnoreCase(media.getTitle())) // Checking if the users output, equals a media objects title
            {
                System.out.println("Do you want to 1) watch the " + (isMovie ? "movie" : "series") + " or 2) save the " + (isMovie ? "movie" : "series")); //Using the Ternary for the same reason as i did before
                int choice = Integer.parseInt(this.input.nextLine());
                switch (choice) { //Switch cases that lets the user chose to either save the media or watch the media
                    case 1:
                        if (isMovie) {
                            ui.watchedMovie(this, media);
                        } else {
                            ui.watchedSerie(this, media);
                        }
                        chosenMediaFound = true;
                        break;
                    case 2:
                        ui.saveMedia(this, media);
                        chosenMediaFound = true;
                        break;
                }
                displayChoice();
            }
        }

        if (!chosenMediaFound) {
            ui.mediaNotFound();
            searchByGenre(mediaList, isMovie); //if the media isn't found, then let the user try again
        }
    }



    public void searchByName() {
        System.out.println("Enter the name of the movie or series you're looking for: ");
        String search = input.nextLine().trim();
        boolean mediaFound = false;

        ArrayList<Media> allMedia = new ArrayList<>();
        allMedia.addAll(d1.readData(true)); // Adding all movies to my ArrayList allMedia
        allMedia.addAll(d1.readData(false)); //Adding all series to my ArrayList allMedia

        for (Media media : allMedia) {
            if (search.equalsIgnoreCase(media.getTitle())) { // Checking if the users output, equals a media objects title
                System.out.println(media);
                mediaFound = true;

                System.out.println("Do you want to 1) watch or 2) save");

                int choice = Integer.parseInt(input.nextLine());
                switch (choice) { //Switch cases that lets the user chose to either save the media or watch the media
                    case 1:
                        if (media instanceof Movie) {
                            ui.watchedMovie(this, media);
                        } else if (media instanceof Serie) {
                            ui.watchedSerie(this, media);
                        }
                        break;
                    case 2:
                        ui.saveMedia(this, media);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                displayChoice();
            }
        }

        if (!mediaFound) {
            ui.mediaNotFound();
            searchByName();
        }
    }

    public void createAccount() {
        String username = getUserInput("Enter a username: ");
        String password = getUserInput("Enter a password: ");

        if (isValidAccount(username, password)) { //checking if the Data file already have matching username + passwords
            System.out.println("you already have an account, please login");
            login();  //recursion that lets the user try again if his/hers attempt to create an account failed
        } else {
            saveAccount(username, password); //Saving account if login worked
            //d1.user.add(username, password); //Adding the user to a list of users
            //users.addAll(d1.writeData()); // Adding all movies to my ArrayList allMedia
            System.out.println("Account created successfully.");
            startMenu(); //displaying start menu
        }
    }

    private static String getUserInput(String msg) { //method that displays a message and then scanning the line for a user input
        Scanner scanner = new Scanner(System.in);
        System.out.print(msg);
        return scanner.nextLine();
    }

    private void saveAccount(String username, String password) {

        try {
            d1.writeData(username, password);
            //(FileWriter writer = new FileWriter(userfile, true)) { //adding my filewriter with the users.txt file. If append is true it turns on append mode. The filewriter will write the data at the end of the file. If the file doesn't exist it will create a new file with the name given in the ()
            // writer.write(username + " ; " + password + "\n");
        } catch (Exception e) {
            System.out.println("couldn't save account");

            startMenu();

        }
    }

    private boolean isValidAccount(String username, String password) {
        // Database connection parameters
        DB_URL  = "jdbc:mysql://localhost/world";
        USER  = "root";
        PASS =  "2ke&Qa+H_YM*Pa,";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            System.out.println("Connecting to database to check if login is matching...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Execute a query
            String sql = "SELECT username, password FROM streaming.user WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Account exists in the database
                return true;
            } else {
                // Account does not exist in the database
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Handle errors
            e.printStackTrace();
            return false;
        } finally {
            // Close resources in the finally block
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    public void removeUser() {
        String username = getUserInput("Enter a username: ");
        String password = getUserInput("Enter a password: ");
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
            String sql = "delete from streaming.user where username = ? and password = ?";
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
        void displayChoice () {
            System.out.println("Do you 1) Want to shut down the program or 2) return to startpage");
            int menuChoice = Integer.parseInt(input.nextLine());
            switch (menuChoice) { //switch case that either shuts down the program or lets the user return to the start page
                case 1:
                    break;
                case 2:
                    s1.displayStartMenu();
            }
        }

}