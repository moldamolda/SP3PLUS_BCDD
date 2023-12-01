import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileIO{


    public ArrayList<Media> readMediaData(String path, boolean isMovie) {
        ArrayList<Media> mediaList = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine(); // Scanning next line

                String[] data = line.split(";"); // Splitting data when ; occurs

                // Common attributes
                String title = data[0];
                String release = data[1].trim();
                double rating = Double.parseDouble(data[3].replace(",", ".").trim());


                String[] categories;
                categories = data[2].split(","); // Splitting categories when , occurs
                if (isMovie) {

                    ArrayList<String> movieCategories = new ArrayList<>(Arrays.asList(categories));  // Creating a new arraylist for each movie category

                    Movie movie = new Movie(title, movieCategories, release, rating);       // Creating movie objects

                    mediaList.add(movie); // Adding my movie objects to my mediaList
                } else {

                    String seasons = data[4].trim(); // Data[4] is seasons

                    ArrayList<String> seriesSeasons = new ArrayList<>(Arrays.asList(seasons.split(",")));  // Creating a new ArrayList for each series category. splitting  season[4] to get seasons

                    Serie serie = new Serie(title, new ArrayList<>(Arrays.asList(categories)), release, rating, seriesSeasons);  // Creating new series objects

                    mediaList.add(serie);    // Adding Series objects to mediaList
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found"); //catching error
        }

        return mediaList;
    }

}