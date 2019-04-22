package models;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.PurchaseLineItemsModel;
import models.VendorsModel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-28T15:43:16")
@StaticMetamodel(PurchaseOrdersModel.class)
public class PurchaseOrdersModel_ { 

    public static volatile SingularAttribute<PurchaseOrdersModel, BigDecimal> amount;
    public static volatile SingularAttribute<PurchaseOrdersModel, Date> podate;
    public static volatile CollectionAttribute<PurchaseOrdersModel, PurchaseLineItemsModel> purchaseLineItemsModelCollection;
    public static volatile SingularAttribute<PurchaseOrdersModel, VendorsModel> vendorno;
    public static volatile SingularAttribute<PurchaseOrdersModel, Integer> ponumber;

}