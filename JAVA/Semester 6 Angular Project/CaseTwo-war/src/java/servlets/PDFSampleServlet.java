package servlets;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dtos.DisplayDTO;
import dtos.VendorDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import models.ProductOrderModel;
import models.VendorModel;
import java.util.Currency;
import java.util.Locale;

/**
 *
 * @author Evan
 */
@WebServlet(name = "PDFSampleServlet", urlPatterns = {"/PDFSample"})
public class PDFSampleServlet extends HttpServlet {

    private int pono;
    @Resource(lookup = "jdbc/Info5059db")
    DataSource ds;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            buildpdf(request, response);
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }    
    }
    private void buildpdf(HttpServletRequest request, HttpServletResponse response) {
        pono = Integer.parseInt(request.getParameter("po"));
        Font catFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);        
        //String appRealPath = super.getServletContext().getRealPath("/");
        //String IMG = appRealPath + "\random.png";
        String relativeWebPath = "\random.png";
        //String IMG = getServletContext().getRealPath(relativeWebPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            ArrayList <DisplayDTO> data = new ArrayList();
            VendorDTO vendor = new VendorDTO();
            ProductOrderModel model = new ProductOrderModel();
            data = model.getPDFData(pono, ds);
            VendorModel vendorModel = new VendorModel();
            int vendorNo = data.get(0).getVendorno();
            vendor = vendorModel.getVendor(ds, vendorNo);
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph preface = new Paragraph();
            // We add one empty line
            //Image image1 = Image.getInstance(IMG);
            //image1.setAbsolutePosition(55f, 760f);
            //preface.add(image1);
            preface.setAlignment(Element.ALIGN_RIGHT);
            // Lets write a big header
            Paragraph mainHead = new Paragraph(String.format("%55s", "Purchase Order"), catFont);
            preface.add(mainHead);
            preface.add(new Paragraph(String.format("%82s", "PO Num: " + pono ), subFont));
            addEmptyLine(preface, 5);
            
            PdfPTable vendorTable = new PdfPTable(1);
            vendorTable.setWidthPercentage(20);
            vendorTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cell = new PdfPCell(new Paragraph("Vendor"));
            
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            vendorTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(vendor.getName()));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            vendorTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(vendor.getAddress1()));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            vendorTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(vendor.getCity()));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            vendorTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(vendor.getProvince()));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            vendorTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(vendor.getPostalCode()));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            vendorTable.addCell(cell);

            // 5 column table
            PdfPTable table = new PdfPTable(5);
            cell = new PdfPCell(new Paragraph("Product", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Description", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Quantity", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Total Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            
            double totalCost = 0.0;
            Locale locale = new Locale("en", "US");      
            Currency currency = Currency.getInstance(locale);
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            
            for(DisplayDTO item : data)
            {
                cell = new PdfPCell(new Phrase(item.getCode()));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(item.getName()));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + item.getQuantity() + ""));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormatter.format(item.getPrice())));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                double itemPrice = item.getPrice() * item.getQuantity();
                DecimalFormat df = new DecimalFormat("#.00");      
                itemPrice = Double.valueOf(df.format(itemPrice));
                totalCost += itemPrice;
                cell = new PdfPCell(new Phrase(currencyFormatter.format(itemPrice)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }            
            PdfPTable totalTable = new PdfPTable(2);
            cell = new PdfPCell(new Phrase("Before Tax:"));            
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(cell);
            DecimalFormat df = new DecimalFormat("#.00");      
            totalCost = Double.valueOf(df.format(totalCost));
            cell = new PdfPCell(new Phrase(currencyFormatter.format(totalCost)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT); 
            cell.setBackgroundColor(BaseColor.YELLOW);
            totalTable.addCell(cell);            
            cell = new PdfPCell(new Phrase("Tax"));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(cell);            
            double tax = totalCost * 0.13;
            cell = new PdfPCell(new Phrase(currencyFormatter.format(tax)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);       
            cell.setBackgroundColor(BaseColor.YELLOW);
            totalTable.addCell(cell);            
            cell = new PdfPCell(new Phrase("Total:"));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(cell);
            double tot = totalCost + tax;
            cell = new PdfPCell(new Phrase(currencyFormatter.format(tot)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);  
            cell.setBackgroundColor(BaseColor.YELLOW);
            totalTable.addCell(cell);
            preface.add(vendorTable);
            addEmptyLine(preface, 2);
            preface.add(table);
            addEmptyLine(preface, 1);
            preface.add(totalTable);
            addEmptyLine(preface, 3);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.add(new Paragraph(String.format("%60s", new Date()), subFont));
            document.add(preface);
            document.close();
            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
            
        }

   private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }    
   
   
   
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
