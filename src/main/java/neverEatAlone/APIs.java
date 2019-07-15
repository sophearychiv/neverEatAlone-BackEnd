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
import java.io.FileReader;
import java.sql.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
        
        post("/interests", (request, response) -> {
            response.type("application/json");

            JsonParser parser = new JsonParser();
            JsonObject interested = parser.parse(request.body()).getAsJsonObject();
            String userId = interested.get("userId").getAsString();
            String restId = interested.get("restId").getAsString();

            
            JsonObject aws = parser.parse(new FileReader("src/main/java/neverEatAlone/secrets.txt")).getAsJsonObject();
            String userName = aws.get("aws_userName").getAsString();
            String password = aws.get("aws_password").getAsString();
            
            String url = "jdbc:mysql://nevereatalonedata.ctvdfnzijgid.us-west-2.rds.amazonaws.com:3306/neverEatAloneData";
        	String databaseUser = userName;
        	String databasePassword = password;
        	
        	 Connection myConn = null;
             Statement myStmt = null;
      
                 // 1. Get a connection to database
                 myConn = DriverManager.getConnection(url, databaseUser, databasePassword);
      
                 // 2. Create a statement
                 myStmt = myConn.createStatement();
      
                 // 3. Execute SQL query
                 String sql = "insert into interests " + "(user_id, rest_id)"
                		 + " values ('" + userId + "','" + restId + "')";

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
				
				user = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(new User(id, firstName, lastName, email))));
			}
			
			return user;
			
        });
        
        get("/users/email/:email", (request, response) -> {
            response.type("application/json");
            
            String url = "jdbc:mysql://localhost:3306/users";
        	String databaseUser = "student";
        	String password = "student";
        	
    		 Connection myConn = null;
    		 PreparedStatement preparedStmt = null;
     		ResultSet myRs = null;
            
            myConn = DriverManager.getConnection(url, databaseUser , password);
			
			// 2. Prepare statement
			preparedStmt = myConn.prepareStatement("select * from users where email = ?");
			
			// 3. Set the parameters
			preparedStmt.setString(1, request.params(":email"));
			
			// 4. Execute SQL query
			myRs = preparedStmt.executeQuery();
			String user = null;
			
			while (myRs.next()) {
				String id = myRs.getString("id");
				String lastName = myRs.getString("last_name");
				String firstName = myRs.getString("first_name");
				String email = myRs.getString("email");
				
				user = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(new User(id, firstName, lastName, email))));
			}
			
			return user;
			
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