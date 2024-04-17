package pl.coderslab.users;

import pl.coderslab.utils.User;
import pl.coderslab.utils.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/show")
public class ShowUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User();
        int userId;
        try {
            userId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e){
            userId = -1;
        }

        if(userId > 0){
            UserDao userDao = new UserDao();
            user = userDao.read(userId);
        }
        req.setAttribute("userData", user);
        getServletContext().getRequestDispatcher("/users/show.jsp").forward(req, resp);
    }
}
