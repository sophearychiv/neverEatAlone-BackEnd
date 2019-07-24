package com.github.sophearychiv.nevereatalone;


import static spark.Spark.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import com.github.sophearychiv.nevereatalone.Interest;
import com.github.sophearychiv.nevereatalone.StandardResponse;
import com.github.sophearychiv.nevereatalone.StatusResponse;
import com.github.sophearychiv.nevereatalone.User;
import com.github.sophearychiv.nevereatalone.UserService;
import com.github.sophearychiv.nevereatalone.UserServiceMapImpl;

public class Main {
    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws SQLException {
    	System.out.println("in main");
    	
    	port(getHerokuAssignedPort());
    	
    	final UserService userService = new UserServiceMapImpl();

        post("/users", (request, response) -> {
            response.type("application/json");

            User user = new Gson().fromJson(request.body(), User.class);
            userService.addUser(user);
            
            JsonParser parser = new JsonParser();
            Connection myConn = connecToDatabase();
             Statement myStmt = null;
      
                 // 2. Create a statement
                 myStmt = myConn.createStatement();
                 
                 JsonObject newUser = parser.parse(request.body()).getAsJsonObject();
                 String fbId = newUser.get("fbId").getAsString();
                 String firstName = newUser.get("firstName").getAsString();
                 String lastName = newUser.get("lastName").getAsString();
                 String email = newUser.get("email").getAsString();
                 String photoUrl = newUser.get("photoUrl").getAsString();
      
                 // 3. Execute SQL query
                 String sql = "insert into users " + " (user_fb_id, first_name, last_name, email, image_url)"
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

            
            Connection myConn = connecToDatabase();
             Statement myStmt = null;

      
                 // 2. Create a statement
                 myStmt = myConn.createStatement();
      
                 // 3. Execute SQL query
                 String sql = "insert into interests " + "(user_fb_id, rest_yelp_id)"
                		 + " values ('" + userFbId + "','" + restYelpId + "')";

                 myStmt.executeUpdate(sql);
      
                 System.out.println("Insert complete.");


            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            
        });
        
        post("/invites", (request, response) -> {
            response.type("application/json");

            JsonParser parser = new JsonParser();
            JsonObject invite = parser.parse(request.body()).getAsJsonObject();
            String requesterFbId = invite.get("requesterFbId").getAsString();
            String restYelpId = invite.get("restYelpId").getAsString();
            String creationDateTimeString = invite.get("creationDateTime").getAsString();
            String mealStartDateTimeString = invite.get("mealStartDateTime").getAsString();
            String mealEndDateTimeString = invite.get("mealEndDateTime").getAsString();
            
//            SimpleDateFormat dateFormatter=new SimpleDateFormat("EEE MMM yyyy, HH:mm:ss"); 
            SimpleDateFormat dateFormatter=new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss a"); 
            Date javaCreationDateTime = dateFormatter.parse(creationDateTimeString);
            java.sql.Timestamp creationDateTime = new java.sql.Timestamp(javaCreationDateTime.getTime());
//            LocalDateTime creationDateTime = LocalDateTime.parse(creationDateTimeString, DateTimeFormatter.ofPattern("MM/dd/yyyy, HH:mm:ss"));
            Date javaMealStartDateTime = dateFormatter.parse(mealStartDateTimeString);
            java.sql.Timestamp mealStartDateTime = new java.sql.Timestamp(javaMealStartDateTime.getTime());
            Date javaMealEndDateTime = dateFormatter.parse(mealEndDateTimeString);
            java.sql.Timestamp mealEndDateTime = new java.sql.Timestamp(javaMealEndDateTime.getTime());
//            JsonElement receipients = invite.get("receipients");
            JsonElement receipientFbIds = invite.get("receipientFbIds").getAsJsonArray();
            
//            System.out.println(requesterFbId);
//            System.out.println(restYelpId);
//            System.out.println(creationDateTime);
//            System.out.println(mealStartDateTime);
//            System.out.println(mealEndDateTime);
//            
//            System.out.println(receipients);
            System.out.println("type of receipients :" + receipientFbIds.getClass().getSimpleName());
            

            
            Connection myConn = connecToDatabase();
             Statement myStmt = null;
             ResultSet myRs = null;

      
                 // 2. Create a statement
                 myStmt = myConn.createStatement();
      
                 // 3. Execute SQL query
                 String sql = "insert into invites " + "(requester_fb_id, rest_yelp_id, creation_date, meal_start_date, meal_end_date)"
                		 + " values ('" + requesterFbId + "','" + restYelpId + "','" + creationDateTime + "','" + mealStartDateTime + "','" + mealEndDateTime + "')";

                 myStmt.executeUpdate(sql);
                 
                 PreparedStatement preparedStmt = null;
                 
                 preparedStmt = myConn.prepareStatement("select id from invites where requester_fb_id = ? and rest_yelp_id = ? and creation_date = ?");
                 preparedStmt.setString(1, requesterFbId);
                 preparedStmt.setString(2, restYelpId);
                 preparedStmt.setObject(3, creationDateTime);
                 
                 myRs = preparedStmt.executeQuery();
                 
                 if(myRs.next()) {
                	 String id = myRs.getString("id");
                	 ((Iterable<JsonElement>) receipientFbIds).forEach((fbId) -> {
                     	System.out.println(fbId.getAsString().replace("\"", ""));
                     	 String anotherSql = "insert into invites_users " + "(invite_id, receipient_fb_id)"
                        		 + " values ('" + id + "','" + fbId.getAsString().replace("\"", "") + "')";
                     	 
                     	 try {
                     		 Statement anotherStmt = myConn.createStatement();
                     		
							anotherStmt.executeUpdate(anotherSql);

			                System.out.println("Insert complete.");
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                     });
                	
                 }

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            
        });


        get("/users/:fbId", (request, response) -> {
            response.type("application/json");
            
            Connection myConn = connecToDatabase();
 	   		PreparedStatement preparedStmt = null;
     		ResultSet myRs = null;
     		String user = null;

			
			// 2. Prepare statement
			preparedStmt = myConn.prepareStatement("select * from neverEatAlone.users where user_fb_id = ?");
			
			// 3. Set the parameters
			preparedStmt.setString(1, request.params(":fbId"));
			
			System.out.println("user fbId in get user request is " + request.params(":fbId"));
			
			// 4. Execute SQL query
			myRs = preparedStmt.executeQuery();
			
			System.out.println(myRs);
			
			
			
			while (myRs.next()) {
				String id = myRs.getString("id");
				String fbId = myRs.getString("user_fb_id");
				String lastName = myRs.getString("last_name");
				String firstName = myRs.getString("first_name");
				String email = myRs.getString("email");
				String photoUrl = myRs.getString("image_url");
				
				
				user = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, 
						new Gson().toJsonTree(new User(id, fbId, firstName, lastName, email, photoUrl))));
			}
			
			System.out.println("returned user from get request: " + user);

			
			return user;
			
        });
        
