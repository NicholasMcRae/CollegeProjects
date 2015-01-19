package case2ejbs;

import dtos.VendorEJBDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import models.VendorsModel;

/*
Purpoase: Use entity manager to interface with Vendor model
Author           Date              Description
**********************************************
Nick             Oct. 31 2014      Initial Implementation 
*/
@Stateless
@LocalBean
public class VendorBeanFacade {

    @PersistenceContext
    private EntityManager em;
    
    public int addVendor(VendorEJBDTO ven) {

        VendorsModel vm;
        int retVal = -1;

        try {
            vm = new VendorsModel(null, ven.getAddress1(), ven.getCity(),
                    ven.getProvince(), ven.getPostalcode(),
                    ven.getPhone(), ven.getVendortype(), ven.getName(),
                    ven.getEmail());
            em.persist(vm);
            em.flush();
            retVal = vm.getVendorno();
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
    
    public List<VendorEJBDTO> getVendors(){
        List<VendorsModel> vendors;
        List<VendorEJBDTO> vendorsDTO = new ArrayList();
        
        try{
            Query qry = em.createNamedQuery("VendorsModel.findAll");
            vendors = qry.getResultList();
            
            for(VendorsModel v : vendors){
                VendorEJBDTO dto = new VendorEJBDTO();
                dto.setAddress1(v.getAddress1());
                dto.setCity(v.getCity());
                dto.setEmail(v.getEmail());
                dto.setName(v.getName());
                dto.setPhone(v.getPhone());
                dto.setPostalcode(v.getPostalcode());
                dto.setProvince(v.getProvince());
                dto.setVendorno(v.getVendorno());
                dto.setVendortype(v.getVendortype());
                vendorsDTO.add(dto);
            }
        } catch(Exception e){
            System.out.println("Other issue " + e.getMessage());
        }
        return vendorsDTO;
    }
    
    public int updateVendor(VendorEJBDTO dto){
        VendorsModel vm;
        int rowsUpdated = -1;
        
        try{
            vm = em.find(VendorsModel.class, dto.getVendorno());
            vm.setAddress1(dto.getAddress1());
            vm.setCity(dto.getCity());
            vm.setEmail(dto.getEmail());
            vm.setName(dto.getName());
            vm.setPhone(dto.getPhone());
            vm.setPostalcode(dto.getPostalcode());
            vm.setProvince(dto.getProvince());
            vm.setVendortype(dto.getVendortype());
            em.flush();
            rowsUpdated = 1;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return rowsUpdated;
    }
    
    public int deleteVendor(int vendorno){
        
        try{
           VendorsModel v = em.find(VendorsModel.class, vendorno);
            em.remove(v);
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage() + " Vendor no: " + vendorno);
        }
        
        return 1;
    }
}
