/*
 *
 * tcpServer from Kurose and Ross
 * Compile : javac TCPServer.java
 * Usage: java Server [server port]
 */

import java.io.*;
import java.net.*;
import java.util.*;
/**
 * This class implements the Server side of the eBook reader.
 * The server maintains the server port, all incoming connections, the current book & page requested by user, 
 * 	as well as a database of the books,
 * When an incoming connection arrives, the server creates a new thread for the user allowing multiple concurrent connections.
 * @author DAVID CHOON MING, WONG
 *
 */
public class Server {
	private static ArrayList<Book> books = new ArrayList<Book>();
	public static void main(String[] args)throws Exception {
		int serverPort;
		/* change above port number this if required */		
		if(args.length >= 1){
			serverPort = Integer.parseInt(args[0]);
		}else{
			serverPort = 6789;
		}
		System.out.println("The server is listening on post number " + serverPort);
		
		/**
		 * INITIALISATION OF SERVER DATABASE
		 */
		System.out.println("Initialising server database..");
		//ArrayList<Book> books = new ArrayList<Book>();

		ArrayList<String> listOfBooks = new ArrayList<String>();
		File file = new File(".");
		String[] listOfFiles = file.list();
		for(String s : listOfFiles){
			if(s.contains("_page")){
				listOfBooks.add(s);				
			}
		}
		books = makeBook(listOfBooks);
		for(Book b : books){
			System.out.println(b.getName());
		}
		System.out.println("Initalisation complete");
		/**
		 * INITIALISATION OF SERVER DATABASE COMPLETE
		 */
		
		// create server socket
		ServerSocket welcomeSocket = new ServerSocket(serverPort);

		while (true){
		    // accept connection from connection queue
		    Socket connectionSocket = welcomeSocket.accept();
		    ServerThread st = new ServerThread(connectionSocket);
		    st.start();
		    System.out.println("connection from " + connectionSocket);
		
		} // end of while (true)
		
	}// end of main()
	
	/**
	 * This function retrieves the specified book from the server's database 
	 * @param books The server's book database
	 * @param bookName 
	 * @return The book requested by the server
	 */
	public static Book getBook(ArrayList<Book> books, String bookName){
		for(Book b : books){
			if(b.getName().equals(bookName)){
				return b;
			}
		}
		return null;
	}
	
	public static ArrayList<Book> getListOfBooks(){
		return books;
	}
	
	/**
	 * This function is used in initialisation of the server's database. 
	 * It retrieves a list of books requested and adds them to the server's database.
	 * @param listOfBooks The books to be added into the server's database
	 * @return the list of books that have been added to the database
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
} // end of class TCPServer