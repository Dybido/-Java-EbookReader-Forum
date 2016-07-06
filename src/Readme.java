import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Readme {
	public static void main(String[] args)throws Exception {
		/*
		ArrayList<Book> books = new ArrayList<Book>();
		Book exupery = new Book();
		Book joyce = new Book();
		Book shelley = new Book();
		books.add(exupery);
		books.add(joyce);
		books.add(shelley);
		*/
		ArrayList<Page> book = new ArrayList<Page>();
		Page p1 = new Page(1);
		book.add(p1);
	
		/**
		//String file = "./src/exupery_page"+i;
		String file = "./src/exupery_page1";
		Scanner sc = new Scanner(new File(file));
		//System.out.println(file);
		int i = 0 ;
		while(sc.hasNextLine()){	
			String sentence = sc.nextLine();
			//System.out.println(sentence);
			p1.getPost().get(i).add(sentence);
			System.out.println(p1.getPost().get(i));
			i++;
		}
		//To add stuff to new column in same row 
		p1.getPost().get(0).add("poopie butthole");
		System.out.println(p1.getPost().get(0));
		
		System.out.println(p1.getPost().size());
		String lol = foo("Lambda", 10);
		System.out.println(lol);
		/**
		Integer i = 0;
		while(i<9){
			p1.getPost().get(i).add(i.toString());
			System.out.println(p1.getPost().get(0).get(0));
			i++;
		}
		
		//To add more stuff to row 0 : 
		p1.getPost().get(0).add("Line Number 1");
		//To add more stuff to next row 1 : 
		p1.getPost().get(1).add("Line Number 2");
		System.out.println(p1.getPost().get(0));
		System.out.println(p1.getPost().get(1));
		**/
		String[] s = {"hi", "i", "am", "David"};
		
		StringBuilder builder = new StringBuilder();
		for(String se : s) {
		    builder.append(se);
		    builder.append(",");
		}
		System.out.println(builder);

	}
	
	public static String foo(String s, int p){
		s = s + p;
		return s;
	}
}