        get("/users/:fbId/interests/:restYelpId", (request, response) -> {
        	
    	   response.type("application/json");
           
    	   Connection myConn = connecToDatabase();
	   		PreparedStatement preparedStmt = null;
			ResultSet myRs = null;
			String interest = null;
   			
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

   				
   				interest = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(new Interest(id, userFbId, restYelpId))));
   				
  
   			}
 			
   			return interest;
        	
        });
        
        get("/users/:fbId/interests", (request, response) -> {
        	
     	   response.type("application/json");

     	  Connection myConn = connecToDatabase();
	   		
 	   		PreparedStatement preparedStmt = null;
 			ResultSet myRs = null;
 			String interest = null;

    			
    			// 2. Prepare statement
    			preparedStmt = myConn.prepareStatement("select * from neverEatAlone.interests where user_fb_id = ?");
    			
    			// 3. Set the parameters
    			preparedStmt.setString(1, request.params(":fbId"));
    			
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

    				
    				interest = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(interests)));
    				
   
    			}
    			
    			return interest;
         	
         });
        
        get("/interests/:restYelpId/userFbIds", (request, response) -> {
        	
      	   response.type("application/json");

      	  Connection myConn = connecToDatabase();
 	   		
  	   		PreparedStatement preparedStmt = null;
  			ResultSet myRs = null;
  			String interest = null;

     			
     			// 2. Prepare statement
     			preparedStmt = myConn.prepareStatement("select * from interests where rest_yelp_id = ?");
     			
     			// 3. Set the parameters
     			preparedStmt.setString(1, request.params(":restYelpId"));
     			
     			// 4. Execute SQL query
     			myRs = preparedStmt.executeQuery();
     			
     			System.out.println(myRs);
     			
     			Collection<String> userFbIds = new ArrayList<String>();
     			
     			
     			while (myRs.next()) {
     				String id = myRs.getString("id");
     				String userFbId = myRs.getString("user_fb_id");
     				String restYelpId = myRs.getString("rest_yelp_id");
     				
     				userFbIds.add(userFbId);
     				
     				System.out.println(id + " " + userFbId + " " + restYelpId);

     				
     				interest = new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userFbIds)));
     				
    
     			}
     			
     			return interest;
          	
          });
        
        delete("/users/:fbId/interests/:restYelpId", (request, response) -> {
        	response.type("application/json");
       
 	       	
 	   		Connection myConn = connecToDatabase();
 	   		PreparedStatement preparedStmt = null;
    			
    		// 2. Prepare statement
    		preparedStmt = myConn.prepareStatement("delete from interests where user_fb_id = ? and rest_yelp_id = ? ");
    			
    		// 3. Set the parameters
    		preparedStmt.setString(1, request.params(":fbId"));
    		preparedStmt.setString(2, request.params(":restYelpId"));
    			
    			
    		// 4. Execute SQL query
    		preparedStmt.executeUpdate();
    			
    		System.out.println("deleted");
    			
    			
        	return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "interest deleted"));
        	
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
    
    public static Connection connecToDatabase() throws JsonIOException, JsonSyntaxException, FileNotFoundException, SQLException {
    	JsonParser parser = new JsonParser();

        
        JsonObject aws = parser.parse(new FileReader(".secrets.txt")).getAsJsonObject();
        String aws_database_url = aws.get("aws_database_url").getAsString();
        String aws_userName = aws.get("aws_userName").getAsString();
        String aws_password = aws.get("aws_password").getAsString();
		String local_database_url = aws.get("local_database_url").getAsString();
        String local_userName = aws.get("local_userName").getAsString();
        String local_password = aws.get("local_password").getAsString();
        
		String aws_url = "jdbc:mysql://" + aws_database_url;
		String aws_databaseUser = aws_userName;
		String aws_databasePassword = aws_password;
		String local_url = "jdbc:mysql://localhost:3306/neverEatAlone?useSSL=false";
		String local_databaseUser = local_userName;
		String local_databasePassword = local_password;
		
//		System.out.println(local_databaseUser);
//		System.out.println(local_databasePassword);
		
		 return DriverManager.getConnection(aws_url, aws_databaseUser , aws_databasePassword);
//		return DriverManager.getConnection(local_url, local_databaseUser , local_databasePassword);
    }
    
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }


}