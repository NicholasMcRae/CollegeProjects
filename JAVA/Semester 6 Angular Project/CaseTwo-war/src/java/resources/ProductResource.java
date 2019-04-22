package resources;

import case2ejbs.ProductBeanFacade;
import dtos.ProductEJBDTO;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

/**
 * REST Web Service
 *
 * @author Nick
 */
@Path("product")
@RequestScoped
public class ProductResource {
       
    ProductBeanFacade pbf = lookupProductBeanFacadeBean();
    
    private ProductBeanFacade lookupProductBeanFacadeBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProductBeanFacade) c.lookup("java:global/CaseTwo/CaseTwo-ejb/ProductBeanFacade!case2ejbs.ProductBeanFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    @Context
    private UriInfo context;
    
    @POST
    @Consumes("application/json")
    public Response addProduct(ProductEJBDTO product){
        String msg = pbf.addProduct(product);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(msg).build();
    }
    
    @GET
    @Produces("application/json")
    public List<ProductEJBDTO> getProductsJson() {
        List<ProductEJBDTO> products = pbf.getProducts();
        return products;
    }
    
    
    @PUT
    @Consumes("application/json")
    public Response updateProductFromJson(ProductEJBDTO product){        
        int numOfRowsUpdated = pbf.updateProduct(product);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsUpdated).build();
    }
    
    @DELETE
    @Path("/{code}")
    @Consumes("application/json")
    public Response deleteProductFromJson(@PathParam("code")String code){
        int numOfRowsDeleted = pbf.deleteProduct(code);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsDeleted).build(); 
    }
    
    @GET
    @Path("/{code}")
    @Produces("application/json")
    public ProductEJBDTO getProductJson(@PathParam("code")String code) {
        ProductEJBDTO product = pbf.getProduct(code);
        return product;
    }
    
    @GET
    @Path("products/vendorno")
    @Produces("application/json")
    public List<ProductEJBDTO> getProductsForVendorJson(@PathParam("vendorno") int vendorno){
        return pbf.getAllProductsForVendor(vendorno);
    }
           
    public ProductResource() {
    }

    
}

/*
@POST
    @Consumes("application/json")
    public Response addProduct(ProductDTO product){
        ProductModel model = new ProductModel();
        String msg = model.addProduct(product, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(msg).build();
    }
    
    @GET
    @Produces("application/json")
    public ArrayList<ProductDTO> getProductsJson() {
        ProductModel model = new ProductModel();
        return model.getProducts(ds);
    }
    
    @PUT
    @Consumes("application/json")
    public Response updateProductFromJson(ProductDTO product){
        ProductModel model = new ProductModel();
        int numOfRowsUpdated = model.updateProduct(product, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsUpdated).build();
    }
    
    @DELETE
    @Path("/{code}")
    @Consumes("application/json")
    public Response deleteProductFromJson(@PathParam("code")String code){
        ProductModel model = new ProductModel();
        int numOfRowsDeleted = model.deleteProduct(code, ds);
        URI uri = context.getAbsolutePath();
        System.out.println("number of rows deleted " + numOfRowsDeleted);
        return Response.created(uri).entity(numOfRowsDeleted).build(); 
    }
*/
