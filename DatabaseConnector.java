//STEP 1. Import required packages
import java.sql.*;
import java.util.Scanner;

public class DatabaseConnector {
   // JDBC driver name and database URL
   private  String DB_URL = "jdbc:mysql://localhost:3306/moviereservation";

   //  Database credentials
   private String USER = "root";

   //private String PASS = "CHANGEME!";
  
   private String PASS = "test123";
   
   private Connection conn = null;
   private PreparedStatement statement = null;
   private ResultSet resultset = null;
   
   private String currentUID;
   private boolean flag;
   
   
   public DatabaseConnector(){}
   
   
   /**
    * Get the initial connection with the database
    * @return true or false based if connection is null
    */
   public void connectDB()
   {
	  
	   try{
	      //Open a connection
	      //System.out.println("Connecting to database...");
	      conn = DriverManager.getConnection(DB_URL, USER, PASS);
	      //System.out.println("Connected to database");

	   //used to catch errors
	   
	   }catch(SQLException se){
	      System.out.println("Connection failed");
	      se.printStackTrace();
	   }
	   
	   
   }
   /**
    * Registers a new user into the database. This will take all information
    * and store it into the database. 
    * @param uname user's input of username
    * @param fname user's input of firstname
    * @param lname user's input of lastname
    * @param email user's input of email
    * @param password user's input of password
    */
   public void registerUser(String uname, String fname, String lname, 
		   String email, String password)
   {
	   try{
		   //check if username  is already taken
		   statement = this.conn.prepareStatement("SELECT uname FROM reguser WHERE uname = ?");
		   
		   //set the uname to string uname
		   statement.setString(1,uname);
		   
		   //executes the query
		   resultset = statement.executeQuery();
		   
		   //iterates the result of the query
		   if(resultset.next())
		   {
			   System.out.println("The username already exist...please try again");
			  
		   }
		   else
		   {
			   //do statement query to insert the new user
			   statement = this.conn.prepareStatement("INSERT INTO RegUser(uname, fname, lname, email, pw) VALUES(?,?,?,?,?)");
			   
			   statement.setString(1, uname);
			   statement.setString(2, fname);
			   statement.setString(3, lname);
			   statement.setString(4, email);
			   statement.setString(5, password);
			   statement.executeUpdate();
			   System.out.println("New User Registration Success!");
			   
		   }
		     
   
		   }catch(SQLException se){
		      
		      se.printStackTrace();
		   }   
   }
   
   /**
    * Registers a new admin user into the database. This will take all information
    * and store it into the database. 
    * @param uname user's input of username
    * @param fname user's input of firstname
    * @param lname user's input of lastname
    * @param email user's input of email
    * @param password user's input of password
    */
   public void registerAdmin(String uname, String fname, String lname, 
		   String email, String password)
   {
	   try{
		   //check if username  is already taken
		   statement = this.conn.prepareStatement("SELECT uname FROM admin WHERE uname = ?");
		   
		   //set the uname to string uname
		   statement.setString(1,uname);
		   
		   //executes the query
		   resultset = statement.executeQuery();
		   
		   //iterates the result of the query
		   if(resultset.next())
		   {
			   System.out.println("The username already exist...please try again");
			  
		   }
		   else
		   {
			   //do statement query to insert the new user
			   statement = this.conn.prepareStatement("INSERT INTO Admin(uname, fname, lname, email, pw) VALUES(?,?,?,?,?)");
			   
			   statement.setString(1, uname);
			   statement.setString(2, fname);
			   statement.setString(3, lname);
			   statement.setString(4, email);
			   statement.setString(5, password);
			   statement.executeUpdate();
			   System.out.println("New Admin Registration Success!");
			   
			  
		   }
		     
		      
		   
		   }catch(SQLException se){
		      
		      se.printStackTrace();
		   }
	      
		 
   }
   
   /**
    * Checks the user or admin login will be successful or not. 
    * It will check if the user or admin is already stored in the database
    * @param uname user's input of username
    * @param password user's input of password
    */
   public boolean adminlogin(String uname, String password)
   {
	   String adminuname, adminpw;
	   try{
		   
		   statement = this.conn.prepareStatement("SELECT uname, pw FROM moviereservation.admin WHERE uname='" + uname +"' AND pw = '" + password + "';");
		   resultset = statement.executeQuery();
		   
		   while(resultset.next())
		   {
			   adminuname = resultset.getString("uname");
			   adminpw = resultset.getString("pw");
		   
		   if(adminuname.equals(uname) && adminpw.equals(password))
		   {
			   
			   return true;
		   }
		       return false;

		   }
	   }
		catch(SQLException se){
		      
		      se.printStackTrace();
		}
	   return false;
	     
   }
   
