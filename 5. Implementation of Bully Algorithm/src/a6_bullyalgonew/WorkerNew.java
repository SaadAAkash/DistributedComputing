/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a6_bullyalgonew;

/**
 *
 * @author Akash
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerNew {
	
	String servername = "localhost";
	
        //co ord & list
	ArrayList <workerNodesPrivate> workerList;
	workerNodesPrivate coordinator;
        
        //ip & ports
        int serverPort = 1852;
	int port;
        String ip;
	
	
	public WorkerNew(String ip) throws UnknownHostException, IOException{
		this.ip = ip;
		workerList = new ArrayList<workerNodesPrivate>();
		Socket client = new Socket(servername, serverPort);
		
		DataInputStream in = new DataInputStream(client.getInputStream());
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		
		String portNo = in.readUTF();
		
                
                String pid = ManagementFactory.getRuntimeMXBean().getName();
                System.out.println("MY Process ID (PID): " + pid);
                
                System.out.println("MY PORT NUMBER: ");
                System.out.println(portNo);
		
		port = Integer.parseInt(portNo);
                coordinator = new workerNodesPrivate(ip, port, false);  //boolean for if it's needed to be printed or not
		
                //starting the threads
		(new receiverThread()).start();
		(new BullyThread()).start();
                
		out.writeUTF(ip);
		
                //runs continuously EVERY TIME when it is run
		while(true){
                    Scanner s = new Scanner(in.readUTF());
                    synchronized(workerList)
                    {
                            workerList.add(new workerNodesPrivate(s.next(), s.nextInt(), true));
                    }
                    s.close();
		}
		
	}
        
	/////////////////////////////////////////////
        ////////////////////////////////////////////
	private class BullyThread extends Thread{
		Random r = new Random();
                
		@Override
		public void run(){
                    try {
                        printCoordinator();
                    } catch (IOException ex) {
                        Logger.getLogger(WorkerNew.class.getName()).log(Level.SEVERE, null, ex);
                    }
			while(true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				double next = r.nextDouble();
				if(next > 0.85){
					try {
						printCoordinator();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/////////////////////////////////////////////
        ////////////////////////////////////////////
	private void printCoordinator() throws UnknownHostException, IOException{
		ArrayList <electionThread> brt = new ArrayList<electionThread>();
		boolean sent = false;
		for(workerNodesPrivate x : workerList){
			if(x.port > port){
				sent = true;
				brt.add(new electionThread(x));				
			}
		}
		for(electionThread x : brt){
			try {
				x.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(!sent){
			for(workerNodesPrivate x : workerList){
				Socket socket = null;
				try{
					socket = new Socket(x.ip, x.port);
					DataOutputStream out_temp = new DataOutputStream(socket.getOutputStream());
					//out_temp.writeUTF("coordinator: IP: "+ip+ " Port: " + port);
                                        out_temp.writeUTF("coordinator "+ip+ " " + port);
				}
				catch(ConnectException e){
					
				}
			}
		}
	}
	
	/////////////////////////////////////////////
        ////////////////////////////////////////////
	private class electionThread extends Thread{
		workerNodesPrivate x ;
		public electionThread(workerNodesPrivate x){
			this.x = x;
			start();
		}
		@Override
		public void run(){
			Socket socket;
			try {
				socket = new Socket(x.ip, x.port);        //socket.setSoTimeout(1000); --> google search
				//inp & out
                                DataInputStream in_temp = new DataInputStream(socket.getInputStream());
				DataOutputStream out_temp = new DataOutputStream(socket.getOutputStream());
				out_temp.writeUTF("election");
				String msg = in_temp.readUTF();
				System.out.println(msg);
			}catch (IOException e) {
				if(x.port == coordinator.port){
					//System.err.println("coordinator" + coordinator.ip + ":" + coordinator.port+ " IS NOT ALIVE");
                                       
                                        System.out.println("coordinator" + coordinator.ip + ":" + coordinator.port+ " IS NOT ALIVE");
                                        
                                        synchronized(workerList){
                                            for(workerNodesPrivate x : workerList){
                                                if(x.port == coordinator.port){
                                                    workerList.remove(x);
                                                    break;
                                                }
                                            }
                                        }
                                    try {
                                        
                                        printCoordinator();
                                       
                                    } catch (IOException ex) {
                                        Logger.getLogger(WorkerNew.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
			} 
			
		}
	}
	
	/////////////////////////////////////////////
        ////////////////////////////////////////////
	private class receiverThread extends Thread{
		ServerSocket ss;
		@Override
		public void run(){
			try {
				ss = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while(true){
				try {
					Socket socket = ss.accept();
					
                                        //inp & out
                                        DataInputStream in_temp = new DataInputStream(socket.getInputStream());
					DataOutputStream out_temp = new DataOutputStream(socket.getOutputStream());
					
					String msg = in_temp.readUTF();
					System.out.println(msg);
					if(msg.contains("coordinator"))
                                        {
						Scanner s = new Scanner(msg);
						s.next();
						synchronized(coordinator){
							coordinator = new workerNodesPrivate(s.next(), s.nextInt(), false);
						}
						s.close();
					}
					else
						out_temp.writeUTF("RCVR THREAD UNNING SUCCESSFULLY......");
															
				} catch (IOException e) {
					System.err.println("SOKET NOT ACCEPTED");
					e.printStackTrace();
				}
				
			}
		}
	}
	
	/////////////////////////////////////////////
        ////////////////////////////////////////////
	private class workerNodesPrivate{
		String ip;
		int port;
		public workerNodesPrivate(String ip, int port, boolean print){
			this.ip = ip;
			this.port = port;
			if(print)
				System.out.println("NODE CREATED : " + ip+":" + port);
		}
	}
	
	/////////////////////////////////////////////
        ////////////////////////////////////////////
	public static void main(String args[]) throws UnknownHostException, IOException{
		new WorkerNew("localhost");
	}
}
