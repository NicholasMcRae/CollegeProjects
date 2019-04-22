package servlets;


import case2ejbs.VendorBeanFacade;
import dtos.VendorEJBDTO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nickm
 */
@WebServlet(name = "VendorServlet", urlPatterns = {"/VendorServlet"})
public class VendorServlet extends HttpServlet {
    @EJB
    private VendorBeanFacade vbf;

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
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Sample</title>");            
            out.println("</head>");
            out.println("<html>");
            out.println("<body>");
            out.println("<h1>Servlet TestVendorFacadeServlet at " + request);
            VendorEJBDTO vendor = new VendorEJBDTO();
            vendor.setAddress1("123 Here");
            vendor.setCity("Nowhere");
            vendor.setEmail("dept@gadgethut.com");
            vendor.setName("Gadget Hut");
            vendor.setPhone("555-5555");
            vendor.setPostalcode("N5Y 3W6");
            vendor.setProvince("ON");
            vendor.setVendortype("trusted");
            int newID = vbf.addVendor(vendor);
            out.println("New vendor row added" + newID);
            out.println("</body>");
            out.println("</html>");
            out.println("</html>");
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
