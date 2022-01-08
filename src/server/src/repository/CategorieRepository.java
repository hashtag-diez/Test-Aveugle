package src.repository;

import java.util.List;

import src.App;
import src.model.Categorie;
import src.model.Channel;
import src.model.Image;

public class CategorieRepository {
    
    //-> return X none equal catalogue Images    if(x >= categImages.size()) return categImages
        
    //TODO 
    public static Image getRandomImage(String channelName, String categorieName) {
        Categorie c = CatalogueRepository.findCategorieByName(App.catalogue, categorieName);
        Channel channel = ChannelRepository.getChannelByName(channelName);
        System.out.println(c.getCategoryName());
        List<Image> l = c.getCategoryImages();
        System.out.println(l.size());
        int j = (int) (Math.random() * l.size());
        System.out.println(j);
        Image res = l.get(j);
        System.out.println(j + ": " + res.getResponse());
        while(channel.getAnsweredQuestions().contains(res.getResponse())){
            j = (int) (Math.random() * l.size());
            res = l.get(j);
        }
        channel.addQuestions(res.getResponse());
        System.out.println(channel.getAnsweredQuestions().toString());
        System.out.println(res.getImg().length());
        return res;
    }

    public static Image findImageByName(Categorie c, String name){
        for(Image i : c.getCategoryImages()){
            if(i.getResponse()==name) return i;
        }
        return null;
    }
}