   /**
    * Checks the user or user login will be successful or not. 
    * It will check if the user or user is already stored in the database
    * @param uname user's input of username
    * @param password user's input of password
    */
   public boolean userlogin(String uname, String password)
   {
	   try{
		   
		   statement = this.conn.prepareStatement("SELECT uid, uname, pw FROM moviereservation.reguser WHERE uname='" + uname +"' AND pw = '" + password + "';");
		   resultset = statement.executeQuery();
		   
		   if(resultset.next())
		   {
		   String useruid = resultset.getString(1);

		   String useruname = resultset.getString(2);
		   String userpw = resultset.getString(3);
		   
		   
		   if(useruname.equals(uname) && userpw.equals(password))
		   {
			   currentUID = useruid;
			   
			   
			   return true;
		   }
		       return false;

		   }
	   }
		catch(SQLException se){
		      
		      se.printStackTrace();
		}
	   return false;
	     
   }
   
   /**
    * This function allows user to reserve a movie. This will store into a reservation database to retrive information later
    * @param uid The user login id
    * @param mid The movie id
    * @return a boolean based if it was successful or not
    */
   public boolean userReserve(String uid, String mid)
   {
     
     try
     {
       

       //retrieve mid from the given title and showtime
       statement = this.conn.prepareStatement(("SELECT movie.mid FROM movie WHERE movie.mid in(Select movie.mid from showtimes where movie.mid ='" + mid +"');"));
       resultset = statement.executeQuery();
       resultset.next();
       String givenMID = resultset.getString(1);
       
       
       statement = this.conn.prepareStatement("INSERT INTO Reservation(uid, mid) VALUES(?,?)"); 

       //Set parameters to uid and mid
       statement.setString(1,uid);
       statement.setString(2,givenMID);
     
       //execute insert
       statement.execute();
       
       return true;
     }
     catch(SQLException se)
     {
       se.printStackTrace();
     }
     return false;
   }
   
   /**
    * The admin can insert new movies into the database. It will ask for a list of queries to store into Movie and Showtime database
    * @param title the title of the movie
    * @param duration how long the movie is
    * @param showtime what time does the movie start
    * @param date when is the movie
    * @param trid what theater number
    * @return a boolean based on if the insert was successful or not
    */
   public boolean adminInsert(String title, String duration, String showtime, String date, String trid)
	{
	  
	   try
	   {
		   //statement for the insertion to movies
		   statement = this.conn.prepareStatement("INSERT INTO Movie(title, duration) VALUES(?,?)");
	   
		   //set the uname to string uname
		   statement.setString(1,title);
		   statement.setString(2,duration);
	   
		   //executes the query
		   statement.executeUpdate();
		   
		   //statement for the insertion into showtimes
		   statement = this.conn.prepareStatement("INSERT INTO Showtimes(showtime, date, trid) VALUES(?,?,?)");
		   statement.setString(1, showtime);
		   statement.setString(2, date);
		   statement.setString(3, trid);
		   statement.executeUpdate();
		   return true;
	   }
	   catch(SQLException se)
	   {
		   se.printStackTrace();
	   }
	   return false;
	}
   
   /**
    * Displays all the theater rooms. Gets a simple query to determine all the theater rooms. 
    */
   public void displayTheaterRooms(){
	   try
	   {
		   //statement for the insertion to movies, i ordered by date
		   statement = this.conn.prepareStatement("SELECT * from TheaterRoom");
		   resultset = statement.executeQuery();
		   
		   String[] labels = {"Theater Room:", "Max Seats:"};
		   String[] results = new String[2];
		   System.out.format("%20s%20s\n", labels);
		   
		   while(resultset.next())
		   {			   
			   results[0] = resultset.getString("trid");
			   results[1] = resultset.getString("seats");

			   
			   //print reservations in pretty columns
			   
			   
			   System.out.format("%20s%20s\n", results);
			   System.out.println();
	       
		   
		   }
	   
	   }
	   catch(SQLException se)
	   {
		   se.printStackTrace();
	   }
	   
   }
   
   /**
    * Display all archived showtimes.
    */
   
