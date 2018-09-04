import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.lang.Thread;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class MasterBot extends Thread
{
	private static int portno; 	

	public static void main( String[] args )throws IOException, ClassNotFoundException
    {
		portno = Integer.parseInt(args[1]);
		MasterThreader mth = new MasterThreader(portno);
	    	mth.start();
		
		try
		{
			while(true)
			{
				
				System.out.print(">");
                	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                	String command = br.readLine();
				String[] splitm =command.split("\\s+");
				String op = splitm[0];
				//System.out.println(op);
        			if(command.equals("list"))
        			{
        				mth.showList();
        				
        			}
        			if(op.equals("connect"))
        			{
        				mth.connectClient(command);
        				
        			}
        			if(op.equals("disconnect"))
        			{
        				mth.disconnectClient(command);
        			}
				if(op.equals("ipscan"))
        			{
        				Thread tip = new Ip_Scan(command);
					tip.start();
        			}
				if(op.equals("tcpportscan"))
        			{
        				
					Thread tport = new Tcpport_Scan(command);
					tport.start();
					
        			}
				if(op.equals("geoipscan"))
    				{
    				
				Thread tgeo = new Geo_Ip_Scan(command);
				tgeo.start();
				
    				}
        			if(command.equals("exit"))
        			{
                     System.exit(0);
        			}

				
				
			}
			
		}
		catch(Exception e)
		{
		
		}
		
 
   }//end of main
}//end of class Master

class List_Slave{
	public static ArrayList<String> Data = new ArrayList<String>();
	public static Map<String, Socket> hashlist = java.util.Collections.synchronizedMap(new HashMap<String,Socket>());	

	List_Slave() {}
}
            
class MasterThreader extends Thread
 {
	int port;
	ServerSocket server;
	//public static ArrayList<String> Data = new ArrayList<String>();
	//public static Map<String, Socket> hashlist = new HashMap<String,Socket>();
	
	public MasterThreader(int port) throws IOException 
	{
        this.port=port;   
        server = new ServerSocket(port);
    }
	
	public void run() 
	  {
		
		try 
	     {	
		while(true)
		{	
			Socket socket = server.accept();
			String hostName = (java.net.InetAddress.getLocalHost()).toString();
			String ipAddr = (socket.getInetAddress()).toString();
		  	Integer portno = new Integer(socket.getPort());
			String slave_port = portno.toString();	
			String date = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());	
			String entry = hostName + " " + ipAddr + " " + slave_port + " " +date  ;
			List_Slave.Data.add(entry);
			List_Slave.hashlist.put(slave_port,socket);
			
			
		}
	}
	 catch (IOException e) 
	{
	      System.out.println(e);
	}
	}//end of run
	
	public void showList()
	{
		for(int i = 0; i < List_Slave.Data.size(); i++) 
		{   
			System.out.println(List_Slave.Data.get(i));
		} 
	}
	
	public void connectClient(String cmd) throws IOException
	{
		String tempCmd = cmd;
		String[] splitCmd =tempCmd.split("\\s+");
		String sIP = splitCmd[1];
		if(sIP.equals("all"))
		{
			
			for(Entry<String, Socket> socket_value : List_Slave.hashlist.entrySet() )
			{
				
				Socket temp = socket_value.getValue();
				OutputStream opstrm = temp.getOutputStream();
				PrintWriter out = new PrintWriter(opstrm, true);
				out.println(cmd);

			}
		}
		else
		{
			for(int i=0;i<List_Slave.Data.size();i++)
			{
				if((List_Slave.Data.get(i)).contains(sIP))
				{
					for(Entry<String, Socket> port_value : List_Slave.hashlist.entrySet() )
					{
						String portno=port_value.getKey();
						if((List_Slave.Data.get(i)).contains(portno))
						{
						Socket temp=port_value.getValue();
						OutputStream opstrm = temp.getOutputStream();
						PrintWriter out = new PrintWriter(opstrm, true);
						out.println(cmd);
						}
					}	
				}
			}
		}
		
		
	}
	
	public void disconnectClient(String cmd) throws IOException
	{
		String tempCmd = cmd;
		String[] splitCmd =tempCmd.split("\\s+");
		String sIP = splitCmd[1];
		if(sIP.equals("all"))
		{
			
			for(Entry<String, Socket> socket_value : List_Slave.hashlist.entrySet() )
			{
				
				Socket temp = socket_value.getValue();
				OutputStream opstrm = temp.getOutputStream();
				PrintWriter out = new PrintWriter(opstrm, true);
				out.println(cmd);
			}
		}
		else
		{
			for(int i=0;i<List_Slave.Data.size();i++)
			{
				//System.out.println(Data.get(i));
				if((List_Slave.Data.get(i)).contains(sIP))
				{
					for(Entry<String, Socket> port_value : List_Slave.hashlist.entrySet() )
					{
						String portno=port_value.getKey();
						
						//System.out.println(portno);
						if((List_Slave.Data.get(i)).contains(portno))
						{
						Socket temp=port_value.getValue();
						
						OutputStream opstrm = temp.getOutputStream();
						PrintWriter out = new PrintWriter(opstrm, true);
						out.println(cmd);
						}
					}	
				}
			}
		}
		
	}

	
}
 

