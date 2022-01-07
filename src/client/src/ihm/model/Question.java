package src.ihm.model;

import java.io.FileOutputStream;
import java.util.Base64;


public class Question {
    private String startingDate;
    
    public Question(String image, String startAtDate) {
        try (FileOutputStream imageOutFile = new FileOutputStream("src/client/img/image.jpg")) {
			byte[] imageByteArray = Base64.getDecoder().decode(image);
			imageOutFile.write(imageByteArray);
            
		} catch (Exception e) {
			System.out.println("Image not found" + e);
		}
        startingDate = startAtDate;
    }


    public String getStartingDate() {
        return startingDate;
    }
}
