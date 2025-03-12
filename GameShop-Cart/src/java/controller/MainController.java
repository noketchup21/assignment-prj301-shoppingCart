/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CouponsDAO;
import model.CouponsDTO;
import model.GamesDAO;
import model.GamesDTO;
import model.UsersDAO;
import model.UsersDTO;

/**
 *
 * @author Admin
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

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

            String action = request.getParameter("action");
            String keyword = request.getParameter("keyword");
            if (keyword == null) {
                keyword = "";
            }
            GamesDAO gamesDAO = new GamesDAO();

            HttpSession session = request.getSession(false);
            if (!"login".equals(action)) {
                if (session == null || session.getAttribute("user") == null) {
                    response.sendRedirect("loginPage.jsp");
                    return;
                }
            }
            String sortCol = request.getParameter("colSort");
            if (action == null) {
                action = "";
            }
            switch (action) {
                case "login":
                    handleLogin(request, response);
                    //                   handleList(request, response);
                    break;
                case "logout":

                    break;
                case "list":
                case "":
                    handleList(request, response);
                    break;
                case "cartList":
                    handleCartList(request, response);
                    break;
                case "addToCart":
                    handleAddToCart(request, response);
                    break;
                case "delete":
                    handleCartDelete(request, response);
                    break;
                case "quantity":
                    handleQuantity(request, response);
                    break;
                case "applyCoupon":
                    handleApplyCoupon(request, response);
                    break;
            }
        } catch (Exception e) {
            log("Error at MainController: ", e);
        }

    }

    protected void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            request.setCharacterEncoding("utf-8");
            String mail = request.getParameter("txtmail");
            String password = request.getParameter("txtpassword");
            if (mail != null && password != null) {
                UsersDAO d = new UsersDAO();
                UsersDTO user = d.checkLogin(mail, password);
                if (user == null) {
                    request.setAttribute("WARNING", "Login fail");
                    request.getRequestDispatcher("loginPage.jsp").forward(request, response);
                } else {
                    HttpSession s = request.getSession();
                    s.setAttribute("user", user);
                    response.sendRedirect("MainController");
                }
            } else {
                request.setAttribute("WARNING", "Email hoac mat khau chua duoc nhap");
                request.getRequestDispatcher("loginPage.jsp").forward(request, response);
            }

        }
    }

    protected void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        String sortCol = request.getParameter("colSort");
        GamesDAO dao = new GamesDAO();
        List<GamesDTO> list = dao.list(keyword, sortCol);
        request.setAttribute("gameslist", list);

        request.getRequestDispatcher("homePage.jsp").forward(request, response);
    }

    protected void handleCartList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        String sortCol = request.getParameter("colSort");

        GamesDAO gamesDAO = new GamesDAO();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return;
        }

        if (action.equals("cartList")) {
            List<GamesDTO> shoppingCartList = (List<GamesDTO>) session.getAttribute("shoppingCartList");

            if (shoppingCartList == null) {
                shoppingCartList = new ArrayList<>();
                session.setAttribute("shoppingCartList", shoppingCartList);
            }

            request.setAttribute("shoppingCartList", shoppingCartList);
            request.getRequestDispatcher("shoppingCart.jsp").forward(request, response);
        }
    }

    protected void handleAddToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        String sortCol = request.getParameter("colSort");

        GamesDAO gamesDAO = new GamesDAO();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return;
        }

        if (action.equals("addToCart")) {
            List<GamesDTO> shoppingCartList = (List<GamesDTO>) session.getAttribute("shoppingCartList");

            if (shoppingCartList == null) {
                shoppingCartList = new ArrayList<>();
            }

            int id = Integer.parseInt(request.getParameter("id"));
            GamesDTO games = GamesDAO.load(id);

            if (games != null && shoppingCartList.stream().noneMatch(g -> g.getGameId() == games.getGameId())) { //de check xem co trung id trong shopping cart, neu co thi chi dc add 1 lan
                games.setOrginalPrice(games.getPrice());
                shoppingCartList.add(games);
            }
            List<GamesDTO> list = GamesDAO.list(keyword, sortCol);
            session.setAttribute("shoppingCartList", shoppingCartList);
            request.setAttribute("gameslist", list);
            request.getRequestDispatcher("homePage.jsp").forward(request, response);

        }
    }

    protected void handleCartDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        String sortCol = request.getParameter("colSort");

        GamesDAO gamesDAO = new GamesDAO();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return;
        } else if (action.equals("delete")) {
            List<GamesDTO> shoppingCartList = (List<GamesDTO>) session.getAttribute("shoppingCartList");

            if (shoppingCartList == null) {
                shoppingCartList = new ArrayList<>();
            }

            int id = Integer.parseInt(request.getParameter("id"));
            shoppingCartList.removeIf(game -> game.getGameId() == id); //xoa game co voi id
            session.setAttribute("shoppingCartList", shoppingCartList);
            request.setAttribute("shoppingCartList", shoppingCartList);
            request.getRequestDispatcher("shoppingCart.jsp").forward(request, response);
        }
    }

    protected void handleQuantity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        String sortCol = request.getParameter("colSort");

        GamesDAO gamesDAO = new GamesDAO();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return;
        }

        if (action.equals("quantity")) {
            int gameId = Integer.parseInt(request.getParameter("id"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            List<GamesDTO> shoppingCartList = (List<GamesDTO>) session.getAttribute("shoppingCartList");

            if (shoppingCartList != null) {
                for (GamesDTO game : shoppingCartList) {
                    if (game.getGameId() == gameId) {
                        game.setQuantity(quantity); // Ensure GamesDTO has a setQuantity method
                        break;
                    }
                }
            }

            session.setAttribute("shoppingCartList", shoppingCartList);
            request.setAttribute("shoppingCartList", shoppingCartList);
            request.getRequestDispatcher("shoppingCart.jsp").forward(request, response);
        }
    }
    
        protected void handleApplyCoupon(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        String sortCol = request.getParameter("colSort");

        GamesDAO gamesDAO = new GamesDAO();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return;
        }

        if (action.equals("applyCoupon")) {
            String code = request.getParameter("code");
            CouponsDAO dao = new CouponsDAO(); 

            List<GamesDTO> shoppingCartList = (List<GamesDTO>) session.getAttribute("shoppingCartList");
            boolean check = dao.validateCoupon(code);
            if(!check) {
                request.setAttribute("errorCode", "Code khong hop le hoac het hieu luc");
                request.setAttribute("shoppingCartList", shoppingCartList);
                request.getRequestDispatcher("shoppingCart.jsp").forward(request, response);
                return;
            } else{
                int discount = dao.getDiscount(code);
                for (GamesDTO game : shoppingCartList) {
                    double discountedPrice = game.getOrginalPrice()* (1 - discount / 100.0);
                    game.setPrice(discountedPrice);
                }
                request.setAttribute("couponApplied", true);
            }
            

            session.setAttribute("shoppingCartList", shoppingCartList);
            request.setAttribute("shoppingCartList", shoppingCartList);
            request.getRequestDispatcher("shoppingCart.jsp").forward(request, response);
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
