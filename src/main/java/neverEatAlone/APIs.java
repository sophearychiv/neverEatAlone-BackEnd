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
import java.sql.*;

import com.google.gson.Gson;

public class APIs {
    public static void main(String[] args) throws SQLException {
    	
    	
         
    	
        final UserService userService = new UserServiceMapImpl();

        post("/users", (request, response) -> {
            response.type("application/json");

            User user = new Gson().fromJson(request.body(), User.class);
            userService.addUser(user);
            
        	String url = "jdbc:mysql://localhost:3306/users";
        	String databaseUser = "student";
        	String password = "student";
        	
        	 Connection myConn = null;
             Statement myStmt = null;
      
                 // 1. Get a connection to database
                 myConn = DriverManager.getConnection(url, databaseUser, password);
      
                 // 2. Create a statement
                 myStmt = myConn.createStatement();
      
                 // 3. Execute SQL query
                 String sql = "insert into users " + " (last_name, first_name, email)"
                		 + " values ('" + user.getLastName() + "','" + user.getFirstName() + "','" + user.getEmail() + "')";

                 myStmt.executeUpdate(sql);
      
                 System.out.println("Insert complete.");


            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            
        });

        get("/users", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userService.getUsers())));
        });

        get("/users/:id", (request, response) -> {
            response.type("application/json");
            
            String url = "jdbc:mysql://localhost:3306/users";
        	String databaseUser = "student";
        	String password = "student";
        	
    		 Connection myConn = null;
    		 PreparedStatement preparedStmt = null;
     		ResultSet myRs = null;
            
            myConn = DriverManager.getConnection(url, databaseUser , password);
			
			// 2. Prepare statement
			preparedStmt = myConn.prepareStatement("select * from users where id = ?");
			
			// 3. Set the parameters
//			preparedStmt.setDouble(1, Integer.parseInt(request.params(":id")));
			preparedStmt.setString(1, request.params(":id"));
			
			// 4. Execute SQL query
			myRs = preparedStmt.executeQuery();
			String user = null;
			
			while (myRs.next()) {
				String id = myRs.getString("id");
				String lastName = myRs.getString("last_name");
				String firstName = myRs.getString("first_name");
				String email = myRs.getString("email");
				
//				System.out.printf("%s, %s", lastName, firstName);
				user = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(new User(id, firstName, lastName, email))));
			}
			
			return user;
			
			
			
//			String id = request.params(":id");
//			String lastName = myRs.getString("last_name");
//			String firstName = myRs.getString("first_name");
//			String email = myRs.getString("email");
			
//			System.out.printf("%s, %s", lastName, firstName);
//			
//			System.out.println(firstName);
			
			// 5. Display the result set
//			display(myRs);
			
//			user = new User("1", "Adelyn", "Cheng", "adelyn@ada.com");
			
//            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(new User(id, firstName, lastName, email))));
			

//            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userService.getUser(request.params(":id")))));
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
    
    private static void display(ResultSet myRs) throws SQLException {
		while (myRs.next()) {
			String lastName = myRs.getString("last_name");
			String firstName = myRs.getString("first_name");
			
			System.out.printf("%s, %s", lastName, firstName);
		}
	}

}