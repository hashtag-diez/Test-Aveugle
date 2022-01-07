package src.model;

/**
 * La classe Image stock une image en format string pour faciliter les transactions entre Utilisateurs, ainsi que la bonne réponse correspondante
 */
public class Image {
    private final String encodedImg;
    private final String response;

    public Image(String encodedImg, String response){
        this.encodedImg = encodedImg;
        this.response = response;
    }

    public String getImg(){
        return encodedImg;
    }

    public String getResponse(){
        return response;
    }
}
