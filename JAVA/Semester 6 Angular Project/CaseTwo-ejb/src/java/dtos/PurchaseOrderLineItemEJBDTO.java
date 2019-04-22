/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.math.BigDecimal;

/**
 *
 * @author nickm
 */
public class PurchaseOrderLineItemEJBDTO {
    
    private String productCode;
    private int quantity;
    private BigDecimal price;  
    private String name;
    private BigDecimal cost;

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal p) {
        this.price = p;
    }
    
     /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name to set
     */
    public void setName(String n) {
        this.name = n;
    }
    
    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost to set
     */
    public void setCost(BigDecimal c) {
        this.cost = c;
    }
    
     
    
}
