<%-- 
    Document   : homePage
    Created on : Feb 23, 2025, 9:51:19 PM
    Author     : Quoc Bao
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.GamesDTO"%>
<%@page import="model.GamesDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Day la trang chu tam thoi!!!</h1>
        <a href='shoppingCartController?action=cartList'>Show cart list</a>
        <table>
            <tr>
                <td>Id</td>
                <td>Game Name</td>
            </tr>
            <%
                List<GamesDTO> list = (List<GamesDTO>) request.getAttribute("gameslist");
                if (list != null) {
                    for (GamesDTO games : list) {
                        pageContext.setAttribute("games", games);
            %>
            <tr>
                <td><a href="gameController?id=${games.gameId}"> ${games.gameId} </a></td>
                <td>${games.title}</td>


                <td colspan="5">
                    <form action="shoppingCartController" method="POST">
                        <input name="action" value="addToCart" type="hidden">
                        <input type="hidden" name="id" value="${games.gameId}">
                        <input type="submit" value="Add to cart">
                    </form> 
                </td>
            </tr>
            <%
                    }
                }
            %>

        </table>


    </body>
</html>
