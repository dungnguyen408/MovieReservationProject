import java.util.ArrayList;
import java.util.Scanner;

public class MovieReservationTester
{
	public static void main(String [] args)
	{
		//import scanner
		Scanner sc = new Scanner(System.in);
		
		//this flag is used to loop the input
		boolean flag = true;
	
		while(flag)
		{
			System.out.println("Would you like to [S]ign Up, [L]ogin or [Q]uit?");
			String signuporlogin = sc.nextLine().toUpperCase();
			
			if(signuporlogin.equals("S"))
			{
				signup();
			}
			else if(signuporlogin.equals("L"))
			{
				login();
				break;
				
			}
			else if(signuporlogin.equals("Q"))
			{
				System.out.println("Thank you...Have a nice day.");
				flag = false;				
			}
			else
			{
				System.out.println("Wrong input...try again");
				
			}		
			}
			
	}
	
	/**
	 * This functions does the conditions for signup. It will ask the user if he/she is a user or an admin.
	 * Afterwards, the user must create a username, first name, last name, email, and password.
	 * If the username is already taken, it will throw an error. If it is successful, the loop will iterate again
	 * and the user can then login with the new information. 
	 */
	public static void signup()
	{
		Scanner sc = new Scanner(System.in);
		DatabaseConnector db = new DatabaseConnector();
		db.connectDB();
		
		System.out.println("Did you want to make a [U]ser or [A]dmin?");
		String signupchoice = sc.nextLine().toUpperCase();
	
		System.out.println("Username: ");
		String uname = sc.nextLine();
	
		System.out.println("First Name: ");
		String fname = sc.nextLine();
	
		System.out.println("Last Name: ");
		String lname = sc.nextLine();
	
		System.out.println("Email: ");
			String email = sc.nextLine();
	
		System.out.println("Password: ");
		String password = sc.nextLine();
	
		if(signupchoice.equals("U"))
		{
			db.registerUser(uname,fname,lname,email,password);
			
		}
		else if(signupchoice.equals("A"))
		{
			db.registerAdmin(uname, fname, lname, email, password);
			
		}
		else
		{
			System.out.println("Unvalid input.");
			
		}
	}
	
	/**
	 * The login is the initial condition to check the username and password. If the username and or password is 
	 * incorrect, it will throw an error message. Otherwise, the user has login successfully. 
	 * It will also determine if the user is a regular user or an admin. 
	 */
	public static void login()
	{
		Scanner sc = new Scanner(System.in);
		DatabaseConnector db = new DatabaseConnector();
		db.connectDB();
		
		
		System.out.println("Please enter username: ");
		String username = sc.nextLine();
		
		System.out.println("Please enter password: ");
		String password = sc.nextLine();
		
		if(db.adminlogin(username, password) == true)
		{
			System.out.println("Admin login successful");
			
			//create the admin page here
			adminPage();
			
		}
		else if(db.userlogin(username,password) == true)
		{
			System.out.println("User login successful!");
			
			//create the user page here
			userPage(db.getCurrentUID());
		}
		else
		{
			System.out.println("Login unsuccessful");
			
		}
	}
	/**
	 * The userpage function is the userpage after the login. This will have functions
	 * based on whether the user wants to reserve or update etc. 
	 */
	public static void userPage(String uid)
	{
		
		Scanner sc = new Scanner(System.in);
		boolean flag = true;
		DatabaseConnector db = new DatabaseConnector();
		db.connectDB();
		
		while(flag)
		{
			System.out.println("Would you like to [R]eserve, [D]elete, [C]heck showtimes,[U]ser Reservation or [Q]uit?");
			String s = sc.nextLine().toUpperCase();
			if(s.equals("R"))
			{
				db.displayShowtimes();
			
				if(db.getReservation() == false)
				{
					System.out.println("There are no movies at this time...");
					System.out.println();
				}
				else
				{
					System.out.println("Select movie id to make reservation: ");
					String mid = sc.nextLine();
					if(db.userReserve(uid, mid) == true)
					{
						System.out.println("Reservation complete");
					}
					else
					{
						System.out.println("Could not reserve correctly...");
					}
				
				
				}
			}
			else if(s.equals("D"))
			{
				
				System.out.println("Which movie reservation would you like to remove?");
				
				
				db.displayReservations(uid);
				if(db.getReservation() == false)
				{
					System.out.println("You have no reservations at this time...");
					System.out.println();
				}
				else
				{
					System.out.println("Enter the reservation id: ");
					String rid = sc.nextLine();
				
					if(db.deleteReserve(rid) == true)
					{
						System.out.println("Reservation is deleted");
					}
					else{
						System.out.println("Could not delete the reservation correctly...");
					}
				}
			}
			else if(s.equals("C"))
			{
				//check ALL showtimes
				if(db.getReservation() == false)
				{
					System.out.println("There are no showtimes at this time...");
					System.out.println();
				}
				else
				{
					db.displayShowtimes();
				}
			}
			else if(s.equals("U"))
			{
				if(db.getReservation() == false)
				{
					System.out.println("You have no reservations at this time...");
					System.out.println();
				}
				else
				{
				db.displayReservations(uid);
				}
			}
			else if(s.equals("Q"))
			{
				flag = false;
				System.out.println("Thank you... Have a good day");
			}
			else
			{
				System.out.println("Wrong input...please try again");
			}
		}
	}
	
