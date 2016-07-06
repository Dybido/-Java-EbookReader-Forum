import java.util.*;
import java.io.*;

/**
 * 
 * @author David Choon Ming, Wong
 * This class implements the pages in a book.
 * Each book has 4 pages and each page has 9 lines. 
 * 
 * The first index[0] is always the story
 * The following indexes are the discussion posts. 
 * 
 * Each page has a page number, an array of discussion posts that includes the story.
 */
public class Page implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<ArrayList<Post>> posts;
	private int pageNumber;
	
	public Page(int pN){
		posts = new ArrayList<ArrayList<Post>>();
		for(int i=0; i<9; i++){
			posts.add(new ArrayList<Post>());
		}
		pageNumber = pN;
	}
	
	/**
	 * Creates the story in each of the pages
	 * @param filename Eg. exupery_page0
	 * @throws FileNotFoundException 
	 */
	public void initialise(String filename) throws FileNotFoundException{
		//Use this line if executing in Eclipse
			//String file = "./src/"+filename;
		//Use this line if executing in command line
		String file = filename;
		Scanner sc = new Scanner(new File(file));
		int i=0;
		while(sc.hasNextLine()){	
			String sentence = sc.nextLine();
			Post p = new Post("0", sentence);
			posts.get(i).add(p);
			i++;
		}
		sc.close();
	}
	
	//Getters and Setters
	public int getPageNumber(){
		return pageNumber;
	}
	
	public List<ArrayList<Post>> getPost(){
		return posts;
	}

	
}
