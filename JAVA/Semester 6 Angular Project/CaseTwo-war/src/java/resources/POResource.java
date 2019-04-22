package resources;

import case2ejbs.POBeanFacade;
import dtos.ProductDTO;
import dtos.ProductEJBDTO;
import dtos.PurchaseOrderDTO;
import dtos.PurchaseOrderEJBDTO;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.ProductOrderModel;

/**
 * REST Web Service
 *
 * @author Nick
 */
@Path("po")
@RequestScoped
public class POResource {
    POBeanFacade pbf = lookupPOBeanFacadeBean();

    @Context
    private UriInfo context;
    
    @POST
    @Consumes("application/json")
    public Response createPO(PurchaseOrderEJBDTO po){
        int pono = pbf.addPo(po);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(pono).build();
    }
    
    @GET
    @Path("/{vendorno}")
    @Produces("application/json")
    public List<PurchaseOrderEJBDTO> getProductsForVendorJson(@PathParam("vendorno") int vendorno){
        return pbf.getAllPurchaseOrdersFromVendor(vendorno);
    }
    
    public POResource() {
    }

    private POBeanFacade lookupPOBeanFacadeBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (POBeanFacade) c.lookup("java:global/CaseTwo/CaseTwo-ejb/POBeanFacade!case2ejbs.POBeanFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}