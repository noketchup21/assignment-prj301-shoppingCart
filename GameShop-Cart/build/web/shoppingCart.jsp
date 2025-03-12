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
        <%
            Boolean couponApplied = (Boolean) request.getAttribute("couponApplied");
            if (request.getAttribute("MESSAGE") != null) {
                out.println("<p style='color:green'>" + request.getAttribute("MESSAGE") + "</p>");
            }
            if (request.getAttribute("ERROR") != null) {
                out.println("<p style='color:red'>" + request.getAttribute("ERROR") + "</p>");
            }
        %>
        <table>
            <tr>
                <td>Id</td>
                <td>Image</td>
                <td>Game Name</td>
                <td>Price</td>
                <td>Quantity</td>
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
                <td><img src="<%= games.getUrl()%>" alt="Game Cover" width="100" height="100"></td>
                <td>${games.title}</td>
                <% if (couponApplied != null && couponApplied) {%>
                <td><%= games.getPrice() * games.getQuantity()%></td>
                <% } else {%>
                <td><%= games.getOrginalPrice() * games.getQuantity()%></td>
                <% } %>
                <td>
                    <form action="MainController" method="POST">
                        <input name="action" value="quantity" type="hidden">
                        <input name="id" value="${games.gameId}" type="hidden">
                        <input type="number" name="quantity" placeholder="Enter quantity" min="1" style="width: 100px;" required>
                        <input type="submit" value="Enter">
                    </form>
                </td>
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
            <tr>
                <%
                    if (couponApplied == null || !couponApplied) {
                %>
                <td colspan="4"><form action="MainController" method="POST">
                        <input type="hidden" name="action" value="applyCoupon">
                        <input type="text" name="code" placeholder="Enter coupon code" required>
                        <input type="submit" value="Apply Coupon">
                    </form></td>
                    <%
                    } else {
                    %>
            <p style="color: green;">Coupon has been applied!</p>
            <%
                }
            %>
        </tr>
        <h3>
            <%
                if (request.getAttribute("errorCode") != null) {
                    out.print(request.getAttribute("errorCode"));
                }
            %>
        </h3>
    </table>
</body>
</html>
