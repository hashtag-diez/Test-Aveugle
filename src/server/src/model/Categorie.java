package src.model;

import java.util.List;


/**
 * La classe Categorie a un nom (ex:Film) et conserve une liste d'images correspondant à cette catégorie
 */
public class Categorie {
    private String name;
    private List<Image> categImages;

    public Categorie(String name, List<Image> lImg){
        this.name = name;
        this.categImages = lImg;
    }

    public String getCategoryName(){
        return name;
    }

    public List<Image> getCategoryImages(){
        return categImages;
    }
    
}
