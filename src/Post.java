/**
 * 
 * @author David Choon Ming, Wong
 * This class implements the discussion posts
 * Each post contains a message and is classified by a serial number.
 */
public class Post implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private String serialNumber;
	private String message;
	
	public Post(String sn, String m){
		serialNumber = sn;
		message = m;
	}
	
	//Getters and Setters
	public String getSerialNumber(){
		return serialNumber;
	}
	
	public String getMessage(){
		return message;
	}
}
