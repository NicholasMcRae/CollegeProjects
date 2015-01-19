package dtos;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author nickm
 */
public class PurchaseOrderEJBDTO {
    
    private int ponumber;
    private int vendorno;
    private PurchaseOrderLineItemEJBDTO[] items;    
    private BigDecimal total;
    private Date date;

    /**
     * @return the ponumber
     */
    public int getPonumber() {
        return ponumber;
    }

    /**
     * @param ponumber to set
     */
    public void setPonumber(int p) {
        this.ponumber = p;
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
    public PurchaseOrderLineItemEJBDTO[] getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(PurchaseOrderLineItemEJBDTO[] items) {
        this.items = items;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date to set
     */
    public void setDate(Date d) {
        this.date = d;
    }
    
}
