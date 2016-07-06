/*
 *
 *  client for TCPClient from Kurose and Ross
 *  Compile : javac TCPClient.java
 *  * Usage: java TCPClient [server addr] [server port]
 *  
 *  Usage : java Reader [mode] [polling_interval] [user_name] [server_name] [server_port_number]
 *  0 : mode : push or pull 
 *  1 : polling_interval(sec) : how often the user queries the server. Only works in pull mode
 *  2 : user_name : identity of user
 *  3 : server_name : hostname
 *  4 : server_port_number : port number 
 */
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The client side of the eBook reader. 
 * The reader maintains the mode of connection, the connected user, the server port & hostname, and the local database of books.
 * @author DAVID CHOON MING, WONG
 * 
 */
public class Reader {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		String mode;
		int pollingInterval;
		String user_name;
		String serverName;
		int serverPort; 
		
		String currBook = "";
		int currPage = 1;
		
		/**
		 * INITIALISATION OF LOCAL DATABASE
		 */
		System.out.println("Initialising local database..");
		ArrayList<Book> local_books = new ArrayList<Book>();

		ArrayList<String> listOfBooks = new ArrayList<String>();
		File file = new File(".");
		String[] listOfFiles = file.list();
		for(String s : listOfFiles){
			if(s.contains("_page")){
				listOfBooks.add(s);				
			}
		}
		local_books = makeBook(listOfBooks);
		for(Book b : local_books){
			System.out.println(b.getName());
		}
		System.out.println("Initalisation complete");
		/**
		 * INITALISATION OF LOCAL DATABASE COMPLETE
		 */
		
		mode = args[0];
		pollingInterval = Integer.parseInt(args[1]);
		user_name = args[2];
		
		// get server address(hostname)
		// hostname has to be relevant to the machine that the server is hosted on. 
		// hostname is not random		
		serverName = args[3];
		InetAddress serverIPAddress = InetAddress.getByName(serverName);
		
		// get server port
		//change above port number if required
		serverPort = Integer.parseInt(args[4]);
		
		System.out.println("The user: \'" + user_name + "\'" + 
							" has successfully logged in to " + serverName + 
								" at port number: " + serverPort);
		System.out.println("The current mode is: " + mode);
		System.out.println("Polling interval is set to be: " + pollingInterval);

		// create socket which connects to server
		Socket clientSocket = new Socket(serverIPAddress, serverPort);
		
		// get input from keyboard
		//sentence can only be : display, post_to_forum & read_post
		
		String sentence;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));		
		while((sentence = inFromUser.readLine())!=null){
			sentence = user_name + " " + sentence;
			String[] s = sentence.split(" ");
			if(mode.equals("pull")){
				if(s[1].equals("display")){
					/**
					 *  display [book_name] [page_number]
					 *  1. Query the server for new posts associated with the PAGE SPECIFIED
					 *  2. In the first column, 
					 *  	n = new post in that line number 
					 *  	m = existing posts in that line number 
					 *  	nothing = no posts in that line number
					 *  
					 *  The reader needs to maintain a database of posts it has.
					 */
					currBook = s[2];
					currPage = Integer.parseInt(s[3]);		
					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
					outToServer.writeBytes(sentence + "\n");
					
					ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
					Page copyPage = (Page) inFromServer.readObject();
					
					//Retrieves the story in the page requested while checking if 
					//	there are new discussion posts attached to the specified line number
					// n = There are new posts
					// m = There are existing, old posts
					for(int j=0; j<copyPage.getPost().size(); j++){	
						Book b = getBook(local_books, currBook);
						int localSize = b.getPage(currPage).getPost().get(j).size();
						int intlSize = copyPage.getPost().get(j).size();
						if(localSize < intlSize){ //Implies there are new discussion posts
							String p = copyPage.getPost().get(j).get(0).getMessage();
							System.out.println("n  " + p);
						}else if(localSize == intlSize){
							if(b.getPage(currPage).getPost().get(j).size() > 1){ //Implies there are discussion posts but have been read
								String p = copyPage.getPost().get(j).get(0).getMessage();
								System.out.println("m  " + p);
							}else{ //No discussion posts
								String p = copyPage.getPost().get(j).get(0).getMessage();
								System.out.println("   " + p);			
							}
						}
					}
				} else if(s[1].equals("post_to_forum")){ //post_to_forum [line_number] [content_of_post]
					/**
					 * 1. Send a forum post associated with the line number 
					 * 2. All words after [line_numer] should be regarded as content post
					 * 3. Server should keep track of book name, page number, and username too
					 * 4. Server should allocate a serial number to each post
					 */
					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
					outToServer.writeBytes(sentence + "\n");
					
				} else if(s[1].equals("read_post")){ // read_post [line_number]
					/**
					 * 1. Display all unread posts of the current page on the specified line number 
					 */
					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
					outToServer.writeBytes(sentence + "\n");
					
					ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
					
					ArrayList<Post> copy = new ArrayList<Post>();
					copy = (ArrayList<Post>) inFromServer.readObject();
					
					int lineNumber = Integer.parseInt(s[2]);
					Book b = getBook(local_books, currBook);
					
					//Checks if there are unread new posts in the specified line number 
					//	by comparing the number of the discussion posts in both the local and server's database.
					int localSize = b.getPage(currPage).getPost().get(lineNumber-1).size()-1;
					int intlSize = copy.size();
					System.out.println("Book by " + currBook + ", " 
											+ "Page " + currPage + ", "
												+ "Line Number " + lineNumber + ": ");
					if(localSize < intlSize){
						//Indicates there are new posts
						int numPostToPrint = intlSize - localSize;
						for(int k=intlSize-numPostToPrint; k<intlSize; k++){
							Post p = copy.get(k);
							int count = 0;
							//Looking for the requested book
							for(Book local_b : local_books){
								if(local_b.getName().equals(currBook)){
									count = local_books.indexOf(local_b);
								}
							}
							//Adding the new posts that have been read into the local database
							local_books.get(count).getPage(currPage).getPost().get(lineNumber-1).add(p);
							String m = copy.get(k).getMessage();
							String sn = copy.get(k).getSerialNumber();
							System.out.println(sn + " " + m);
						}
					}
				}
			} else if(mode.equals("push")){
				System.out.println("This mode has not been implemented yet");
			} 
		// close client socket
		//clientSocket.close();

		} // end of outer while loop
	}//end of main
	/**
	 * 
	 * @param books
	 * @param bookName
	 * @return
	 */
	public static Book getBook(ArrayList<Book> books, String bookName){
		for(Book b : books){
			if(b.getName().equals(bookName)){
				return b;
			}
		}
		return null;
	}
	
	/**
	 * This function retrieves the specified book from the local database 
	 * @throws FileNotFoundException
	 * @param listOfBooks The local book database
	 * @return The book requested by the reader
	 * @throws FileNotFoundException
	 */
	public static ArrayList<Book> makeBook(ArrayList<String> listOfBooks) throws FileNotFoundException{
		ArrayList<Book> bookList = new ArrayList<Book>();
		//Retrieving the name of the book
		for(String s : listOfBooks){
			if(s.contains("_page1")){
				String temp[] = s.split("_");
				Book newBook = new Book(temp[0]);
				bookList.add(newBook);
			}
		}
		//Creating the pages of the book
		for(Book b: bookList){
			String bname = b.getName();
			int i = 1;
			while(i < 5){
				b.createPage(bname+"_page", i);
				i++;
			}	
		}
		return bookList;
	}
}// end of class TCPClient