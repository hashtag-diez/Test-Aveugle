package src.repository;

import java.util.ArrayList;
import java.util.List;

import src.App;
import src.model.Categorie;
import src.model.Image;

public class CategorieRepository {
    
    //-> return X none equal catalogue Images    if(x >= categImages.size()) return categImages
        
    //TODO 
    public static List<Image> getXRandomImages(int x, String categorieName) {
        Categorie c = CatalogueRepository.findCategorieByName(App.catalogue, categorieName);
        List<Image> l = c.getCategoryImages();
        int i=0;
        List<Image> l1 = l;
        List<Image> returnedList = new ArrayList<>();
        while(i < x && l1.size()>0){
            int j = (int) (Math.random() % l1.size());
            returnedList.add(l1.get(j));
            l1.remove(j);
            i++;
        }
        return returnedList;
    }

    public static Image findImageByName(Categorie c, String name){
        for(Image i : c.getCategoryImages()){
            if(i.getResponse()==name) return i;
        }
        return null;
    }
}
