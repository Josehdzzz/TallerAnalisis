package ccr.ac.ucr.paraiso.codigo;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class UserController extends HttpServlet {

    //entradas vulnerables para sql injection
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb"; //jp obviamente esta es una ruta de ejemplo, no la pruebe
    private static final String USER = "root"; //porque usualmente al explotar una vulnerabilidad se intenta ser usuario root
    private static final String PASS = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //el parametro que recibe desde la url
        String username = request.getParameter("username");  
        String search = request.getParameter("search");      

        response.setContentType("text/html");

        // parte del xxs
        // el parámetro se imprime directo
        response.getWriter().println("<h2>Hola " + username + "</h2>");

        Connection conn = null;
        Statement stmt = null; //vea aqui jp

        try {
            // no se valida adecuadamente
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // aqui es todavia pero un sql injection
            
            String sql = "SELECT * FROM users WHERE name = '" + search + "'";
            ResultSet rs = stmt.executeQuery(sql);

            response.getWriter().println("<ul>");
            while (rs.next()) {
                response.getWriter().println("<li>" + rs.getString("name") + "</li>");
            }
            response.getWriter().println("</ul>");

        } catch (SQLException e) {
            response.getWriter().println("Error en la base de datos: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}
