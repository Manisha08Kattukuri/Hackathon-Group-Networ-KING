
import java.util.*;
import java.text.*;


public class Validations {
  
    
    private String From;
    private String To;
	
    private static final String CRLF = "\r\n";
    
    
    public String Headers;
    public String Body;


 
    public Validations(String from, String to, String subject, String text) {
	
	From = from.trim();
	To = to.trim();
	Headers = "From: " + From + CRLF;
	Headers += "To: " + To + CRLF;
	Headers += "Subject: " + subject.trim() + CRLF;

	SimpleDateFormat dateFormat = 
	    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
	String dateString = dateFormat.format(new Date());
	Headers += "Date: " + dateString + CRLF;
	Body = text;
    }

    public String getTo() {
    	return To;
        }
    
    public String getFrom() {
	return From;
    }

   
    public boolean isValid() {
    int toAddr = To.indexOf('@');	
	int formAddr = From.indexOf('@');
	

	if(formAddr < 1 || (From.length() - formAddr) <= 1) {
	    System.out.println("Invalid sender addresults");
	    return false;
	}
	
	if(formAddr != From.lastIndexOf('@')) {
	    System.out.println("Invalid sender addresults");
	    return false;
	}
	
	if(toAddr < 1 || (To.length() - toAddr) <= 1) {
	    System.out.println("Invalid recipient addresults");
	    return false;
	}
	
	if(toAddr != To.lastIndexOf('@')) {
	    System.out.println("Invalid recipient addresults");
	    return false;
	}	
	return true;
    }
    

    public String toString() {
	String result;

	result = Headers + CRLF;
	result += Body;
	return result;
    }
}