   public void displayArchive()
   {
	   try
	   {
		   //statement for the insertion to movies, i ordered by date
		   statement = this.conn.prepareStatement("SELECT * FROM Archive");
		   resultset = statement.executeQuery();
		   
		   String[] results = new String[6];
		   
		   while(resultset.next())
		   { 
			  
			   results[0] = resultset.getString("mid");
			   results[1] = resultset.getString("trid");
			   results[2] = resultset.getString("showtime");
			   results[3] = resultset.getString("date");
			   results[4] = resultset.getString("seats");
			   results[5] = resultset.getString("updatedAt");
			   
			   //print reservations in pretty columns
			   String[] labels = {"Movie ID", "Theater Room ID", "Showtime", "Date", "Reserved Seats", "Last Updated"};
			   System.out.format("%20s%20s%20s%20s%20s%20s\n\n", labels);
			   System.out.format("%20s%20s%20s%20s%20s%20s\n", results);
			   System.out.println();
	       
		   }
		   
	   
	   }
	   catch(SQLException se)
	   {
		   se.printStackTrace();
	   }
	   
   }
   /**
    * Display all the showtime listing for each movie. Added a flag to determine if there is a result. If there is 
    * no result, then display a simple message that there is no showtime listings.
    */
   public void displayShowtimes()
   {
	   try
	   {
		   //statement for the insertion to movies, i ordered by date
		   statement = this.conn.prepareStatement("SELECT movie.mid, title, showtime, date, trid, seats "
		   		+ "FROM  moviereservation.movie, moviereservation.showtimes "
		   		+ "WHERE moviereservation.movie.mid = moviereservation.showtimes.mid order by mid");
		   resultset = statement.executeQuery();
		   
		   String[] results = new String[6];
		   String[] labels = {"Movie ID", "Movie Title", "Showtime", "Date", "Theater Room", "Reserved Seats"};
		   System.out.format("%20s%20s%20s%20s%20s%20s\n\n", labels);
		   
		   while(resultset.next())
		   { 
			  
			   results[0] = resultset.getString("mid");
			   results[1] = resultset.getString("title");
			   results[2] = resultset.getString("showtime");
			   results[3] = resultset.getString("date");
			   results[4] = resultset.getString("trid");
			   results[5] = resultset.getString("seats");
			   
			   //print reservations in pretty columns
			   
			   System.out.format("%20s%20s%20s%20s%20s%20s\n", results);
			   System.out.println();
	       
			   flag = true;
		   }
		   
	   
	   }
	   catch(SQLException se)
	   {
		   se.printStackTrace();
	   }
	   
   }
   

   /**
    * Displays all the reservation made by only a specific user. If the result set return is false, then 
    * we will display a simple message that there the user has made no reservations.
    * @param uid
    */
   public void displayReservations(String uid)
   {
	   try
	   {
		   //statement for the insertion to movies, i ordered by date
		   statement = this.conn.prepareStatement("SELECT rid, title, showtime, date "
		   		+ "FROM  movie, reservation, showtimes "
		   		+ "WHERE reservation.uid ='"+ uid +"' && reservation.mid = showtimes.mid && showtimes.mid = movie.mid order by date");
		   resultset = statement.executeQuery();
		 
		   
		   String[] results = new String[4];
		  
		   
		   while(resultset.next())
		   {
		   
		   results[0] = resultset.getString("rid");
		   results[1] = resultset.getString("title");
		   results[2] = resultset.getString("showtime");
		   results[3] = resultset.getString("date");
		   
		   //print reservations in pretty columns
		   flag = true;
		   
		   String[] labels = {"Reservation ID", "Movie Title", "Showtime", "Date"};
		   System.out.format("%15s%15s%15s%15s\n", labels);
		   System.out.format("%15s%15s%15s%15s\n", results);

		   
		   }
		   
	   
	   }
	   catch(SQLException se)
	   {
		   se.printStackTrace();
	   }
	   
   }
   
   /**
    * Displays all reservations made by ALL users. We will set a flag and if it is false
    * then we will display a simple message that there are no reservations being made by any user.
    */
   public void displayAllReservation()
   {
	   try
	   {
		   //statement for the insertion to movies, i ordered by date
		   statement = this.conn.prepareStatement("SELECT rid, title, showtime, date "
		   		+ "FROM  movie, reservation, showtimes "
		   		+ "WHERE reservation.mid = movie.mid && reservation.mid = showtimes.mid");
		   
		   resultset = statement.executeQuery();
		 
		  
		   String[] results = new String[4];
		   
		   while(resultset.next())
		   {
		   
		   results[0] = resultset.getString("rid");
		   results[1] = resultset.getString("title");
		   results[2] = resultset.getString("showtime");
		   results[3] = resultset.getString("date");
		   
		   //print reservations in pretty columns
		   
		   String[] labels = {"Reservation ID", "Movie Title", "Showtime", "Date"};
		   System.out.format("%15s%15s%15s%15s\n", labels);
		   System.out.format("%15s%15s%15s%15s\n", results);

		   flag = true;
		   }
		   
	   
	   }
	   catch(SQLException se)
	   {
		   se.printStackTrace();
	   }
	   
   }
   
