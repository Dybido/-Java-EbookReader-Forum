import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * An individual thread that handles one incoming connection to the server from the reader.
 * @author DAVID CHOON MING, WONG
 *
 */
public class ServerThread extends Thread {
	private final Socket connectionSocket;
	String currBook = "";
	int currPage = 1;
	
	public ServerThread(Socket s){
		this.connectionSocket = s;
	}
	
	@Override
	public void run() {
				
		BufferedReader inFromClient = null;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    String clientSentence;
	    try {
			while((clientSentence = inFromClient.readLine())!= null){
				String[] commands = clientSentence.split(" ");
				if(commands[1].equals("display")){ //display [book_name] [page_number]
					String bookName = commands[2];
					int pageNumber = Integer.parseInt(commands[3]);
					currBook = bookName;
					currPage = pageNumber;
					
					Page p = Server.getBook(Server.getListOfBooks(), currBook).getPage(currPage);
					//ObjectOutputStream allows sending of Arrays
					ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
					outToClient.writeObject(p);	 

				} else if(commands[1].equals("post_to_forum")){ //post_to_forum [line_number] [content_of_post]
					//Array starts at 0 so lineNumber = lineNumber-1
					System.out.println("New post received from " + commands[0]);
					String username = commands[0] + ":";
					int lineNumber = Integer.parseInt(commands[2]);
					
					
					//The post is received as an array. As such, it has to be rebuilt as a string of messages.
					// The username is added to the beginning of the message
					ArrayList<String> post = new ArrayList<String>();
					post.add(username);
					for(int i=3; i<commands.length; i++){
						post.add(commands[i]);
					}
					
					StringBuilder builder = new StringBuilder();
					for(String se : post) {
					    builder.append(se);
					    builder.append(" ");
					}
					String message = builder.toString();
					
					Book b = Server.getBook(Server.getListOfBooks(), currBook);
					b.addDiscussion(currPage, lineNumber, message);
					
					int sizeOfLineNumber = b.getPage(currPage).getPost().get(lineNumber-1).size()-1;
					String serialNumber = b.getPage(currPage).getPost().get(lineNumber-1).get(sizeOfLineNumber).getSerialNumber(); 
					System.out.println("Post added to the database and given the serial number " + serialNumber);
					System.out.println("Push list empty. No action required.");
					
				} else if(commands[1].equals("read_post")){ /// read_post [line_number]
					int lineNumber = Integer.parseInt(commands[2]);
					
					Book b = Server.getBook(Server.getListOfBooks(), currBook);
					//Retrieving the requested discussion on specified line number and copying it into an 
					//	array to be passed back to reader
					ArrayList<Post> posts = new ArrayList<Post>();
					int sizeOfLineNumber = b.getPage(currPage).getPost().get(lineNumber-1).size(); 
					for(int i=1; i<sizeOfLineNumber; i++){
						Post p = b.getPage(currPage).getPost().get(lineNumber-1).get(i);
						posts.add(p);
					}
					ObjectOutputStream outToClient = null;
					try {
						outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						outToClient.writeObject(posts);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	 
				}	// TODO Auto-generated method stub
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}