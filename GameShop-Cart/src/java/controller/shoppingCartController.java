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
import model.GamesDAO;
import model.GamesDTO;

/**
 *
 * @author Admin
 */
@WebServlet(name = "shoppingCartController", urlPatterns = {"/shoppingCartController"})
public class shoppingCartController extends HttpServlet {

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
        } else if (action.equals("addToCart")) {
            List<GamesDTO> shoppingCartList = (List<GamesDTO>) session.getAttribute("shoppingCartList");

            if (shoppingCartList == null) {
                shoppingCartList = new ArrayList<>();
            }

            int id = Integer.parseInt(request.getParameter("id"));
            GamesDTO games = GamesDAO.load(id);

            if (games != null && shoppingCartList.stream().noneMatch(g -> g.getGameId() == games.getGameId()))  { //de check xem co trung id trong shopping cart, neu co thi chi dc add 1 lan
                shoppingCartList.add(games);
            }
            List<GamesDTO> list = GamesDAO.list(keyword, sortCol);
            session.setAttribute("shoppingCartList", shoppingCartList);
            request.setAttribute("gameslist", list);
            request.getRequestDispatcher("homePage.jsp").forward(request, response);

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
