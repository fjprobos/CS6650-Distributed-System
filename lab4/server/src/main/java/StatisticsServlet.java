import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "StatisticsServlet", value = "/statistics")
public class StatisticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL
        if (urlPath != null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("too many paramterers");
            return;
        }

        res.setStatus(HttpServletResponse.SC_OK);

        // TODO: Implement Actual Logic Below
        res.getWriter().write("200 OK GET RECEIVED: get the API performance stats ");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}