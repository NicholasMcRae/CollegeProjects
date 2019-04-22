package models;

import dtos.ProductDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 
import javax.enterprise.context.RequestScoped;  
import javax.inject.Named;
import javax.sql.DataSource;

/*
 * VendorModel.java
 *
 * Created on Aug 31, 2013, 3:03 PM
 *  Purpose:    Contains methods for supporting db access for vendor information
 *              Usually consumed by the ViewModel Class via DTO
 *  Revisions: 
 *  Sep 18th - Initial Implementation
 */


@Named(value = "productModel")  
@RequestScoped
public class ProductModel implements Serializable {

    public ProductModel() {
    }
    
    /*
    Purpose: add product to database
    Input: product object, data source
    Output: result message
    */
    public String addProduct(ProductDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String msg = "";

        String sql = "INSERT INTO PRODUCTS (PRODUCTCODE,VENDORNO,SKU,PRODUCTNAME,COST,"
                + "MSRP,ROP,EOQ,QOH,QOD) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getCode());
            pstmt.setInt(2, details.getVendorno());
            pstmt.setString(3, details.getSku());
            pstmt.setString(4, details.getName());
            pstmt.setDouble(5, details.getCost());
            pstmt.setDouble(6, details.getMsrp());
            pstmt.setInt(7, details.getRop());
            pstmt.setInt(8, details.getEoq());
            pstmt.setInt(9, details.getQoh());
            pstmt.setInt(10, details.getQod());
            pstmt.execute();
            int productno;

            try (ResultSet resultSet = pstmt.getGeneratedKeys()) {
                resultSet.next();
                productno = resultSet.getInt(1);
            }
            con.close();

            if (productno > 0) {
                msg = "Product " + productno + " Added!";
            } else {
                msg = "Vendor Not Added!";
            }
        } catch (SQLException se) {
            //Handle erroresultSet for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other erroresultSet
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return msg;
    }
    
    /*
    Purpose: update product from app
    Input: vendor object, data source
    Output: rows updated, which specifies success or failure
    */
     public int updateProduct(ProductDTO details, DataSource ds) {
        PreparedStatement preparedStatement;
        Connection connection = null;
        int updateCount = 0;

        String sql = "UPDATE PRODUCTS SET VENDORNO = " + details.getVendorno() + 
                ",SKU = '" + details.getSku() + "',PRODUCTNAME = '" + details.getName() + "',COST = " + details.getCost() +
                ",MSRP = " + details.getMsrp() + ",ROP = " + details.getRop() + ",EOQ = " + details.getEoq() + ", QOH = " + details.getQoh() +
                ", QOD = " + details.getQod() + " WHERE PRODUCTCODE = '" + details.getCode() + "'";
        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(sql);   
            preparedStatement.execute();            
            updateCount = preparedStatement.getUpdateCount();
            connection.close();            
        } catch (SQLException se) {
            System.out.println("SQL issue: " + se.getMessage());
        } catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return updateCount;
    }
    
    /*
    Purpose: retrieve all products from database
    Input: data source
    Output: list of vendor objects
    */
    public ArrayList<ProductDTO> getProducts(DataSource ds) {        
        PreparedStatement preparedStatement;
        Connection connection = null;
        ArrayList<ProductDTO> products = new ArrayList();
        String sql = "SELECT * FROM Products";
        
        try {
            connection = ds.getConnection();            
            preparedStatement = connection.prepareStatement(sql);  
            ResultSet resultSet = preparedStatement.executeQuery();
            ProductDTO productDTO;                
            while(resultSet.next()){
                productDTO = new ProductDTO(); 
                productDTO.setCode(resultSet.getString(1));
                productDTO.setVendorno(resultSet.getInt(2));
                productDTO.setSku(resultSet.getString(3));
                productDTO.setName(resultSet.getString(4));
                productDTO.setCost(resultSet.getDouble(5));
                productDTO.setMsrp(resultSet.getDouble(6));
                productDTO.setRop(resultSet.getInt(7));
                productDTO.setEoq(resultSet.getInt(8));                    
                productDTO.setQoh(resultSet.getInt(9));                                   
                productDTO.setQod(resultSet.getInt(10));
                products.add(productDTO);
            }
            connection.close();
            
       }catch (SQLException se) {
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }
        }        
        return products;
    }
    
    /*
    Purpose: Delete Product
    Input: Product Number, Data Source
    Output: Deleted Product and int
    */
    public int deleteProduct(String productCode, DataSource ds){
        PreparedStatement preparedStatement;
        Connection connection = null;
        String sql = "DELETE FROM PRODUCTS WHERE PRODUCTCODE = '" + productCode + "'";
        boolean flag = true;
        
        try {
            connection = ds.getConnection();            
            preparedStatement = connection.prepareStatement(sql);  
            flag = preparedStatement.execute();
            connection.close();
       }catch (SQLException se) {
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }
        }        
            
        if(flag)
        {
            return -1;
        }
                    
        return 1;
    }   
    
    /*
    Purpose: retrieve all products for a specific vendor
    Input: data source, vendor no
    Output: list of product objects 
    */
    public ArrayList<ProductDTO> getAllProductsForVendor(int vendorno, DataSource ds) {        
        PreparedStatement preparedStatement;
        Connection connection = null;
        ArrayList<ProductDTO> products = new ArrayList();
        String sql = "SELECT * FROM Products WHERE VENDORNO = " + vendorno;
        
        try {
            connection = ds.getConnection();            
            preparedStatement = connection.prepareStatement(sql);  
            ResultSet resultSet = preparedStatement.executeQuery();
            ProductDTO productDTO;                
            while(resultSet.next()){
                productDTO = new ProductDTO(); 
                productDTO.setCode(resultSet.getString(1));
                productDTO.setVendorno(resultSet.getInt(2));
                productDTO.setSku(resultSet.getString(3));
                productDTO.setName(resultSet.getString(4));
                productDTO.setCost(resultSet.getDouble(5));
                productDTO.setMsrp(resultSet.getDouble(6));
                productDTO.setRop(resultSet.getInt(7));
                productDTO.setEoq(resultSet.getInt(8));                    
                productDTO.setQoh(resultSet.getInt(9));                                   
                productDTO.setQod(resultSet.getInt(10));
                products.add(productDTO);
            }
            connection.close();
            
       }catch (SQLException se) {
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            System.out.println("other issue " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }
        }        
        return products;
    }
}
