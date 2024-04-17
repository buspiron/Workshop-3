package pl.coderslab.users;

import pl.coderslab.utils.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/delete")
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userToDelete;
        try {
            userToDelete = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e){
            userToDelete = -1;
        }

        if(userToDelete > 0){
            UserDao userDao = new UserDao();
            userDao.delete(userToDelete);
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/user/list");
    }

}
