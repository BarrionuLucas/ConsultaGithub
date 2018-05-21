package br.com.lbarrionuevo.consultagithub.Model;

public class Repository {

    String nmRepo;
    String descRepo;
    String user;
    String urlImage;
    int stars, forks;
    boolean favorited;

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Repository(String nmRepo, String descRepo, String user,  int stars, int forks, String urlImage) {
        this.nmRepo = nmRepo;
        this.descRepo = descRepo;
        this.user = user;
        this.stars = stars;
        this.forks = forks;
        this.urlImage = urlImage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }



    public String getNmRepo() {
        return nmRepo;
    }

    public void setNmRepo(String nmRepo) {
        this.nmRepo = nmRepo;
    }

    public String getDescRepo() {
        return descRepo;
    }

    public void setDescRepo(String descRepo) {
        this.descRepo = descRepo;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
