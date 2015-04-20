package tcp;

import java.io.DataInputStream;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.net.SocketException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.net.Socket;

public class RequestHandler implements Runnable {

	protected DataInputStream clientIptStam;
	protected InputStream remoteIptStam;
	protected HashMap<String, String> header;
	protected OutputStream clientOptStam,remoteOptStam;
	static String endOfLine = "\r\n";
	protected Socket remoteSk,clientSk;
	protected String requestType,httpV,uri,url;
	public RequestHandler(Socket clientSk) 
	{
		header = new HashMap<String, String>();
		this.clientSk = clientSk;
	}

	public void run() 
	{
		try {
			clientIptStam = new DataInputStream(clientSk.getInputStream());
			clientOptStam = clientSk.getOutputStream();
			cToP();
			pToR();
			rToC();
			System.out.println("Sucessful");
			
			if(remoteIptStam != null) remoteIptStam.close();
			if(remoteSk != null) remoteSk.close();
			if(remoteOptStam != null) remoteOptStam.close();
			if(clientSk != null) clientSk.close();
			if(clientOptStam != null) clientOptStam.close();
			if(remoteSk != null) remoteSk.close();
			if(clientIptStam != null) clientIptStam.close();
		} 
		catch (IOException e) 
		{ 
			System.out.println("There is an Error");
		}
	}
	@SuppressWarnings("deprecation")
	private void gUri() 
	{
		if(header.containsKey("host")) 
		{
			int temp = url.indexOf(header.get("host"));
			temp += header.get("host").length();
			if(temp < 0) 
			{ 
				uri = url;
			} 
			else 
			{
				uri = url.substring(temp);	
			}
		}
	}

	private void pToR () 
	{
		try
		{
			if(header.get("host") == null)
			{ 
				return;
			}
			if(!requestType.startsWith("POST") &&  !requestType.startsWith("GET"))
			{ 
				return;
			}
			remoteSk = new Socket(header.get("host"), 90);
			remoteOptStam = remoteSk.getOutputStream();
			ckRStm();
			ckCStm();
			String req = requestType + " " + uri + " HTTP/1.1";
			remoteOptStam.write(req.getBytes());
			remoteOptStam.write(endOfLine.getBytes());
			System.out.println("I am printing request:" + req);
			String cmd = "host: "+ header.get("host");
			remoteOptStam.write(cmd.getBytes());
			remoteOptStam.write(endOfLine.getBytes());
			System.out.println(cmd);
			for( String k : header.keySet()) 
			{
				if(!k.equals("host"))
				{
					cmd = k + ": "+ header.get(k);
					remoteOptStam.write(cmd.getBytes());
					remoteOptStam.write(endOfLine.getBytes());
					System.out.println(cmd);
				}
			}
			remoteOptStam.write(endOfLine.getBytes());
			remoteOptStam.flush();
			if(requestType.startsWith("POST")) 
			{
				int cL = Integer.parseInt(header.get("content-length"));
				for (int i = 0; i < cL; i++)
				{
					remoteOptStam.write(clientIptStam.read());
				}
			}
			remoteOptStam.write(endOfLine.getBytes());
			remoteOptStam.flush();
		}
		catch (IOException e) 
		{
		System.out.println("There is an Error"); 
		return; 
		}

	}
	@SuppressWarnings("deprecation")
	
	private void cToP () 
	{
		StringTokenizer tokens;
		String k, linee, val;
		
		try {
			if(( endOfLine = clientIptStam.readLine()) != null) 
			{
				tokens = new StringTokenizer(endOfLine);
				requestType = tokens.nextToken();
				httpV = tokens.nextToken();
				url = tokens.nextToken();
			}
			while((endOfLine = clientIptStam.readLine()) != null) 
			{
				if(endOfLine.trim().length() == 0) 
				break;
				System.out.println(endOfLine);
				tokens = new StringTokenizer(endOfLine);
				k = tokens.nextToken(":");
				val = endOfLine.replaceAll(k, "").replace(": ", "");
				header.put(k.toLowerCase(), val);
			}
			stripUnHeader();
			gUri();
		} 
		catch (IOException e) 
		{ 
			System.out.println("There is an Error");
			return; 
		} 

	}


	private void rToC () {

		try {
			if(remoteSk == null) 
			{
				return;
			}
			String line;
			DataInputStream remoteOutHeader = new DataInputStream(remoteSk.getInputStream());
			while((line = remoteOutHeader.readLine()) != null) 
			{
				if(line.trim().length() == 0) 
				{
					break;
				}
				if(line.toLowerCase().startsWith("proxy"))
				{ 
					continue;
				}
				if(line.contains("keep-alive")) 
				{ 
					continue;
				}
				if(line.contains("Last-Modified")) 						{
					System.out.println(line+"last modifed");
				}					
				System.out.println(line);
				clientOptStam.write(line.getBytes());
				clientOptStam.write(endOfLine.getBytes());
			}
			clientOptStam.write(endOfLine.getBytes());
			clientOptStam.flush();
			remoteIptStam = remoteSk.getInputStream();
			byte[] buffer = new byte[1024];
			for(int i; (i = remoteIptStam.read(buffer)) != -1;) 
			{
				clientOptStam.write(buffer, 0, i);
				clientOptStam.flush();
			}
		} 
		catch (IOException e) 
		{
			System.out.println("There is an Error"); 
			return; 
		} 
	}

	private void ckCStm () 
	{
		try 
		{
			if(clientSk.isOutputShutdown())	
			{
				clientOptStam = clientSk.getOutputStream();
			}
			if(clientSk.isInputShutdown())	
			{
				clientIptStam = new DataInputStream(clientSk.getInputStream());
			}
		}
		catch (IOException e) 
		{
			System.out.println("There is an Error"); 
			return; 
		}

	}
	private void stripUnHeader() 
	{
		if(header.containsKey("user-agent")) 
		{	
			header.remove("user-agent");
		}
		if(header.containsKey("referer")) 
		{
			header.remove("referer");
		}
		if(header.containsKey("proxy-connection")) 
		{
			header.remove("proxy-connection");
		}
		if(header.get("connection").equalsIgnoreCase("keep-alive")&& header.containsKey("connection")) 
		{
			header.remove("connection");
		}
	}

	private void ckRStm() 
	{
		try 
		{
			if(remoteSk.isOutputShutdown())
			{	
				remoteOptStam = remoteSk.getOutputStream();
			}
			if(remoteSk.isInputShutdown())
			{	
				remoteIptStam = new DataInputStream(remoteSk.getInputStream());
			}				
		} 
		catch (IOException e) 
		{
			System.out.println("There is an Error"); 
			return; 
		}
	}

}
