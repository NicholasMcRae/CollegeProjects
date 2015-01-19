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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "PRODUCTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductsModel.findAll", query = "SELECT p FROM ProductsModel p"),
    @NamedQuery(name = "ProductsModel.findByProductcode", query = "SELECT p FROM ProductsModel p WHERE p.productcode = :productcode"),
    @NamedQuery(name = "ProductsModel.findBySku", query = "SELECT p FROM ProductsModel p WHERE p.sku = :sku"),
    @NamedQuery(name = "ProductsModel.findByProductname", query = "SELECT p FROM ProductsModel p WHERE p.productname = :productname"),
    @NamedQuery(name = "ProductsModel.findByCost", query = "SELECT p FROM ProductsModel p WHERE p.cost = :cost"),
    @NamedQuery(name = "ProductsModel.findByMsrp", query = "SELECT p FROM ProductsModel p WHERE p.msrp = :msrp"),
    @NamedQuery(name = "ProductsModel.findByRop", query = "SELECT p FROM ProductsModel p WHERE p.rop = :rop"),
    @NamedQuery(name = "ProductsModel.findByEoq", query = "SELECT p FROM ProductsModel p WHERE p.eoq = :eoq"),
    @NamedQuery(name = "ProductsModel.findByQoh", query = "SELECT p FROM ProductsModel p WHERE p.qoh = :qoh"),
    @NamedQuery(name = "ProductsModel.findByQod", query = "SELECT p FROM ProductsModel p WHERE p.qod = :qod"),
    @NamedQuery(name = "ProductsModel.findByQrcodetxt", query = "SELECT p FROM ProductsModel p WHERE p.qrcodetxt = :qrcodetxt")})
    @NamedQuery(name = "ProductsModel.findByVendorno", query = "SELECT p FROM ProductsModel p WHERE p.vendorno = :vendorno")
public class ProductsModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "PRODUCTCODE")
    private String productcode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "SKU")
    private String sku;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PRODUCTNAME")
    private String productname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "COST")
    private BigDecimal cost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MSRP")
    private BigDecimal msrp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROP")
    private int rop;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EOQ")
    private int eoq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QOH")
    private int qoh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QOD")
    private int qod;
    @Lob
    @Column(name = "QRCODE")
    private Serializable qrcode;
    @Size(max = 50)
    @Column(name = "QRCODETXT")
    private String qrcodetxt;
    @JoinColumn(name = "VENDORNO", referencedColumnName = "VENDORNO")
    @ManyToOne(optional = false)
    private VendorsModel vendorno;

    public ProductsModel() {
    }

    public ProductsModel(String productcode) {
        this.productcode = productcode;
    }

    public ProductsModel(String productcode, String sku, String productname, BigDecimal cost, BigDecimal msrp, int rop, int eoq, int qoh, int qod, VendorsModel v) {
        this.productcode = productcode;
        this.sku = sku;
        this.productname = productname;
        this.cost = cost;
        this.msrp = msrp;
        this.rop = rop;
        this.eoq = eoq;
        this.qoh = qoh;
        this.qod = qod;
        this.vendorno = v;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public int getRop() {
        return rop;
    }

    public void setRop(int rop) {
        this.rop = rop;
    }

    public int getEoq() {
        return eoq;
    }

    public void setEoq(int eoq) {
        this.eoq = eoq;
    }

    public int getQoh() {
        return qoh;
    }

    public void setQoh(int qoh) {
        this.qoh = qoh;
    }

    public int getQod() {
        return qod;
    }

    public void setQod(int qod) {
        this.qod = qod;
    }

    public Serializable getQrcode() {
        return qrcode;
    }

    public void setQrcode(Serializable qrcode) {
        this.qrcode = qrcode;
    }

    public String getQrcodetxt() {
        return qrcodetxt;
    }

    public void setQrcodetxt(String qrcodetxt) {
        this.qrcodetxt = qrcodetxt;
    }

    public VendorsModel getVendorno() {
        return vendorno;
    }

    public void setVendorno(VendorsModel vendorno) {
        this.vendorno = vendorno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productcode != null ? productcode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductsModel)) {
            return false;
        }
        ProductsModel other = (ProductsModel) object;
        if ((this.productcode == null && other.productcode != null) || (this.productcode != null && !this.productcode.equals(other.productcode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.ProductsModel[ productcode=" + productcode + " ]";
    }
    
}
