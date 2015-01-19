package case2ejbs;

import dtos.PurchaseOrderEJBDTO;
import dtos.PurchaseOrderLineItemEJBDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import models.ProductsModel;
import models.PurchaseLineItemsModel;
import models.PurchaseOrdersModel;
import models.VendorsModel;

/*
Purpose: Use entity manager to interface with Purchase Orders Model for manipulating POs
Author           Date              Description
**********************************************
Nick             Nov. 14 2014      Initial Implementation 
*/

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Stateless
@LocalBean
public class POBeanFacade {
    
    @PersistenceContext
    private EntityManager em;
    private EJBContext context;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public int addPo(PurchaseOrderEJBDTO poDTO)
    {          
        PurchaseOrdersModel pm;
        VendorsModel vm;
        int poRowID = -1;
        Date poDate = new java.util.Date();
        
        try{            
            vm = em.find(VendorsModel.class, poDTO.getVendorno());
            pm = new PurchaseOrdersModel(0, poDTO.getTotal(), poDate);
            pm.setVendorno(vm);
            em.persist(pm);
            em.flush();
            poRowID = pm.getPonumber();
            
            for(PurchaseOrderLineItemEJBDTO line : poDTO.getItems())
            {
                if(line.getQuantity() > 0){
                    int retVal = addPOLine(line, pm);
                }
            }
        }catch(ConstraintViolationException v){
            Set<ConstraintViolation<?>> coll = v.getConstraintViolations();
            for(ConstraintViolation s : coll){
                System.out.println(s.getPropertyPath() + " " + s.getMessage());
            }
        }catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
            context.setRollbackOnly();
            
        }
        return poRowID;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private int addPOLine(PurchaseOrderLineItemEJBDTO line, PurchaseOrdersModel po){
        PurchaseLineItemsModel poim;
        int poLineNum = -1;
        
        try{   
            int rowsUpdated = updateInventory(line.getProductCode(), line.getQuantity());
            poim = new PurchaseLineItemsModel();
            poim.setPonumber(po);
            poim.setPrice(line.getPrice());
            poim.setProdcd(line.getProductCode());
            poim.setQty(line.getQuantity());
            em.persist(poim);
            em.flush();
            poLineNum = poim.getLineid();
        }
        catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        }        
        return poLineNum;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private int updateInventory(String code, int quantity)
    {
        ProductsModel pm;
        int rowsUpdated = -1;
        
        try{
            pm = em.find(ProductsModel.class, code);
            pm.setQod(pm.getQod() + quantity);
            em.persist(pm);
            em.flush();
        }
        catch(Exception e){
            System.out.println("other issue " + e.getMessage());
        }
        
        return 1;
    }
    
    public List<PurchaseOrderEJBDTO> getAllPurchaseOrdersFromVendor(int vendorno)
    {
        PurchaseOrdersModel pm;
        VendorsModel vm;
        List<PurchaseOrdersModel> purchaseOrders;
        List<PurchaseOrderEJBDTO> purchaseOrderDTOs = new ArrayList<PurchaseOrderEJBDTO>();
        
        try{
            Query qry = em.createNamedQuery("PurchaseOrdersModel.findByVendorno");
            vm = em.find(VendorsModel.class, vendorno);
            qry.setParameter("vendorno", vm);
            purchaseOrders = qry.getResultList();
            
            for(PurchaseOrdersModel item : purchaseOrders)
            {
              PurchaseOrderEJBDTO poDTO = new PurchaseOrderEJBDTO();
              poDTO.setPonumber(item.getPonumber());
              poDTO.setTotal(item.getAmount());
              poDTO.setItems(getPOLine(item));
              poDTO.setDate(item.getPodate());
              purchaseOrderDTOs.add(poDTO);
            }
        }
        catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        }
        return purchaseOrderDTOs;
    }    
    
    private PurchaseOrderLineItemEJBDTO[] getPOLine(PurchaseOrdersModel po){
        
        PurchaseOrdersModel p0;
        List<PurchaseLineItemsModel> lineItems;
        PurchaseOrderLineItemEJBDTO[] list = new PurchaseOrderLineItemEJBDTO[0];
        List<ProductsModel> products = getVendorProducts(po.getVendorno().getVendorno());
        po = em.find(PurchaseOrdersModel.class, po.getPonumber());
        try{
            Query qry = em.createNamedQuery("PurchaseLineItemsModel.findByPonumber");
            qry.setParameter("ponumber", po);  
            lineItems = qry.getResultList();
            list = new PurchaseOrderLineItemEJBDTO[lineItems.size()];
            int i = 0;
            for(PurchaseLineItemsModel item : lineItems){
                list[i] = new PurchaseOrderLineItemEJBDTO();
                list[i].setProductCode(item.getProdcd());
                list[i].setQuantity(item.getQty());                  
                list[i].setPrice(item.getPrice());
                
                for(ProductsModel p : products)
                {
                    if(p.getProductcode().equals(list[i].getProductCode()))
                    {
                        list[i].setName(p.getProductname());
                        list[i].setCost(p.getCost());
                    }
                }
                i++;
            }
        }
        catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        }        
        return list;
    }
    
    private List<ProductsModel> getVendorProducts(int vendorno)
    {
        List<ProductsModel> products = new ArrayList();
        VendorsModel vm;
        vm = em.find(VendorsModel.class, vendorno);
        
        try{
            Query qry = em.createNamedQuery("ProductsModel.findByVendorno");
            qry.setParameter("vendorno", vm);
            products = qry.getResultList();
        }catch(Exception e){
           System.out.println("other issue " + e.getMessage()); 
        }
        
        return products;
    }
}
