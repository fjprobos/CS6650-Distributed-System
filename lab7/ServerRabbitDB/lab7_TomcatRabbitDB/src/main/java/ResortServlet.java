import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ResortsServlet", value = "/resorts")
public class ResortServlet extends HttpServlet {

    // POJO to store extracted variable from url
    private LiftRide pojo = new LiftRide();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_OK);

            // TODO: Implement Actual Logic Below
            res.getWriter().write("200 OK GET RECEIVED: get a list of ski resorts in the database ");
            return;

        }

        String[] urlParts = urlPath.split("/");
        if (!isUrlValidGet(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("404 Invalid URL");
        } else {
            res.setStatus(HttpServletResponse.SC_OK);

            // TODO: Implement Actual Logic Below
            res.getWriter().write("200 OK GET RECEIVED: /resorts/* ");
            String jsonString = this.gson.toJson(pojo);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(jsonString);
            out.flush();
            pojo = new LiftRide();

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
            res.getWriter().write("200 OK POST RECEIVED: /resorts/*");
            res.getWriter().write(s);

            String jsonString = this.gson.toJson(pojo);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(jsonString);
            out.flush();
            pojo = new LiftRide();

        }
    }

    private boolean isUrlValidGet(String[] urlPaths) {
//    GET/resorts/{resortID}/seasons/{seasonID}/day/{dayID}/skiers (Links to an external site.)
//    get number of unique skiers at resort/season/day
//    GET/resorts/{resortID}/seasons (Links to an external site.)
//    get a list of seasons for the specified resort

        if (!urlPaths[0].equals("")) {
            return false;
        }

        if (urlPaths.length == 3) {

            // resortID
            try {
                int x = Integer.parseInt(urlPaths[1]);
                pojo.setResortID(x);
            } catch (NumberFormatException e) {
                return false;
            }

            //seasons
            if (!urlPaths[2].equals("seasons")) {
                return false;
            }

            return true;

        } else if (urlPaths.length == 7) {

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

            // day
            if (!urlPaths[4].equals("day")) {
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

            return true;

        } else {

            return false;

        }

    }

    private boolean isUrlValidPost(String[] urlPaths) {
//    POST/resorts/{resortID}/seasons
//    Add a new season for a resort

        if (urlPaths.length != 3) {
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

        //seasons
        if (!urlPaths[2].equals("seasons")) {
            return false;
        }

        return true;

    }

}