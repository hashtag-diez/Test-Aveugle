package client.src.repository;

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

    //TODO : Option --- addImage() _ getXRandomImages(int x) -> return X none equal catalogue Images. 
    /*
    TODO : Options --- 
                    addImage() _ 
                    getXRandomImages(int x) -> return X none equal catalogue Images
                                            if(x >= categImages.size()) return categImages
                                            OR
                                            int i=0;
                                            List<Message> l1 = categImages;
                                            List<Message> returnedList = new ArrayList();
                                            while(i < x && l1.size()>0){
                                                int j = Random() % l1.size();
                                                returnedList.add(l1[j]);
                                                l1.remove(j);
                                                i++;
                                            }
     */
    
}
