package src;

import java.io.File;
import java.io.IOException;
import java.util.List;

import src.model.Catalogue;
import src.model.Categorie;
import src.model.Image;
import src.repository.CatalogueRepository;
import src.utils.EncodingImage;

public class Main {
    public static void main(String[] args) {
        Catalogue c =new Catalogue();
        CatalogueRepository.showCatalogue(c);
        List<Categorie> l = c.getCatalogue();
        Image i = l.get(1).getCategoryImages().get(0);
        File f = new File("src/server/img/new.png");
        if(!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        EncodingImage.decoder(i.getImg(), "src/server/img/new.png");
    }
    
}
