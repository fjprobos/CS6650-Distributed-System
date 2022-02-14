import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SkierServlet", value = "/skiers")
public class SkierServlet extends HttpServlet {

    // POJO to store extracted variable from url
    private variablePOJO pojo = new variablePOJO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        if (!isUrlValidGet(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("404 Invalid URL");
        } else {
            if (urlParts.length == 8) {
                res.setStatus(HttpServletResponse.SC_OK);

                // TODO: Implement Actual Logic Below
                res.getWriter().write("200 OK GET RECEIVED: get ski day vertical for a skier");
                String jsonString = this.gson.toJson(pojo);
                PrintWriter out = res.getWriter();
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                out.print(jsonString);
                out.flush();
                pojo = new variablePOJO();

            } else if (urlParts.length == 3) {
                res.setStatus(HttpServletResponse.SC_OK);

                // TODO: Implement Actual Logic Below
                res.getWriter().write("200 OK GET RECEIVED: get the total vertical for the skier for specified seasons at the specified resort");
                String jsonString = this.gson.toJson(pojo);
                PrintWriter out = res.getWriter();
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                out.print(jsonString);
                out.flush();
                pojo = new variablePOJO();

            } else {
                throw new ServletException("ServletException in GET");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        if (!isUrlValidPost(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("404 Invalid URL");
        } else {

            res.setStatus(HttpServletResponse.SC_OK);

            // TODO: Implement Actual Logic Below
            String s = req.getReader().lines().collect(Collectors.joining());
            res.getWriter().write("200 OK POST RECEIVED");
            res.getWriter().write(s);

            String jsonString = this.gson.toJson(pojo);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(jsonString);
            out.flush();
            pojo = new variablePOJO();

        }
    }

    private boolean isUrlValidGet(String[] urlPaths) {
//    GET/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
//    get ski day vertical for a skier
//    GET/skiers/{skierID}/vertical
//    get the total vertical for the skier for specified seasons at the specified resort

        if (!urlPaths[0].equals("")) {
            return false;
        }

        if (urlPaths.length == 3) {

            // skierID
            try {
                int x = Integer.parseInt(urlPaths[1]);
                pojo.setSkierID(x);
            } catch (NumberFormatException e) {
                return false;
            }

            // vertical
            if (!urlPaths[2].equals("vertical")) {
                return false;
            }

            return true;

        } else if (urlPaths.length == 8) {

            // resortID
            try {
                int x = Integer.parseInt(urlPaths[1]);
                pojo.setResortID(x);
            } catch (NumberFormatException e) {
                return false;
            }

            // seasons
            if (!urlPaths[2].equals("seasons")) {
                return false;
            }

            // seasonID
            try {
                int x = Integer.parseInt(urlPaths[3]);
                pojo.setSeasonID(x);
            } catch (NumberFormatException e) {
                return false;
            }

            // days
            if (!urlPaths[4].equals("days")) {
                return false;
            }

            // dayID
            try {
                int x = Integer.parseInt(urlPaths[5]);
                pojo.setDayID(x);
            } catch (NumberFormatException e) {
                return false;
            }

            // skiers
            if (!urlPaths[6].equals("skiers")) {
                return false;
            }

            // skierID
            try {
                int x = Integer.parseInt(urlPaths[7]);
                pojo.setSkierID(x);
            } catch (NumberFormatException e) {
                return false;
            }

            return true;

        } else {

            return false;

        }
    }

    private boolean isUrlValidPost(String[] urlPaths) {
//    POST/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
//    write a new lift ride for the skier

        if (urlPaths.length != 8) {
            return false;
        }

        if (!urlPaths[0].equals("")) {
            return false;
        }

        // resortID
        try {
            int x = Integer.parseInt(urlPaths[1]);
            pojo.setResortID(x);
        } catch (NumberFormatException e) {
            return false;
        }

        // seasons
        if (!urlPaths[2].equals("seasons")) {
            return false;
        }

        // seasonsID
        try {
            int x = Integer.parseInt(urlPaths[3]);
            pojo.setSeasonID(x);
        } catch (NumberFormatException e) {
            return false;
        }

        // days
        if (!urlPaths[4].equals("days")) {
            return false;
        }

        //dayID
        try {
            int x = Integer.parseInt(urlPaths[5]);
            pojo.setDayID(x);
        } catch (NumberFormatException e) {
            return false;
        }

        // skiers
        if (!urlPaths[6].equals("skiers")) {
            return false;
        }

        // skierID
        try {
            int x = Integer.parseInt(urlPaths[7]);
            pojo.setSkierID(x);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}