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

public class SyncNew {
	ArrayList <WorkerNodes> workerList;
	int currentPort = 1234;//int currentPort = 5000;
	public SyncNew()
        {
		workerList = new ArrayList<WorkerNodes>();
		try{
			ServerSocket ss = new ServerSocket(1852);
			while(true)
                        {
				Socket socket = ss.accept();
				System.out.println("A NEW PROCESS HAS BEEN CREATED NOW!");
                                
                                //
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				out.writeUTF(currentPort+"");
				
				String ip = in.readUTF();
				
				transmitAll(out);
                                
				System.out.println("NEW WORKER ADDED: " + ip + ":" + currentPort);
				
                                broadcastAll(ip, currentPort);
				
				workerList.add(new WorkerNodes(socket, ip, currentPort));
				currentPort++;
			}
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
//	private void transmitAll(DataOutputStream out) throws IOException{
//		for(WorkerNodes x : workerList){
//			out.writeUTF(x.ip + " " + x.port);
//		}
//	}
//	
//	private void broadcastAll(String ip, int currentPort) throws IOException
//        {
//		for(WorkerNodes x : workerList)
//                {
//			DataOutputStream out = new DataOutputStream(x.socket.getOutputStream());
//			out.writeUTF(ip+ " " + currentPort);
//		}
//	}
//	
        private void transmitAll(DataOutputStream out) {
            try{
                
		for(WorkerNodes x : workerList){
			out.writeUTF(x.ip + " " + x.port);
		}
            }catch(Exception e)
            {
                
            }
	}
	
	private void broadcastAll(String ip, int currentPort) 
        {
		try{
                    for(WorkerNodes x : workerList)
                    {
                            DataOutputStream out = new DataOutputStream(x.socket.getOutputStream());
                            out.writeUTF(ip+ " " + currentPort);
                    }
                }catch(Exception e)
                {
                    
                }
	}
	
        
	public static void main(String args[]){
		new SyncNew();
	}
	

}
