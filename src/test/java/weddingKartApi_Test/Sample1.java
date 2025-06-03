package weddingKartApi_Test;
import java.util.*;
public class Sample1 {
	

	
	    public static void main(String[] args) {
	        String s="aaabbaacc";
	        
	     HashSet<Character> set=new HashSet<Character>();
	     for(int i=0; i<s.length(); i++){
	         set.add(s.charAt(i));
	     }
	       for(int i=0; i<s.length(); i++){
	         int count=0;
	         for(char ch:s.toCharArray()){
	             if(s.charAt(i)==(ch)){
	                 count++;
	             }
	            
	         }
	         System.out.println(s.charAt(i)+" "+count);
	     }
	     
	    }
	

}
