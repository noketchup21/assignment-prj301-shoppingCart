<%-- 
    Document   : shoppingCart
    Created on : Mar 2, 2025, 4:56:26 PM
    Author     : Admin
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.GamesDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shopping Cart</title>
    </head>
    <body>
        <a href='MainController?action=list'>Main Page</a>
        <table>
            <tr>
                <td>Id</td>
                <td>Game Name</td>
            </tr>
            <%
                List<GamesDTO> list = (List<GamesDTO>) request.getAttribute("shoppingCartList");
                for (GamesDTO games : list) {
                    pageContext.setAttribute("games", games);
            %>
            <tr>
                <td>
                    <a href="MainController?id=${games.gameId}"> ${games.gameId} </a>
                </td>
                <td>${games.title}</td>
                <td>
                    <form action="MainController" method="POST">
                        <input name="action" value="delete" type="hidden">
                        <input name="id" value="${games.gameId}" type="hidden">
                        <input type="submit" value="Delete">
                    </form> 
                </td>
            </tr>
            <%
                }
            %>    
        </table>
    </body>
</html>