class Ip_Scan extends Thread
{
String recv_cmd;
Ip_Scan(String command)
{
	recv_cmd = command;

}
public void run()
{
try
		{
		
                //System.out.println("IP SCAN");
		String[] splitCmd =recv_cmd.split("\\s+");
		String sIP = splitCmd[1];
		if(sIP.equals("all"))
		{
			
			for(Entry<String, Socket> socket_value : List_Slave.hashlist.entrySet() )
			{
				
				Socket temp = socket_value.getValue();
				
				OutputStream opstrm = temp.getOutputStream();
				PrintWriter out = new PrintWriter(opstrm, true);
				out.println(recv_cmd);
				
				BufferedReader brdr = new BufferedReader(new InputStreamReader(temp.getInputStream(),"UTF-8"));
				
				
				String result = " ";
				result = brdr.readLine();
				
				if(result.length()!=0)
				{
				System.out.println("Responded IP Address List:");
				System.out.println(result);
				}
												
																		
			}
		}
		else
		{
			for(int i=0;i<List_Slave.Data.size();i++)
			{
				//System.out.println(Data.get(i));
				if((List_Slave.Data.get(i)).contains(sIP))
				{
					for(Entry<String, Socket> port_value : List_Slave.hashlist.entrySet() )
					{
						String portno=port_value.getKey();
						
						//System.out.println(portno);
						if((List_Slave.Data.get(i)).contains(portno))
						{
						Socket temp=port_value.getValue();
						
						OutputStream opstrm = temp.getOutputStream();
						PrintWriter out = new PrintWriter(opstrm, true);
						out.println(recv_cmd);
						BufferedReader brdr = new BufferedReader(new InputStreamReader(temp.getInputStream(),"UTF-8"));
						String result = " ";
						result = brdr.readLine();
						if(result.length()!=0)
						{
						System.out.println("Responded IP Address List:");
						System.out.println(result);
						}

						}
					}	
				}
			}
		}
		}//end of try
		catch(Exception e)
		{
			e.printStackTrace();
		}

	
System.out.print(">");
}//end of run
} 



