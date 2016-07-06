import java.io.FileNotFoundException;
import java.util.*;

/**
 * 
 * @author DAVID CHOON MING, WONG
 * This class implements the books stored in a server
 * A Server has a list of Book(s).
 * 
 * Each book has a list of pages(max 4) and a book name
 */
public class Book {
	private ArrayList<Page> pages;
	private String name;
	
	public Book(String n){
		pages = new ArrayList<Page>();
		this.name = n;
	}
	
	/**
	 * Creates the pages of the book by calling initialise(). The page is then added to the array. 
	 * @param filename The story in each page
	 * @param pageNumber 
	 * @throws FileNotFoundException
	 * 
	 */
	public void createPage(String filename, int pageNumber) throws FileNotFoundException{
		filename = filename + pageNumber;		
		Page newPage = new Page(pageNumber);
		newPage.initialise(filename);
		pages.add(newPage);
	}
	
	/**
	 * Given the page number and message, it attaches a discussion post to the specified line number 
	 * @param pageNumber
	 * @param lineNumber
	 * @param message The discussion post to be added to the story in question
	 */
	public void addDiscussion(int pageNumber, int lineNumber, String message){
		//Arrays start at 0, so to get the exact lineNumber = lineNumber-1
		int ln = lineNumber-1;
		for(Page p : pages){
			if(p.getPageNumber() == pageNumber){
				String serialNumber = String.valueOf(p.getPost().get(ln).size());
				Post newPost = new Post(serialNumber, message);
				p.getPost().get(ln).add(newPost);
			}
		}
	}
	
	//Getters & Setters
	public Page getPage(int pageNumber){
		for(Page p : pages){
			if (p.getPageNumber() == pageNumber){	
				return p; 
			}
		}
		return null;
	}
	
	public ArrayList<Page> getPages(){
		return pages;
	}
	
	public String getName(){
		return name;
	}
}
