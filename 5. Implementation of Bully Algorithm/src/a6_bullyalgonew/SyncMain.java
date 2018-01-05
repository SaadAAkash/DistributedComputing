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

import java.io.*;
import static java.lang.Thread.sleep;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.security.acl.WorldGroupImpl;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;

class Sync implements Runnable {

    static int counter = 0;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    PrintWriter outPortArray[] = new PrintWriter[1000];
    BufferedReader inArray[] = new BufferedReader[1000];


    ArrayList<String> WorkerSocketList = new ArrayList<String>();
    ArrayList<String> toDel = new ArrayList<String>();

    int count = 0;
    String str = "";
    int flag[] = new int[1000];

    Sync(Socket clientSocket) throws IOException, InterruptedException {
        this.clientSocket = clientSocket;
        new CheckThread();
        SocketInit();
    }

    void SocketInit() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(8000);
        long curTime = System.currentTimeMillis();
        {
            while(true){
                //accept a socket request
                clientSocket = serverSocket.accept();
                //PRINTING THE SOCKET
                System.out.println(clientSocket);
                   //socjet accept & so start a thread
                Thread thread = new Thread(this);
                thread.start();
            }

        }
    }

    @Override
    public void run() {
    BufferedReader inPort = null;
    PrintWriter outPort = null;

    try {
        Socket self = clientSocket;
        outPort = new PrintWriter(self.getOutputStream(), true);
        inPort = new BufferedReader(new InputStreamReader(self.getInputStream()));

        synchronized(this){
            count++;
        }

        String inputLine = null;

        WorkerSocketList.add(inPort.readLine());
        String allWorker = "";
        for(int i=0 ; i< WorkerSocketList.size(); i++)
        {
            allWorker+= WorkerSocketList.get(i)+"o";

        }
        //System.out.println(allWorker);
        outPort.println(allWorker);
    } catch (IOException ex) {
        System.out.println("Read : " + ex);
    }
    return;
}
   

    class CheckThread implements Runnable {

        BufferedReader inPort = null;
        PrintWriter outPort = null;
        Socket socket = null;
        long prev_time = 0, cur_time = 0;

        public CheckThread() throws InterruptedException {            
                Thread thread = new Thread(this);
                thread.start();
        }

        @Override
        public void run() {
            while (true)
            {
                while (System.currentTimeMillis() - prev_time >= 1000) 
                {
                    int size = WorkerSocketList.size();
                    System.out.print("");
                    Socket testSocket;
                    for(int i=0; i< size; i++)
                    {
                        String str = WorkerSocketList.get(i);
                     
                        String strarr[]= str.split("m");
                        int port = Integer.parseInt(strarr[2]);
                        try
                        {
                            testSocket = new Socket(strarr[0],port);
                        }
                        catch (IOException ex)
                        {
                            toDel.add(str);
                          System.out.println("A worker server stopped");

                            continue;
                        }
                        try {
                            testSocket.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Sync.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                    while (toDel.size() != 0) {
                        
                        if(!WorkerSocketList.contains(toDel.get(0)))
                        {
                            System.out.println("Worker to be removed not found");
                        }
                        else{
                            WorkerSocketList.remove(toDel.remove(0));
                        }
                    }
                    prev_time = System.currentTimeMillis();
                    System.out.println("Cur Worker Server Count: "+WorkerSocketList.size());
                }
            }
        }
    }  //class CheckThread ends in the class Sync
}

public class SyncMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        //calls class sync
        Sync sync = new Sync(null);

    }
}

