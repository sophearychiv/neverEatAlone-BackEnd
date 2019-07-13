package neverEatAlone;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.put;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

public class APIs {
    public static void main(String[] args) throws SQLException {
    	

         
    	
        final UserService userService = new UserServiceMapImpl();

        post("/users", (request, response) -> {
            response.type("application/json");

            User user = new Gson().fromJson(request.body(), User.class);
            userService.addUser(user);
            
            System.out.println("executed");
            
        	String url = "jdbc:mysql://localhost:3306/users";
        	String databaseUser = "student";
        	String password = "student";
        	
        	 Connection myConn = null;
             Statement myStmt = null;
      
//             try {
                 // 1. Get a connection to database
                 myConn = DriverManager.getConnection(url, databaseUser, password);
      
                 // 2. Create a statement
                 myStmt = myConn.createStatement();
      
                 // 3. Execute SQL query
                 String sql = "insert into users " + " (last_name, first_name, email)"
                         + " values ('" + user.getLastName() + "','" + user.getFirstName() + "','" + user.getEmail() + "')";

                 myStmt.executeUpdate(sql);
      
//                 System.out.println("Insert complete.");
//             } catch (Exception exc) {
//                 exc.printStackTrace();
//             } finally {
//                 if (myStmt != null) {
//                     myStmt.close();
//                 }
//      
//                 if (myConn != null) {
//                     myConn.close();
//                 }
//             }
            

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            
        });

        get("/users", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userService.getUsers())));
        });

        get("/users/:id", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userService.getUser(request.params(":id")))));
        });

        put("/users/:id", (request, response) -> {
            response.type("application/json");

            User toEdit = new Gson().fromJson(request.body(), User.class);
            User editedUser = userService.editUser(toEdit);

            if (editedUser != null) {
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(editedUser)));
            } else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, new Gson().toJson("User not found or error in edit")));
            }
        });

        delete("/users/:id", (request, response) -> {
            response.type("application/json");

            userService.deleteUser(request.params(":id"));
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "user deleted"));
        });

        options("/users/:id", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, (userService.userExist(request.params(":id"))) ? "User exists" : "User does not exists"));
        });

    }

}