package dtos;
/**
 * Container for purchase order
 * Initial Implementation: Sep 26th 
 */
import java.io.Serializable;
import java.util.ArrayList;
import dtos.PurchaseOrderLineItemDTO;

public class PurchaseOrderDTO implements Serializable {

    public PurchaseOrderDTO() {
    }
    
    private int ponumber;
    private int vendorno;
    private PurchaseOrderLineItemDTO[] items;    
    private Double total;

    
    /**
     * @return the ponumber
     */
    public int getPonumber() {
        return vendorno;
    }

    /**
     * @param ponumber the ponumber to set
     */
    public void setPonumber(int pono) {
        this.ponumber = pono;
    }
    
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
     * @return the items
     */
    public PurchaseOrderLineItemDTO[] getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(PurchaseOrderLineItemDTO[] items) {
        this.items = items;
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
    }
    

    
    
}
