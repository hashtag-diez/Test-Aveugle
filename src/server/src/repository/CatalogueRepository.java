package server.src.repository;

import server.src.model.Catalogue;
import server.src.model.Categorie;
import server.src.model.Image;

public class CatalogueRepository {

    public static void showCatalogue(Catalogue catalogue){
        for(Categorie c : catalogue.getCatalogue()){
            System.out.println(c.getCategoryName());
            for(Image image : c.getCategoryImages()){
                System.out.println(image.getResponse());
            }
        }
    }

    public static Categorie findCategorieByName(Catalogue c, String name){
        for(Categorie categ : c.getCatalogue()){
            if (categ.getCategoryName() == name) return categ;
        }
        return null;
    }

}
