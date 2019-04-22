/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nickm
 */
@Entity
@Table(name = "PURCHASEORDERLINEITEMS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseLineItemsModel.findAll", query = "SELECT p FROM PurchaseLineItemsModel p"),
    @NamedQuery(name = "PurchaseLineItemsModel.findByLineid", query = "SELECT p FROM PurchaseLineItemsModel p WHERE p.lineid = :lineid"),
    @NamedQuery(name = "PurchaseLineItemsModel.findByProdcd", query = "SELECT p FROM PurchaseLineItemsModel p WHERE p.prodcd = :prodcd"),
    @NamedQuery(name = "PurchaseLineItemsModel.findByQty", query = "SELECT p FROM PurchaseLineItemsModel p WHERE p.qty = :qty"),
    @NamedQuery(name = "PurchaseLineItemsModel.findByPrice", query = "SELECT p FROM PurchaseLineItemsModel p WHERE p.price = :price"),
    @NamedQuery(name = "PurchaseLineItemsModel.findByPonumber", query = "SELECT p FROM PurchaseLineItemsModel p WHERE p.ponumber = :ponumber")})
public class PurchaseLineItemsModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LINEID")
    private Integer lineid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PRODCD")
    private String prodcd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QTY")
    private int qty;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRICE")
    private BigDecimal price;
    @JoinColumn(name = "PONUMBER", referencedColumnName = "PONUMBER")
    @ManyToOne(optional = false)
    private PurchaseOrdersModel ponumber;

    public PurchaseLineItemsModel() {
    }

    public PurchaseLineItemsModel(Integer lineid) {
        this.lineid = lineid;
    }

    public PurchaseLineItemsModel(Integer lineid, String prodcd, int qty, BigDecimal price) {
        this.lineid = lineid;
        this.prodcd = prodcd;
        this.qty = qty;
        this.price = price;
    }

    public Integer getLineid() {
        return lineid;
    }

    public void setLineid(Integer lineid) {
        this.lineid = lineid;
    }

    public String getProdcd() {
        return prodcd;
    }

    public void setProdcd(String prodcd) {
        this.prodcd = prodcd;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PurchaseOrdersModel getPonumber() {
        return ponumber;
    }

    public void setPonumber(PurchaseOrdersModel ponumber) {
        this.ponumber = ponumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lineid != null ? lineid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseLineItemsModel)) {
            return false;
        }
        PurchaseLineItemsModel other = (PurchaseLineItemsModel) object;
        if ((this.lineid == null && other.lineid != null) || (this.lineid != null && !this.lineid.equals(other.lineid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.PurchaseLineItemsModel[ lineid=" + lineid + " ]";
    }
    
}
