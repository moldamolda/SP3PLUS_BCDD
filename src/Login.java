import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Login {

    FileIO io = new FileIO();
    TextUI ui = new TextUI();
    Streamingservice s1;
    Scanner scanner = new Scanner(System.in);
    ArrayList<User> users = new ArrayList<>();
    public ArrayList<String> watchedMovies = new ArrayList<>();
    public  ArrayList<String> watchedseries = new ArrayList<>();

    public ArrayList<String> savedmovies = new ArrayList<>();
    public  ArrayList<String> savedseries = new ArrayList<>();

    private static final String userfile = "C:\\Users\\danie\\IdeaProjects\\SP3-newnew\\Users.txt";
    private static final String seriesfile = "C:\\Users\\danie\\IdeaProjects\\SP3-newnew\\100bedsteserier.txt";
    private static final String moviefile = "C:\\Users\\danie\\IdeaProjects\\SP3-newnew\\100bedstefilm.txt";

    public static void main(String[] args) {
        Login login = new Login()    ;
        login.startmenu()   ;
    }

    public void startmenu() {
        System.out.println("Welcome to betflmix");
        System.out.println("1) Create an account");
        System.out.println("2) login");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                default:
                    System.out.println("Invalid choice please try again");
                    startmenu();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please type a number");
            startmenu();
        }
    }
    public boolean login() {
        String username = getUserInput("Enter your username: ");
        String password = getUserInput("Enter your password: ");
        //checking if the account is valid
        if (isValidAccount(username, password)) {
            System.out.println("login successful!");
            s1= new Streamingservice(this);
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
        String search = scanner.nextLine().trim();
        boolean mediaFound = false; //creating a mediafound boolean that chooses the path of the genre

        for (Media media : mediaList)//For each loop that cycles through all media objects
        {
            for (String category : media.getCategory())// For each loop that cycles through all media category's
            {
                if (search.equalsIgnoreCase(category.trim())) //Checking if the user's output is the same as a media object's category
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
        String chosenMedia = scanner.nextLine().trim();
        boolean chosenMediaFound = false;

        for (Media media : mediaList) {
            if (chosenMedia.equalsIgnoreCase(media.getTitle())) // Checking if the users output, equals a media objects title
            {
                System.out.println("Do you want to 1) watch the " + (isMovie ? "movie" : "series") + " or 2) save the " + (isMovie ? "movie" : "series")); //Using the Ternary for the same reason as i did before
                int choice = Integer.parseInt(scanner.nextLine());
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
        String search = scanner.nextLine().trim();
        boolean mediaFound = false;

        ArrayList<Media> allMedia = new ArrayList<>();
        allMedia.addAll(io.readMediaData(moviefile, true)); // Adding all movies to my ArrayList allMedia
        allMedia.addAll(io.readMediaData(seriesfile, false)); //Adding all series to my ArrayList allMedia

        for (Media media : allMedia) {
            if (search.equalsIgnoreCase(media.getTitle())) { // Checking if the users output, equals a media objects title
                System.out.println(media);
                mediaFound = true;

                System.out.println("Do you want to 1) watch or 2) save");

                int choice = Integer.parseInt(scanner.nextLine());
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
            System.out.println("You already have a valid account. Try again");
            createAccount();  //recursion that lets the user try again if his/hers attempt to create an account failed
        } else {
            saveAccount(username, password); //Saving account if login worked
            System.out.println("Account created successfully.");
            users.add(new User(username, password)); //Adding the user to a list of users
            startmenu(); //displaying start menu
        }
    }

    private static String getUserInput(String msg) { //method that displays a message and then scanning the line for a user input
        Scanner scanner = new Scanner(System.in);
        System.out.print(msg);
        return scanner.nextLine();
    }

    private static boolean isValidAccount(String username, String password) {
        try (Scanner scanner = new Scanner(new FileReader(userfile))) { //scanning the Users.txt file
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals(username + " ; " + password)) { //returning true if the username and password is in the Users file
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("invalid account"); //catching the exception if the document doesn't contain the username and password the user wrote to login with.
        }
        return false;
    }

    private static void saveAccount(String username, String password) {
        try (FileWriter writer = new FileWriter(userfile, true)) { //adding my filewriter with the users.txt file. If append is true it turns on append mode. The filewriter will write the data at the end of the file. If the file doesn't exist it will create a new file with the name given in the ()
            writer.write(username + " ; " + password + "\n");
        } catch (IOException e) {
            System.out.println("couldn't save account");
        }
    }


    void displayChoice(){
        System.out.println("Do you 1) Want to shut down the program or 2) return to startpage");
        int menuChoice = Integer.parseInt(scanner.nextLine());
        switch (menuChoice){ //switch case that either shuts down the program or lets the user return to the start page
            case 1:
                break;
            case 2:
                s1.displayStartMenu();
        }
    }
}