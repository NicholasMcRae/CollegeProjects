package dtos;

import java.math.BigDecimal;

public class ProductEJBDTO {
    
    private int vendorno;
    private String code;    
    private String sku;
    private String name;
    private BigDecimal cost;
    private BigDecimal msrp;
    private int rop;
    private int eoq;
    private int qoh;
    private int qod;
    private byte[] QRCode;
    private String QRCodeText;

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
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the msrp
     */
    public BigDecimal getMsrp() {
        return msrp;
    }

    /**
     * @param msrp the msrp to set
     */
    public void setMsrp(BigDecimal msrp) {
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
     * @return the qod
     */
    public int getQod() {
        return qod;
    }

    /**
     * @param qod the qod to set
     */
    public void setQod(int qod) {
        this.qod = qod;
    }
    
    /**
     * @return the qr code
     */
    public byte[] getQRCode() {
        return QRCode;
    }
    
    /**
     * @param qr code to set
     */
    public void setQRCode(byte [] QRCode) {
        this.QRCode = QRCode;
    }

    /**
     * @param text to set
     */
    public void setQRCodeText(String QRCode) {
        this.QRCodeText = QRCode;
    }
    
    /**
     * @return the code text
     */
    public String getQRCodeText() {
        return QRCodeText;
    }
}