   /**
    * The delete reservation is only made when the admin is logged on. The admin can delete any reservation
    * made by a user.
    * @param rid the reservation id number
    * @return a boolean if the delete was successful or not
    */
   public boolean deleteReserve(String rid) {
		try
		   {
			   //statement to delete from Movies
			   statement = this.conn.prepareStatement("DELETE FROM reservation WHERE rid = '" + rid + "';" );
			   
			   //executes the statement

			   statement.executeUpdate();

			   return true;
		   }
		   catch(SQLException se)
		   {
			   se.printStackTrace();
		   }
		   return false;
	}

   
   /**
    * The admin delete is when the admin wants to delete a specific movie. 
    * @param mid the movie id number
    * @return a boolean if it was deleted successful or not
    */
public boolean adminDelete(String mid) {
	try
	   {
		   //statement to delete from Movies
		statement = this.conn.prepareStatement("DELETE movie, showtime FROM moviereservation.movie movie JOIN moviereservation.showtimes showtime on showtime.mid =  movie.mid WHERE movie.mid = '" + mid + "';" );

		   //executes the statement
		   statement.executeUpdate();

		   return true;
	   }
	   catch(SQLException se)
	   {
		   se.printStackTrace();
	   }
	   return false;
}

public boolean archiveShowtimes(String date, String time){
	try
	   {
		   //statement to delete from Movies
		   statement = this.conn.prepareStatement("CALL archiveShowtime('" + date + " " + time + "')");
		   //executes the statement
		   statement.executeUpdate();

		   return true;
	   }
	   catch(SQLException se)
	   {
		   se.printStackTrace();
	   }
	   return false;
	
}
/**
 * The admin updates a specific movie. It will ask for the movie id and 
 * updates the following: title, duration, time, date, and theater id.
 * If it is successful, then it will update to the mysql database.
 * @param mid the movie id number
 * @param name the title of the movie
 * @param duration the length of the movie
 * @param time the showtime of the movie
 * @param date the date of the movie
 * @param trid the theater number of the movie
 * @return a boolean whether it was successfully updated
 */
public boolean adminUpdate(String mid, String name, String duration, String time, String date, String trid) {
	try{
		//TODO: Prevent theater room update, seating conflict. Ex. Update trid 2=>1. Max seats then 30=>3. If showtimes.seats > 3, then INCORRECT!
		
		//EDIT THIS BASED ON MID SELECTION
		//THEN UPDATE BASED ON QUERY
		statement = this.conn.prepareStatement(" UPDATE moviereservation.movie, moviereservation.showtimes set title= '" + name + "', duration= '" + duration + "', showtime = '" + time + "', date = '" + date + "', trid = '" + trid + "' where moviereservation.movie.mid = '" + mid + "' AND moviereservation.showtimes.mid = '" + mid + "';");
		statement.execute();
		
		return true;
	}
	catch(SQLException se)
	 {
		 se.printStackTrace();
	 }
	return false;
} 
public boolean queryN(String i){
	try{
		statement = this.conn.prepareStatement("SELECT movie.title, COUNT(Reservation.rid) " +
				"FROM Reservation, Movie "+
				"WHERE reservation.mid = movie.mid "+
				"GROUP BY movie.mid "+
				"HAVING COUNT(Reservation.rid) >" + i);
		statement.execute();
		return true;
	}catch(SQLException se)
	 {
		 se.printStackTrace();
	 }
	return false;
}

public boolean queryIntersect(){
	try{
		statement = this.conn.prepareStatement("SELECT showtimes.mid, Movie.title " +
				"FROM Movie,showtimes INNER JOIN archive "+
				"USING (mid) "+
				"WHERE showtimes.mid = movie.mid ");
		statement.execute();
		return true;
	}catch(SQLException se)
	 {
		 se.printStackTrace();
	 }
	return false;
}

public boolean queryShowtimeMaxSeats(){
	try{
		statement = this.conn.prepareStatement("SELECT showtimes.trid, showtime, date, TheaterRoom.seats " +
				"FROM Showtimes "+
				"LEFT JOIN TheaterRoom "+
				"ON Showtimes.trid = TheaterRoom.trid; ");
		statement.execute();
		return true;
	}catch(SQLException se)
	 {
		 se.printStackTrace();
	 }
	return false;
}
/**
 * Returns the currentuserid
 * @return currentuid of the user
 */
public String getCurrentUID()
{
	return currentUID;
}
/**
 * Returns if the reservation is available or not
 * @return a boolean flag of whether or not it is empty
 */
public boolean getReservation()
{

	return flag;
}


}