import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Media {

    String title;

    int release;

    double rating;

    public Media(String title, double rating){
        this.title = title;

        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }


    public ArrayList<String> getCategory() {
        return new ArrayList<>();
    }

    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", release='" + release + '\'' +
                ", rating=" + rating +
                '}';
    }
}