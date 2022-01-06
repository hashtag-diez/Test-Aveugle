package server.src;

import java.io.File;
import java.io.IOException;
import java.util.List;

import client.src.repository.Catalogue;
import client.src.repository.Categorie;
import client.src.repository.Image;

public class Main {
    public static void main(String[] args) {
        Catalogue c =new Catalogue();
        c.showCatalogue();
        List<Categorie> l = c.getCatalogue();
        Image i = l.get(0).getCategoryImages().get(1);
        File f = new File("src/server/img/new.png");
        if(!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        c.decoder(i.getImg(), "src/server/img/new.png");
    }
    
}
