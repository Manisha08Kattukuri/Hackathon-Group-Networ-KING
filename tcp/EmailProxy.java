package tcp;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailProxy {

	/**
	 * @param args
	 */
	
	protected ServerSocket serveSocket;
	
	protected ExecutorService serviceExecutor;
	
	protected static int PORT;
	
    public EmailProxy(int port){
    	serviceExecutor = Executors.newCachedThreadPool();
    	try {
    		serveSocket = new ServerSocket(port);
    	}
    	catch(IOException e){
    		System.out.println(e);
    		
    	}
    }
	
    public void accept() {
    	
    	while(true){
    		try{
    			serviceExecutor.execute(new RequestHandler(serveSocket.accept())); 
    		}
    		catch(IOException e){
    			System.out.println(e);
    		}
    	}
		
	}
    
	public static void main(String[] args) {
		
        System.out.println("EmailServer is listening to the port"+PORT );
        EmailProxy emProxy = new EmailProxy(PORT);
        emProxy.accept();
        
        
	}

}