class Tcpport_Scan extends Thread
{
String recv_cmd;
Tcpport_Scan(String command)
{
	recv_cmd = command;

}
public void run()
{
        //System.out.println("TCP PORT SCAN");
	try
	 	{
		
		String[] splitCmd =recv_cmd.split("\\s+");
		String sIP = splitCmd[1];
		if(sIP.equals("all"))
		{
			
			for(Entry<String, Socket> socket_value : List_Slave.hashlist.entrySet() )
			{
				
				Socket temp = socket_value.getValue();
				
				OutputStream opstrm = temp.getOutputStream();
				PrintWriter out = new PrintWriter(opstrm, true);
				out.println(recv_cmd);
                BufferedReader brdr = new BufferedReader(new InputStreamReader(temp.getInputStream(),"UTF-8"));
				String result = brdr.readLine();
				if(result.length()!=0)
				{
                      System.out.println("Port List:");
                      System.out.println(result);
				}	
                               
					
			}
		}
		else
		{
			for(int i=0;i<List_Slave.Data.size();i++)
			{
				//System.out.println(Data.get(i));
				if((List_Slave.Data.get(i)).contains(sIP))
				{
					for(Entry<String, Socket> port_value : List_Slave.hashlist.entrySet() )
					{
						String portno=port_value.getKey();
						
						//System.out.println(portno);
						if((List_Slave.Data.get(i)).contains(portno))
						{
						Socket temp=port_value.getValue();
						
						OutputStream opstrm = temp.getOutputStream();
						PrintWriter out = new PrintWriter(opstrm, true);
						out.println(recv_cmd);
						BufferedReader brdr = new BufferedReader(new InputStreamReader(temp.getInputStream(),"UTF-8"));
						String result = " ";
					    result = brdr.readLine();
						if(result.length()!=0)
						{
	                          System.out.println("Port List:");
	                          System.out.println(result);
						}
						}
					}	
				}
			}
		}
		}//end of try
		catch(Exception e)
		{
			e.printStackTrace();
		}


System.out.print(">");

}
}//end of Tcpport_Scan

class Geo_Ip_Scan extends Thread
{
	String recv_cmd;
	Geo_Ip_Scan(String command)
	{
		recv_cmd = command;

	}
	public void run()
	{
	        
		try
		 	{
			String[] splitCmd =recv_cmd.split("\\s+");
			String sIP = splitCmd[1];
			if(sIP.equals("all"))
			{
				
				for(Entry<String, Socket> socket_value : List_Slave.hashlist.entrySet() )
				{
					
					Socket temp = socket_value.getValue();
					
					OutputStream opstrm = temp.getOutputStream();
					PrintWriter out = new PrintWriter(opstrm, true);
					out.println(recv_cmd);
	                BufferedReader brdr = new BufferedReader(new InputStreamReader(temp.getInputStream(),"UTF-8"));
					String result = brdr.readLine();
					if(result.length()!=0)
					{
                          System.out.println("Ip List:");
                          if(result.contains(":"))
                          {
  							String finalresult = result.replace(":", "\n");
  							System.out.println(finalresult);
  						 }
                         else
                                 {
                                 System.out.println(result);
                                 }
                          
					}
	                               
						
				}
			}
			else
			{
				for(int i=0;i<List_Slave.Data.size();i++)
				{
					//System.out.println(Data.get(i));
					if((List_Slave.Data.get(i)).contains(sIP))
					{
						for(Entry<String, Socket> port_value : List_Slave.hashlist.entrySet() )
						{
							String portno=port_value.getKey();
							
							//System.out.println(portno);
							if((List_Slave.Data.get(i)).contains(portno))
							{
							Socket temp=port_value.getValue();
							
							OutputStream opstrm = temp.getOutputStream();
							PrintWriter out = new PrintWriter(opstrm, true);
							out.println(recv_cmd);
							BufferedReader brdr = new BufferedReader(new InputStreamReader(temp.getInputStream(),"UTF-8"));
							String result = " ";
						    result = brdr.readLine();

							if(result.length()!=0)
							{
		                          System.out.println("Ip List:");
		                          if(result.contains(":"))
		                          {
                                  
		  							String finalresult = result.replace(":", "\n");
		  							System.out.println(finalresult);
		  						 }
                                 else
                                 {
                                    System.out.println(result);
                                 }
		                          
							}
							}
						}	
					}
				}
			}
		 	}//end of try
		catch(Exception e)
		{
			e.printStackTrace();
		}


System.out.print(">");
	}
}
