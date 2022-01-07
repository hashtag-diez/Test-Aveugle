package server.src.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import server.src.utils.EncodingImage;

/**
 * La classe Catalogue crée et stock une liste de Categorie à partir des dossiers locaux au projet
 */
public class Catalogue {
    private List<Categorie> catalogue = new ArrayList<>();
    private final String ImgPath = "src/server/img";

    public Catalogue(){
        File[] files = new File(ImgPath).listFiles();
        setCatalogue(files);
    }
    
    //Parcours le repertoire Catalogue composé de répertoires Categories
    private void setCatalogue(File[] files) {
        for (File file : files) {
            if(file.isDirectory()) setCategorie(file.getName(), file.listFiles());
        }
    }

    //Parcours un repertoire Categorie 'name'
    private void setCategorie(String name, File[] files){
        List<Image> imgList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                setCategorie(file.getName(), file.listFiles());
            } else {
                imgList.add(new Image(EncodingImage.encoder(ImgPath+"/"+ name +"/"+file.getName()), getCorrectName(file.getName())));
            }
        }

        if(imgList.size() > 1) catalogue.add(new Categorie(name, imgList));
    }


    private String getCorrectName(String s){
        String r = "";
        int i =0;
        while(i < s.length() && s.charAt(i) != '.'){
            if((Character.isUpperCase(s.charAt(i)) || Character.isDigit(s.charAt(i))) && i != 0){
                r+=" ";
            }
            r+=s.charAt(i);
            i++;
        }
        return r;
    }

    public List<Categorie> getCatalogue(){
        return catalogue;
    }

}
