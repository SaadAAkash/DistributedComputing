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

public class WorkerNew2 {
	
	String servername = "localhost";
	int serverPort = 1852;
	String ip;
	int port;
	ArrayList <workerInfo> workerList;
	workerInfo coordinator;
	
	public WorkerNew2(String ip) throws UnknownHostException, IOException{
		this.ip = ip;
		workerList = new ArrayList<workerInfo>();
		
		
		Socket client = new Socket(servername, serverPort);
		
		DataInputStream in = new DataInputStream(client.getInputStream());
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		
		String portNo = in.readUTF();
		System.out.println(portNo);
		
		port = Integer.parseInt(portNo);
		
		coordinator = new workerInfo(ip, port, false);
		
		(new receiverThread()).start();
		(new BullyThread()).start();
		out.writeUTF(ip);
		
		while(true){
			Scanner s = new Scanner(in.readUTF());
			synchronized(workerList){
				workerList.add(new workerInfo(s.next(), s.nextInt(), true));
			}
			s.close();
		}
		
	}
	private class BullyThread extends Thread{
		Random r = new Random();
                
		@Override
		public void run(){
                    try {
                        bully();
                    } catch (IOException ex) {
                        Logger.getLogger(WorkerNew2.class.getName()).log(Level.SEVERE, null, ex);
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
						bully();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void bully() throws UnknownHostException, IOException{
		ArrayList <BullyRequestThread> brt = new ArrayList<BullyRequestThread>();
		boolean sent = false;
		for(workerInfo x : workerList){
			if(x.port > port){
				sent = true;
				brt.add(new BullyRequestThread(x));				
			}
		}
		for(BullyRequestThread x : brt){
			try {
				x.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(!sent){
			for(workerInfo x : workerList){
				Socket socket = null;
				try{
					socket = new Socket(x.ip, x.port);
					DataOutputStream out_temp = new DataOutputStream(socket.getOutputStream());
					out_temp.writeUTF("coordinator "+ip+ " " + port);
				}
				catch(ConnectException e){
					
				}
			}
		}
	}
	
	private class BullyRequestThread extends Thread{
		workerInfo x ;
		public BullyRequestThread(workerInfo x){
			this.x = x;
			start();
		}
		@Override
		public void run(){
			Socket socket;
			try {
				socket = new Socket(x.ip, x.port);
				//socket.setSoTimeout(1000);
				DataInputStream in_temp = new DataInputStream(socket.getInputStream());
				DataOutputStream out_temp = new DataOutputStream(socket.getOutputStream());
				out_temp.writeUTF("election");
				String msg = in_temp.readUTF();
				System.out.println(msg);
			}catch (IOException e) {
				if(x.port == coordinator.port){
					System.err.println("coordinator" + coordinator.ip + ":" + coordinator.port+ " is dead");
                                        synchronized(workerList){
                                            for(workerInfo x : workerList){
                                                if(x.port == coordinator.port){
                                                    workerList.remove(x);
                                                    break;
                                                }
                                            }
                                        }
                                    try {
                                        bully();
                                        //e.printStackTrace();
                                    } catch (IOException ex) {
                                        Logger.getLogger(WorkerNew2.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
			} 
			
		}
	}
	
	
	
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
					DataInputStream in_temp = new DataInputStream(socket.getInputStream());
					DataOutputStream out_temp = new DataOutputStream(socket.getOutputStream());
					
					String msg = in_temp.readUTF();
					System.out.println(msg);
					if(msg.contains("coordinator")){
						Scanner s = new Scanner(msg);
						s.next();
						synchronized(coordinator){
							coordinator = new workerInfo(s.next(), s.nextInt(), false);
						}
						s.close();
					}
					else
						out_temp.writeUTF("ok");
															
				} catch (IOException e) {
					System.err.println("Socket rejected");
					e.printStackTrace();
				}
				
			}
		}
	}
	
	private class workerInfo{
		String ip;
		int port;
		public workerInfo(String ip, int port, boolean print){
			this.ip = ip;
			this.port = port;
			if(print)
				System.out.println("created : " + ip+":" + port);
		}
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		new WorkerNew2("localhost");
	}
}
