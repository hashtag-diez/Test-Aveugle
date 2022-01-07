package server.src;

import java.io.File;
import java.io.IOException;
import java.util.List;

import server.src.model.Catalogue;
import server.src.model.Categorie;
import server.src.model.Image;
import server.src.repository.CatalogueRepository;
import server.src.utils.EncodingImage;

public class Main {
    public static void main(String[] args) {
        Catalogue c =new Catalogue();
        CatalogueRepository.showCatalogue(c);
        List<Categorie> l = c.getCatalogue();
        Image i = l.get(0).getCategoryImages().get(29);
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
