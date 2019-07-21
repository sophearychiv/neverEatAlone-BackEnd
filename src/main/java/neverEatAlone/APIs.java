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
import java.util.ArrayList;
import java.util.Collection;
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
            
            JsonParser parser = new JsonParser();
            JsonObject aws = parser.parse(new FileReader(".secrets.txt")).getAsJsonObject();
            String userName = aws.get("aws_userName").getAsString();
            String password = aws.get("aws_password").getAsString();
            
            String url = "jdbc:mysql://database-2.ctvdfnzijgid.us-west-2.rds.amazonaws.com:3306/neverEatAlone";
        	String databaseUser = userName;
        	String databasePassword = password;
        	
        	 Connection myConn = null;
             Statement myStmt = null;
      
                 // 1. Get a connection to database
                 myConn = DriverManager.getConnection(url, databaseUser, databasePassword);
      
                 // 2. Create a statement
                 myStmt = myConn.createStatement();
                 
                 JsonObject newUser = parser.parse(request.body()).getAsJsonObject();
                 String fbId = newUser.get("fbId").getAsString();
                 String firstName = newUser.get("firstName").getAsString();
                 String lastName = newUser.get("lastName").getAsString();
                 String email = newUser.get("email").getAsString();
                 String photoUrl = newUser.get("photoUrl").getAsString();
      
                 // 3. Execute SQL query
                 String sql = "insert into users " + " (fb_id, first_name, last_name, email, photo_url)"
                		 + " values ('" + fbId + "','" + firstName + "','" + lastName + "','" + email + "','" + photoUrl + "')";
//                		 + " values ('" + user.getFbId() + "','" + user.getFirstName() + "','" + user.getLastName() + "','" + user.getEmail() + "','" + user.getPhotoUrl() + "')";

                 myStmt.executeUpdate(sql);
      
                 System.out.println("Insert complete.");


            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            
        });
        
        post("/interests", (request, response) -> {
            response.type("application/json");

            JsonParser parser = new JsonParser();
            JsonObject interested = parser.parse(request.body()).getAsJsonObject();
            String userFbId = interested.get("userFbId").getAsString();
            String restYelpId = interested.get("restYelpId").getAsString();

            
            JsonObject aws = parser.parse(new FileReader(".secrets.txt")).getAsJsonObject();
            String userName = aws.get("aws_userName").getAsString();
            String password = aws.get("aws_password").getAsString();
            
            String url = "jdbc:mysql://database-2.ctvdfnzijgid.us-west-2.rds.amazonaws.com:3306/neverEatAlone";
        	String databaseUser = userName;
        	String databasePassword = password;
        	
        	 Connection myConn = null;
             Statement myStmt = null;
      
                 // 1. Get a connection to database
                 myConn = DriverManager.getConnection(url, databaseUser, databasePassword);
      
                 // 2. Create a statement
                 myStmt = myConn.createStatement();
      
                 // 3. Execute SQL query
                 String sql = "insert into interests " + "(user_fb_id, rest_yelp_id)"
                		 + " values ('" + userFbId + "','" + restYelpId + "')";

                 myStmt.executeUpdate(sql);
      
                 System.out.println("Insert complete.");


            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            
        });


        get("/users/:fbId", (request, response) -> {
            response.type("application/json");
            
            JsonParser parser = new JsonParser();

            
            JsonObject aws = parser.parse(new FileReader(".secrets.txt")).getAsJsonObject();
            String userName = aws.get("aws_userName").getAsString();
            String password = aws.get("aws_password").getAsString();
            
            String url = "jdbc:mysql://database-2.ctvdfnzijgid.us-west-2.rds.amazonaws.com:3306/neverEatAlone";
        	String databaseUser = userName;
        	String databasePassword = password;
        	
    		Connection myConn = null;
    		PreparedStatement preparedStmt = null;
     		ResultSet myRs = null;
     		String user = null;
            
            myConn = DriverManager.getConnection(url, databaseUser , databasePassword);
			
			// 2. Prepare statement
			preparedStmt = myConn.prepareStatement("select * from neverEatAlone.users where fb_id = ?");
			
			// 3. Set the parameters
//			preparedStmt.setDouble(1, Integer.parseInt(request.params(":id")));
			preparedStmt.setString(1, request.params(":fbId"));
			
			System.out.println("user fbId in get user request is " + request.params(":fbId"));
			
			// 4. Execute SQL query
			myRs = preparedStmt.executeQuery();
			
			System.out.println(myRs);
			
			
			
			while (myRs.next()) {
				String id = myRs.getString("id");
				String fbId = myRs.getString("fb_id");
				String lastName = myRs.getString("last_name");
				String firstName = myRs.getString("first_name");
				String email = myRs.getString("email");
				String photoUrl = myRs.getString("photo_url");
				
				user = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, 
						new Gson().toJsonTree(new User(id, fbId, firstName, lastName, email, photoUrl))));
			}
			
			System.out.println("returned user from get request: " + user);
			
//			System.out.println(myRs.next());
			
			return user;
			
        });
        
        get("/users/:fbId/interests/:restYelpId", (request, response) -> {
        	
    	   response.type("application/json");
           
           JsonParser parser = new JsonParser();

               
           JsonObject aws = parser.parse(new FileReader(".secrets.txt")).getAsJsonObject();
           String userName = aws.get("aws_userName").getAsString();
           String password = aws.get("aws_password").getAsString();
           
           	String url = "jdbc:mysql://database-2.ctvdfnzijgid.us-west-2.rds.amazonaws.com:3306/neverEatAlone";
	       	String databaseUser = userName;
	       	String databasePassword = password;
	       	
	   		Connection myConn = null;
	   		PreparedStatement preparedStmt = null;
			ResultSet myRs = null;
			String interest = null;
           
           myConn = DriverManager.getConnection(url, databaseUser , databasePassword);
   			
   			// 2. Prepare statement
   			preparedStmt = myConn.prepareStatement("select * from neverEatAlone.interests where user_fb_id = ? and rest_yelp_id = ?");
   			
   			// 3. Set the parameters
   			preparedStmt.setString(1, request.params(":fbId"));
   			preparedStmt.setString(2, request.params(":restYelpId"));
   			
   			
   			// 4. Execute SQL query
   			myRs = preparedStmt.executeQuery();
   			
   			System.out.println(myRs);
   			
   			Collection<String> interests = new ArrayList<String>();
   			
   			
   			while (myRs.next()) {
   				String id = myRs.getString("id");
   				String userFbId = myRs.getString("user_fb_id");
   				String restYelpId = myRs.getString("rest_yelp_id");
   				
   				interests.add(restYelpId);
   				
   				System.out.println(id + " " + userFbId + " " + restYelpId);
   				
//   				interests = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(interests)));
   				
   				interest = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(new Interest(id, userFbId, restYelpId))));
   				
  
   			}
//   			
//   			System.out.println("returned rest from get request: " + rest);
//   			
////   			System.out.println(myRs.next());
//   			
   			return interest;
        	
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