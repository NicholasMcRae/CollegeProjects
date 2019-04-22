package dtos;
/**
 * VendorDTO - Container class that serializes vendor information traveling 
 * between ViewModel and Model classes
 * Originally written and included by course professor
 */
import java.io.Serializable;
public class ProductDTO implements Serializable {

    public ProductDTO() {
    }
    
    private int vendorno;
    private String code;    
    private String sku;
    private String name;
    private double cost;
    private double msrp;
    private int rop;
    private int eoq;
    private int qoh;
    private int qod;

    /**
     * @return the vendorno
     */
    public int getVendorno() {
        return vendorno;
    }

    /**
     * @param vendorno the vendorno to set
     */
    public void setVendorno(int vendorno) {
        this.vendorno = vendorno;
    }

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
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
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
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * @return the msrp
     */
    public double getMsrp() {
        return msrp;
    }

    /**
     * @param msrp the msrp to set
     */
    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }

    /**
     * @return the rop
     */
    public int getRop() {
        return rop;
    }

    /**
     * @param rop the rop to set
     */
    public void setRop(int rop) {
        this.rop = rop;
    }

    /**
     * @return the eoq
     */
    public int getEoq() {
        return eoq;
    }

    /**
     * @param eoq the eoq to set
     */
    public void setEoq(int eoq) {
        this.eoq = eoq;
    }

    /**
     * @return the qoh
     */
    public int getQoh() {
        return qoh;
    }

    /**
     * @param qoh the qoh to set
     */
    public void setQoh(int qoh) {
        this.qoh = qoh;
    }

    /**
     * @return the qdo
     */
    public int getQod() {
        return qod;
    }

    /**
     * @param qod the qdo to set
     */
    public void setQod(int qod) {
        this.qod = qod;
    }
    
}
