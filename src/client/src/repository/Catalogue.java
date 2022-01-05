package client.src.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * La classe Catalogue crée et stock une liste de Categorie à partir des dossiers locaux au projet
 */
public class Catalogue {
    private List<Categorie> catalogue = new ArrayList<>();
    private final String ImgPath = "src/server/img";

    /*
        Rendre cette classe singleton ? Pourquoi ?
    */

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
                imgList.add(new Image(encoder(ImgPath+"/"+ name +"/"+file.getName()), getCorrectName(file.getName())));
            }
        }

        if(imgList.size() > 1) catalogue.add(new Categorie(name, imgList));
    }

    private String encoder(String imagePath) {
		String base64Image = "";
		File file = new File(imagePath);
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a Image file from file system
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			base64Image = Base64.getEncoder().encodeToString(imageData);
		} catch (Exception e) {
			System.out.println(e);
		}
		return base64Image;
	}

	public void decoder(String base64Image, String pathFile) {
		try (FileOutputStream imageOutFile = new FileOutputStream(pathFile)) {
			// Converting a Base64 String into Image byte array
			byte[] imageByteArray = Base64.getDecoder().decode(base64Image);
			imageOutFile.write(imageByteArray);
		} catch (Exception e) {
			System.out.println("Image not found" + e);
		}
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

    public void showCatalogue(){
        for(Categorie c : catalogue){
            System.out.println(c.getCategoryName());
            for(Image image : c.getCategoryImages()){
                System.out.println(image.getResponse());
            }
        }
    }


    public List<Categorie> getCatalogue(){
        return catalogue;
    }

}
