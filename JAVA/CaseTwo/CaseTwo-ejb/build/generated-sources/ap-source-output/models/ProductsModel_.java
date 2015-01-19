package models;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.VendorsModel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-28T15:43:16")
@StaticMetamodel(ProductsModel.class)
public class ProductsModel_ { 

    public static volatile SingularAttribute<ProductsModel, Integer> qod;
    public static volatile SingularAttribute<ProductsModel, BigDecimal> msrp;
    public static volatile SingularAttribute<ProductsModel, String> productcode;
    public static volatile SingularAttribute<ProductsModel, Serializable> qrcode;
    public static volatile SingularAttribute<ProductsModel, String> qrcodetxt;
    public static volatile SingularAttribute<ProductsModel, Integer> rop;
    public static volatile SingularAttribute<ProductsModel, VendorsModel> vendorno;
    public static volatile SingularAttribute<ProductsModel, Integer> qoh;
    public static volatile SingularAttribute<ProductsModel, Integer> eoq;
    public static volatile SingularAttribute<ProductsModel, String> sku;
    public static volatile SingularAttribute<ProductsModel, BigDecimal> cost;
    public static volatile SingularAttribute<ProductsModel, String> productname;

}