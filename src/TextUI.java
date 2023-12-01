public class TextUI extends FileIO{

    public void saveMedia(Login login, Media media) {
        if (media instanceof Movie) {
            login.savedmovies.add(media.getTitle());
            System.out.println("Movie saved successfully!");
        } else if (media instanceof Serie) {
            login.savedseries.add(media.getTitle());
            System.out.println("Series saved successfully!");
        }
    }

    //metode til at adde et medie til watchedList
    public void watchedMovie(Login login, Media movie){
        System.out.println("You have watched" + movie.getTitle());
        login.watchedMovies.add(movie.getTitle());

    }

    //metode til at adde et medie til watchedList
    public void watchedSerie(Login login, Media series){
        System.out.println("You are now watching " + series.getTitle());
        login.watchedseries.add(series.getTitle());
    }

    //metode til at fjerne et medie fra watchLaterList
    public void removeWatchedSeries(Login login, Media series){
        if(login.watchedseries.remove(series.getTitle())){
            System.out.println("Series has been removed from your watch later list");
        } else {
            System.out.println("Series not found in your watch later list");
        }
    }

    public void removeWatchedMovie(Login login, Media movie){
        if(login.watchedseries.remove(movie.getTitle())){
            System.out.println("Movie has been removed from your watch later list");
        } else {
            System.out.println("Movie not found in your watch later list");
        }
    }

    public void mediaNotFound(){
        System.out.println("Tittle not found try again");

    }
    //metode til at fjerne et medie fra watchLaterList
    /*public void removeMovie(Movie movie){
        if(getWatchMovieLaterList().remove(movie)){
            System.out.println("Media has been removed from your watch later list");
        } else {
            System.out.println("Media not found in your watch later list");
        }
    }*/

}