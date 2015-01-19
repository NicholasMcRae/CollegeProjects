package models;

import dtos.VendorDTO;
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
 *  Sep 9th - Added retrieve and update functions
 *  Sep 12th - Commented and refactored class
 *  Sep 19th - Added delete function
 */

/*
    Original class included in project package
*/


@Named(value = "vendorModel")  
@RequestScoped
public class VendorModel implements Serializable {

    public VendorModel() {
    }
    
    /*
    Author: CouresultSete professor
    Purpose: add vendor to database from app
    Input: vendor object, data source
    Output: result message
    */
    public String addVendor(VendorDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String msg = "";

        String sql = "INSERT INTO Vendors (Address1,City,Province,PostalCode,"
                + "Phone,VendorType,Name,Email) "
                + " VALUES (?,?,?,?,?,?,?,?)";

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getAddress1());
            pstmt.setString(2, details.getCity());
            pstmt.setString(3, details.getProvince());
            pstmt.setString(4, details.getPostalCode());
            pstmt.setString(5, details.getPhone());
            pstmt.setString(6, details.getType());
            pstmt.setString(7, details.getName());
            pstmt.setString(8, details.getEmail());
            pstmt.execute();
            int vendorno;

            try (ResultSet resultSet = pstmt.getGeneratedKeys()) {
                resultSet.next();
                vendorno = resultSet.getInt(1);
            }
            con.close();

            if (vendorno > 0) {
                msg = "Vendor " + vendorno + " Added!";
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
    Purpose: update vendor from app
    Input: vendor object, data source
    Output: rows updated, which specifies success or failure
    */
     public int updateVendor(VendorDTO details, DataSource ds) {
        PreparedStatement preparedStatement;
        Connection connection = null;
        int updateCount = 0;

        String sql =         
                
                "UPDATE VENDORS SET ADDRESS1 = '" + details.getAddress1() + "', CITY = '" + details.getCity() + 
                "',PROVINCE = '" + details.getProvince() + "',POSTALCODE = '" + details.getPostalCode() + "',PHONE = '" + details.getPhone() +
                "',VENDORTYPE = '" + details.getType() + "',NAME = '" + details.getName() + "',EMAIL = '" + details.getEmail() +
                "' WHERE VENDORNO = " + details.getVendorno();
                
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
    Purpose: retrieve all vendoresultSet from database
    Input: data source
    Output: list of vendor objects
    */
    public ArrayList<VendorDTO> getVendors(DataSource ds) {        
        PreparedStatement preparedStatement;
        Connection connection = null;
        ArrayList<VendorDTO> vendors = new ArrayList();
        String sql = "SELECT * FROM Vendors";
        
        try {
            connection = ds.getConnection();            
            preparedStatement = connection.prepareStatement(sql);  
            ResultSet resultSet = preparedStatement.executeQuery();
            VendorDTO vendorDTO;                
            while(resultSet.next()){
                vendorDTO = new VendorDTO(); 
                vendorDTO.setVendorno(resultSet.getInt(1));
                vendorDTO.setAddress1(resultSet.getString(2));
                vendorDTO.setCity(resultSet.getString(3));
                vendorDTO.setProvince(resultSet.getString(4));
                vendorDTO.setPostalCode(resultSet.getString(5));
                vendorDTO.setPhone(resultSet.getString(6));
                vendorDTO.setType(resultSet.getString(7));
                vendorDTO.setName(resultSet.getString(8));                    
                vendorDTO.setEmail(resultSet.getString(9));
                vendors.add(vendorDTO);
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
        return vendors;
    }
    
    /*
    Purpose: Delete Vendor
    Input: Vendor Number, Data Source
    Output: Deleted Vendor and int
    */
    public int deleteVendor(int vendorNumber, DataSource ds){
        PreparedStatement preparedStatement;
        Connection connection = null;
        String sql = "DELETE FROM Vendors WHERE VENDORNO = " + vendorNumber;
        boolean flag = true;
        
        try {
            connection = ds.getConnection();            
            preparedStatement = connection.prepareStatement(sql);
            flag = preparedStatement.execute();
            System.out.println(flag);
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
    Purpose: retrieve all vendoresultSet from database
    Input: data source
    Output: list of vendor objects
    */
    public VendorDTO getVendor(DataSource ds, int vendorno) {        
        PreparedStatement preparedStatement;
        Connection connection = null;       
        VendorDTO vendorDTO = new VendorDTO();
        String sql = "SELECT * FROM Vendors WHERE VENDORNO = " + vendorno;
        
        try {
            connection = ds.getConnection();            
            preparedStatement = connection.prepareStatement(sql);  
            ResultSet resultSet = preparedStatement.executeQuery();                
            while(resultSet.next()){
                vendorDTO = new VendorDTO(); 
                vendorDTO.setVendorno(resultSet.getInt(1));
                vendorDTO.setAddress1(resultSet.getString(2));
                vendorDTO.setCity(resultSet.getString(3));
                vendorDTO.setProvince(resultSet.getString(4));
                vendorDTO.setPostalCode(resultSet.getString(5));
                vendorDTO.setPhone(resultSet.getString(6));
                vendorDTO.setType(resultSet.getString(7));
                vendorDTO.setName(resultSet.getString(8));                    
                vendorDTO.setEmail(resultSet.getString(9));
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
        return vendorDTO;
    }
}