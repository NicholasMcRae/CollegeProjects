/*
 * Produce purchase order line items and pdfs
 * Sep/ 27th - Initial Implementation
 */
package models;

import dtos.DisplayDTO;
import dtos.PurchaseOrderLineItemDTO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.sql.DataSource;

/**
 *
 * @author nickm
 */public class ProductOrderModel {
    
    public String purchaseOrderAdd(double total, int vendorno, PurchaseOrderLineItemDTO[] items, DataSource ds)
    {
        PreparedStatement pstmt;
        Connection con = null;
        String msg = "";
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        
        String sql = "INSERT INTO PURCHASEORDERS (VENDORNO,AMOUNT,PodATE)"
                + " VALUES (?,?,?)";
        
        try{
            con = ds.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, vendorno);            
            pstmt.setBigDecimal(2, BigDecimal.valueOf(total));
            pstmt.setString(3, currentDate);
            pstmt.execute();            
            ResultSet rs = pstmt.getGeneratedKeys();
            int poNum = 0;
            if (rs != null && rs.next()) {
                poNum = rs.getInt(1);
            }
            
            for(PurchaseOrderLineItemDTO item : items)
            {
                if(item.getQuantity() > 0){
                    sql = "INSERT INTO PurchaseOrderLineItems (PONUMBER,PRODCD,QTY,PRICE) VALUES (?,?,?,?)";
                    pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pstmt.setInt(1, poNum);
                    pstmt.setString(2, item.getProductCode());
                    pstmt.setInt(3, item.getQuantity());
                    pstmt.setBigDecimal(4, BigDecimal.valueOf(item.getPrice()));
                    pstmt.execute();
                }
            }
            con.commit();
            msg = "" + poNum + "";
            con.close();
        }
        catch (SQLException se) {
            //Handle erroresultSet for JDBC
            System.out.println("SQL issue " + se.getMessage());
            msg = "PO not added ! - " + se.getMessage();
            try{
                con.rollback();
            } catch (SQLException sqx){
                System.out.println("Rollback failed - " + sqx.getMessage());
            }
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
    
    public ArrayList<DisplayDTO> getPDFData(int PONUM, DataSource ds) {
        PreparedStatement preparedStatement;
        Connection connection = null;
        ArrayList<DisplayDTO> items = new ArrayList();
        String sql = "SELECT PRODUCTCODE,VENDORNO, PRODUCTNAME, QTY, COST FROM PURCHASEORDERLINEITEMS PLI " +
                "JOIN PRODUCTS P ON P.PRODUCTCODE = PLI.PRODCD WHERE PLI.PONUMBER = " + PONUM;
        
        try {
            connection = ds.getConnection();            
            preparedStatement = connection.prepareStatement(sql);  
            ResultSet resultSet = preparedStatement.executeQuery();
            DisplayDTO displayDTO;                
            while(resultSet.next()){
                displayDTO = new DisplayDTO(); 
                displayDTO.setCode(resultSet.getString(1));
                displayDTO.setVendorno(resultSet.getInt(2));
                displayDTO.setName(resultSet.getString(3));
                displayDTO.setQuantity(resultSet.getInt(4));
                displayDTO.setPrice(resultSet.getDouble(5));                
                
                items.add(displayDTO);
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
        return items;
   }
    
}
