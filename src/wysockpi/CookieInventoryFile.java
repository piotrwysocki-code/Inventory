package wysockpi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import prog24178.labs.objects.CookieInventoryItem;
import prog24178.labs.objects.Cookies;

/**
 * A class which extends ArrayList
 * Contains CookieIventoryItems
 * 
 * @author Piotr Wysocki 8/8/2020
 */
public class CookieInventoryFile extends ArrayList<CookieInventoryItem> {

    /**
     * Default constructor for the class
     */
    public CookieInventoryFile(){
        
    }
    
    /**
     * Constructor which accepts a file parameter,
     * loads the class with CookieInventory objects created from the data 
     * contained within the file.
     * 
     * @param file 
     */
    public CookieInventoryFile(File file) {
        loadFromFile(file);
    }
   
    /**
     * method which loads data from the file and converts to CookieInventoryItems
     * Populates this class with the created CookieInventoryItem objects
     * 
     * @param file 
     */
    public void loadFromFile(File file)  {
        if (file.exists()) {
            try {
                Scanner input = new Scanner(file);
                input.useDelimiter("\\|");
                while (input.hasNextLine()) {
                    String s = input.nextLine();
                    String[] values = s.split("\\|");

                    int id = Integer.parseInt(values[0]);
                    int qty = Integer.parseInt(values[1]);
                    Cookies flavor = Cookies.getCookie(id);

                    CookieInventoryItem Cookie = new CookieInventoryItem(flavor, qty);

                    add(Cookie);
                }
                input.close();
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     * Method which searches this class for a CookieInventoryItem with the given 
     * ID parameter
     *  
     * @param id
     * @return CookieInventoryItem if it exists in the list, otherwise returns 
     * a null object
     */
    public CookieInventoryItem find(int id) {
        for (CookieInventoryItem c : this) {
            if (c.cookie.getId() == id) {
                return c;
            }
        }
        return null;
    }
    
    /**
     * Method which writes the data contained in this class to a file.
     * Converts the CookieInventoryItem objects to readable data
     * contained in a file
     * 
     * @param file
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void writeToFile(File file) throws FileNotFoundException, IOException {         
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
            for (CookieInventoryItem c : this) {
                writer.println(c.toFileString());
                
            }
        }

    }
}
