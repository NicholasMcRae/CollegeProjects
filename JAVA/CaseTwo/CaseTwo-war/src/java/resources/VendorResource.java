package resources;

import case2ejbs.ProductBeanFacade;
import case2ejbs.VendorBeanFacade;
import dtos.ProductDTO;
import dtos.ProductEJBDTO;
import dtos.VendorDTO;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.VendorModel;
import models.ProductModel;
import dtos.VendorEJBDTO;
import java.util.List;

/**
 * REST Web Service
 *
 * @author Nick
 */
@Path("vendor")
@RequestScoped
public class VendorResource {
    
    ProductBeanFacade pbf = lookupProductBeanFacadeBean();     
    VendorBeanFacade vbf = lookupVendorBeanFacadeBean();
    
    @Context
    private UriInfo context;

    public VendorResource() {
    }
    
    private VendorBeanFacade lookupVendorBeanFacadeBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (VendorBeanFacade) c.lookup("java:global/CaseTwo/CaseTwo-ejb/VendorBeanFacade!case2ejbs.VendorBeanFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    /**
     *
     * @return
     */
    @GET
    @Produces("application/json")
    public List<VendorEJBDTO> getVendorsJson(){
        List<VendorEJBDTO> vendors = vbf.getVendors();
        return vendors;
    }
    
    @POST
    @Consumes("application/json")
    public Response createVendorFromJson(VendorEJBDTO vendor){
        int msg = vbf.addVendor(vendor);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(msg).build();
    }
    
    @PUT
    @Consumes("application/json")
    public Response updateVendorFromJson(VendorEJBDTO vendor){
        int numOfRowsUpdated = vbf.updateVendor(vendor);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsUpdated).build();
    }
    
    @DELETE
    @Path("/{vendorno}")
    @Consumes("application/json")
    public Response deleteVendorFromJson(@PathParam("vendorno")int vendorno){
        int numOfRowsDeleted = vbf.deleteVendor(vendorno);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsDeleted).build(); 
    }
    
    @GET
    @Path("/{vendorno}")
    @Produces("application/json")
    public List<ProductEJBDTO> getProductsForVendorJson(@PathParam("vendorno") int vendorno){
        return pbf.getAllProductsForVendor(vendorno);
    }

    private ProductBeanFacade lookupProductBeanFacadeBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProductBeanFacade) c.lookup("java:global/CaseTwo/CaseTwo-ejb/ProductBeanFacade!case2ejbs.ProductBeanFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

/*
    @POST
    @Consumes("application/json")
    public Response createVendorFromJson(VendorDTO vendor){
        VendorModel model = new VendorModel();
        String msg = model.addVendor(vendor, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(msg).build();
    }
    
    @GET
    @Produces("application/json")
    public ArrayList<VendorDTO> getVendorsJson() {
        VendorModel model = new VendorModel();
        return model.getVendors(ds);
    }
    
    @PUT
    @Consumes("application/json")
    public Response updateVendorFromJson(VendorDTO vendor){
        VendorModel model = new VendorModel();
        int numOfRowsUpdated = model.updateVendor(vendor, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsUpdated).build();
    }
    
    @DELETE
    @Path("/{vendorno}")
    @Consumes("application/json")
    public Response deleteVendorFromJson(@PathParam("vendorno")int vendorno){
        VendorModel model = new VendorModel();
        int numOfRowsDeleted = model.deleteVendor(vendorno, ds);
        URI uri = context.getAbsolutePath();
        System.out.println("number of rows deleted " + numOfRowsDeleted);
        return Response.created(uri).entity(numOfRowsDeleted).build(); 
    }    
    
    @GET
    @Path("/{vendorno}")
    @Produces("application/json")
    public ArrayList<ProductDTO> getVendorProductsJson(@PathParam("vendorno") int vendorno){
        ProductModel model = new ProductModel();
        return model.getAllProductsForVendor(vendorno, ds); 
    }
    */