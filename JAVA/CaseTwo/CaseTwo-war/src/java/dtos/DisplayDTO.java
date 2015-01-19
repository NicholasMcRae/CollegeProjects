package dtos;
/**
 * DTO Written as an object to be displayed on PO generator pdf
 * Sep 30th - Initial Implementation
 */
import java.io.Serializable;
public class DisplayDTO implements Serializable {

    public DisplayDTO() {
    }
    
    private String code; 
    private String name;
    private int quantity;
    private double price;
    private int vendorno;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    /**
     * @return the quantity
     */
    public int getVendorno() {
        return vendorno;
    }

    
    public void setVendorno(int vendorno) {
        this.vendorno = vendorno;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    

   
    
    
}