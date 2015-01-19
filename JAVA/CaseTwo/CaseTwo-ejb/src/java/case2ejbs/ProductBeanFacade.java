package case2ejbs;

import dtos.ProductEJBDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import models.ProductsModel;
import models.VendorsModel;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/*
Purpoase: Use entity manager to interface with Product Model for manipulating products
Author           Date              Description
**********************************************
Nick             Oct. 31 2014      Initial Implementation 
*/
@Stateless
@LocalBean
public class ProductBeanFacade {

    @PersistenceContext
    private EntityManager em;
    
    public String addProduct(ProductEJBDTO pro) {

        ProductsModel pm;
        String retVal = "Undefined";
        VendorsModel vm = em.find(VendorsModel.class, pro.getVendorno());

        try {
            pm = new ProductsModel(pro.getCode(), pro.getSku(), pro.getName(), pro.getCost(), pro.getMsrp(), pro.getRop(),
                            pro.getEoq(), pro.getQoh(), pro.getQod(), vm);
            pm.setQrcodetxt(pro.getQRCodeText());
            em.persist(pm);
            em.flush();
            retVal = pm.getProductcode();
        } catch (ConstraintViolationException v) { 
            Set<ConstraintViolation<?>> coll = v.getConstraintViolations(); 
            for (ConstraintViolation s : coll) {
                System.out.println(s.getPropertyPath() + " " + s.getMessage());
            }
        } catch (Exception e) {        
            System.out.println(e.getMessage());
        }
        return retVal;

    }
    
    public List<ProductEJBDTO> getProducts(){
        List<ProductsModel> products;
        List<ProductEJBDTO> productsDTO = new ArrayList();
        
        try{
            Query qry = em.createNamedQuery("ProductsModel.findAll");
            products = qry.getResultList();
            
            for(ProductsModel p : products){
                ProductEJBDTO dto = new ProductEJBDTO();                
                dto.setSku(p.getSku());
                dto.setName(p.getProductname());
                dto.setCost(p.getCost());
                dto.setMsrp(p.getMsrp());
                dto.setRop(p.getRop());
                dto.setEoq(p.getEoq());
                dto.setQoh(p.getQoh());
                dto.setQod(p.getQod());
                dto.setCode(p.getProductcode());
                dto.setVendorno(p.getVendorno().getVendorno());
                dto.setQRCodeText(p.getQrcodetxt());
                productsDTO.add(dto);
            }
        } catch(Exception e){
            System.out.println("Other issue " + e.getMessage());
        }
        return productsDTO;
    }
    
    public ProductEJBDTO getProduct(String productCode){
        
        ProductsModel p;
        ProductEJBDTO dto = new ProductEJBDTO();
        
        try{
            Query qry = em.createNamedQuery("ProductsModel.findByProductcode");
            qry.setParameter("productcode", productCode);
            p = (ProductsModel)qry.getSingleResult(); 
            dto.setSku(p.getSku());
            dto.setName(p.getProductname());
            dto.setCost(p.getCost());
            dto.setMsrp(p.getMsrp());
            dto.setRop(p.getRop());
            dto.setEoq(p.getEoq());
            dto.setQoh(p.getQoh());
            dto.setQod(p.getQod());
            dto.setCode(p.getProductcode());
            dto.setQRCodeText(p.getQrcodetxt());
            dto.setVendorno(p.getVendorno().getVendorno());
            
        } catch(Exception e){
            System.out.println("Other issue " + e.getMessage());
        }
        return dto;
    }
    
    public int updateProduct(ProductEJBDTO dto){
        ProductsModel vm;
        int rowsUpdated = -1;
        
        try{
            vm = em.find(ProductsModel.class, dto.getCode());
            vm.setSku(dto.getSku());
            vm.setProductname(dto.getName());
            vm.setCost(dto.getCost());
            vm.setMsrp(dto.getMsrp());
            vm.setRop(dto.getRop());
            vm.setEoq(dto.getEoq());
            vm.setQoh(dto.getQoh());
            vm.setQod(dto.getQod()); 
            vm.setQrcodetxt(dto.getQRCodeText());
            vm.setVendorno(em.find(VendorsModel.class, dto.getVendorno()));
            em.flush();
            rowsUpdated = 1;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return rowsUpdated;
    }
    
    public int deleteProduct(String productcode){
        ProductsModel p = em.find(ProductsModel.class, productcode);
        
        try{
            em.remove(p);
        }
        catch(Exception e){
            System.out.println("Exception in delete product: " + e.getMessage());
        }
        
        return 1;
    } 
    
    public ArrayList<ProductEJBDTO> getAllProductsForVendor(int vendorno)
    {
        ArrayList<ProductEJBDTO> productsDTO = new ArrayList<>();
        List<ProductsModel> products;
        VendorsModel vm;
        
        try{
            vm = em.find(VendorsModel.class, vendorno);
            Query qry = em.createNamedQuery("ProductsModel.findByVendorno");
            qry.setParameter("vendorno", vm);
            products = qry.getResultList();
            
            for(ProductsModel p : products){
                ProductEJBDTO dto = new ProductEJBDTO();
                dto.setCode(p.getProductcode());
                dto.setCost(p.getCost());
                dto.setEoq(p.getEoq());
                dto.setMsrp(p.getMsrp());
                dto.setName(p.getProductname());
                dto.setQRCodeText(p.getQrcodetxt());
                dto.setQod(p.getQod());
                dto.setQoh(p.getQoh());
                dto.setRop(p.getRop());
                dto.setSku(p.getSku());
                dto.setVendorno(p.getVendorno().getVendorno());
                productsDTO.add(dto);
            }
        }catch(Exception e){
            System.out.println("Exception in getProductsForVendor" + e.getMessage());
        }
        
        return productsDTO;
    }
    
    
}
