import java.util.Scanner;

public class Streamingservice {
    Login log;

    Scanner scanner = new Scanner(System.in);
    DatabaseIO d1 = new DatabaseIO();
    TextUI io = new TextUI();

    public Streamingservice(Login log) {
        this.log = log;
    }

    public void displayStartMenu() {
        System.out.println("1) Søg efter et bestemt Medie");
        System.out.println("2) Søg efter en bestemt Medie kategori");
        System.out.println("3) Se din liste over sete film");
        System.out.println("4) Se din liste over gemte film");
        System.out.println("5) Se din liste over sete serier");
        System.out.println("6) Se din liste over gemte serier");
        System.out.println("7) Luk programmet");

        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                log.searchByName();
                break;
            case 2:
                System.out.println("Choose which media type to search: 1)Movies 2) Series");
                int mediachoise = Integer.parseInt(scanner.nextLine());
                switch (mediachoise) {
                    case 1:
                        log.searchByGenre(d1.readData(true),true);
                        break;
                    case 2:
                        log.searchByGenre(d1.readData(false), false);
                        break;
                    default:
                        System.out.println("Invalid choise. Try again");
                        break;
                }
                break;
            case 3:
                for (String watchedmovie : log.watchedMovies) {
                    System.out.println(watchedmovie);
                }

                log.displayChoice();
                break;
            case 4:
                for (String savedmovies : log.savedmovies) {
                    System.out.println(savedmovies);
                }

                log.displayChoice();
                break;
            case 5:
                for (String watchedseries : log.watchedseries) {
                    System.out.println(watchedseries);
                }
                log.displayChoice();
                break;
            case 6:
                for (String savedseries : log.savedseries) {
                    System.out.println(savedseries);
                }
                log.displayChoice();
                break;
            case 7:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayStartMenu();
        }
    }
}