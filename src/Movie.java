import java.util.ArrayList;

public class Movie extends Media {
    String release;
    ArrayList<String> category;

    public Movie(String title, ArrayList<String> category, String release, double rating) {
        super(title, rating);
        this.release = release;
        this.category=category;
    }

    @Override
    public ArrayList<String> getCategory() {
        return category;
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    // @Override
    public String toString() {
        return "Movie: " +
                "title: '" + title + '\'' +
                ", release: " + release +
                ", rating: " + rating +
                ", category: " + category+
                "\n-------------------------";


    }
}