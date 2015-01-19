package models;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.PurchaseOrdersModel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-28T15:43:16")
@StaticMetamodel(PurchaseLineItemsModel.class)
public class PurchaseLineItemsModel_ { 

    public static volatile SingularAttribute<PurchaseLineItemsModel, String> prodcd;
    public static volatile SingularAttribute<PurchaseLineItemsModel, BigDecimal> price;
    public static volatile SingularAttribute<PurchaseLineItemsModel, Integer> lineid;
    public static volatile SingularAttribute<PurchaseLineItemsModel, Integer> qty;
    public static volatile SingularAttribute<PurchaseLineItemsModel, PurchaseOrdersModel> ponumber;

}