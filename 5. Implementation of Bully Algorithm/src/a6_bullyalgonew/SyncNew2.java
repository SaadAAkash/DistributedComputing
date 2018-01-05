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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SyncNew2
{
	ArrayList <workerInfo> workerList;
	int currentPort = 5000;
	public SyncNew2(){
		workerList = new ArrayList<workerInfo>();
		try{
			ServerSocket ss = new ServerSocket(1852);
			while(true){
				Socket socket = ss.accept();
				System.out.println("new process");
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				out.writeUTF(currentPort+"");
				
				String ip = in.readUTF();
				
				sendAllAddress(out);
				System.out.println("received new worker: " + ip + ":" + currentPort);
				sendEveryone(ip, currentPort);
				
				workerList.add(new workerInfo(socket, ip, currentPort));
				currentPort++;
			}
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void sendAllAddress(DataOutputStream out) throws IOException{
		for(workerInfo x : workerList){
			out.writeUTF(x.ip + " " + x.port);
		}
	}
	
	private void sendEveryone(String ip, int currentPort) throws IOException{
		for(workerInfo x : workerList){
			DataOutputStream out = new DataOutputStream(x.socket.getOutputStream());
			out.writeUTF(ip+ " " + currentPort);
		}
	}
	
	public static void main(String args[]){
		new SyncNew2();
	}
	
	private class workerInfo{
		Socket socket;
		String ip;
		int port;
		public workerInfo(Socket socket, String ip, int port){
			this.socket = socket;
			this.ip = ip;
			this.port = port;
		}
	}
}