	/**
	 * The admin page function is the admin page after the login. This will have functions
	 * based on whether the user wants to reserve or update etc. 
	 */
	public static void adminPage()
	{
		Scanner sc = new Scanner(System.in);
		boolean flag = true;
		DatabaseConnector db = new DatabaseConnector();
		db.connectDB();
		
		while(flag)
		{
			System.out.println("Would you like to [I]nsert, [U]pdate, [D]elete, [C]heck showtimes, [A]rchive, [M]isc Query or [Q]uit?");
			String s = sc.nextLine().toUpperCase();
			if(s.equals("I"))
			{
			
				System.out.println("Name of movie title: ");
				String title = sc.nextLine();
				
				System.out.println("How long is this movie: (FORMAT: 60 = 1 hr, 90 = 1hr 30mins)");
				String duration = sc.nextLine();
				
				System.out.println("When is the show time for this movie: (FORMAT: 12:00:00 = 12pm, 22:30:00 = 10:30pm)");
				String showtime = sc.nextLine();
				
				System.out.println("What is the date for this movie: (FORMAT: yyyy-mm-dd)");
				String date = sc.nextLine();
				
				System.out.println("What is the theater room for this movie?");
				db.displayTheaterRooms();
				String trid = sc.nextLine();
				
				
				
				//returns a boolean if inserted correctly
				if(db.adminInsert(title, duration, showtime, date, trid) == true)
				{
					System.out.println("Movie insertion complete");
				}
				else
				{
					System.out.println("Could not insert movie correctly...");
				}
			}
			else if(s.equals("U"))
			{
				
				db.displayShowtimes();
				if(db.getReservation() == false)
				{
					System.out.println("There are no movies to update..");
					System.out.println();
				}
				else
				{
					System.out.println("Please select Movie ID to update: ");
					String mid = sc.nextLine();
			
					System.out.println("Name of movie title: ");
					String name = sc.nextLine();
				
					System.out.println("Duration/how long is this movie: (FORMAT: 60 = 1 hr, 90 = 1hr 30mins)");
					String duration = sc.nextLine();
				
					System.out.println("When is the show time for this movie: (FORMAT: 12:00:00 = 12pm, 22:30:00 = 10:30pm)");
					String time = sc.nextLine();
				
					System.out.println("What is the date for this movie: (FORMAT: yyyy-mm-dd)");
					String date = sc.nextLine();
				
					System.out.println("What is the theater room for this movie?");
					db.displayTheaterRooms();
					String trid = sc.nextLine();
				
					//returns a boolean if updated correctly
					if(db.adminUpdate(mid, name, duration, time, date, trid) == true){
						System.out.println("Movie update complete");
					}
					else{
						System.out.println("Could not update movie properly...");
					}
				
				}
			}
			else if(s.equals("D"))
			{
				System.out.println("Are you Deleting [R]eservation or [M]ovie?");
				String r = sc.nextLine().toUpperCase();
				
				if(r.equals("R")){
					
					//delete reservation from reservation table- > based on mid and userid
					System.out.println("Which movie reservation would you like to remove?");
					
					//show all movie reservation of user..display title, date, and showtime
					db.displayAllReservation();
					
					if(db.getReservation() == false)
					{
						System.out.println("No users have made a reservation...");
					}
					else
					{
						
						System.out.println("Enter the reservation id: ");
						String rid = sc.nextLine();
					
						if(db.deleteReserve(rid) == true)
						{
							System.out.println("Reservation is deleted");
						}
						else
						{
							System.out.println("Could not delete the reservation correctly...");
						}
					}
					
				}
				else if(r.equals("M")){
					
					//delete a movie
					/**
					 * CHANGE THIS TO NOT TITLE BUT MID...so no complication
					 */
					db.displayShowtimes();
					if(db.getReservation() == false)
					{
						System.out.println("There are no movies at this time..");
						System.out.println();
					}
					else
					{
						System.out.println("Which Movie ID you wish to delete: ");
						String mid = sc.nextLine();
					
						if(db.adminDelete(mid) == true)
						{
							System.out.println("Movie is deleted");
						}
						else{
							System.out.println("Could not delete movie correctly...");
						}
					
					}	
				}
				else
				{
					System.out.println("Wrong input...please try again");
				}
				
			}
			else if(s.equals("C"))
			{
				//check ALL showtimes
				if(db.getReservation() == false)
				{
					System.out.println("There are no showings at this time...");
					System.out.println();
				}
				db.displayShowtimes();
				
			}
			else if(s.equals("A")){
				System.out.println("Would you like to [C]heck Archives or [A]rchive Showtimes?");
				s = sc.nextLine().toUpperCase();
				if(s.equals("C")){
					db.displayArchive();
				}
				else if(s.equals("A")){
					System.out.println("What is the cutoff date? (FORMAT: yyyy-mm-dd)");
					String date = sc.nextLine();
					System.out.println("What is the cutoff time? (FORMAT: 12:00:00 = 12pm, 22:30:00 = 10:30pm)");
					String time = sc.nextLine();
					
					if(db.archiveShowtimes(date, time)){
						System.out.println("Showtimes updated before the cutoff have been archived.");
					}
					else{
						System.out.println("Showtimes unabled to be archived with the given cutoff.");
					}
				}
			}
			else if(s.equalsIgnoreCase("M")){
				System.out.println("MISCELLANEOUS QUERIES:");
				System.out.println("[1] Query Movies With At Least One Reservation");
				System.out.println("[2] Query Showtimes and Maximum Seats from Theater Rooms");
				System.out.println("[3] Query Movies Present in Both Showtimes and Archive");
				System.out.println("Choose a query:");
				int x = sc.nextInt();
				if(x == 1){
					db.queryN("1");
				}
				else if(x == 2){
					db.queryShowtimeMaxSeats();
				}
				else if(x==3){
					db.queryIntersect();
				}
			}
			else if(s.equals("Q"))
			{
				flag = false;
				System.out.println("Thank you. Have a good day");
			}
			else
			{
				System.out.println("Wrong input...please try again");
			}
		}
	}
}
