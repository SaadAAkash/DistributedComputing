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
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

        
class WorkerClass {

    public boolean coOrdStatus = true;

    
    BufferedReader inPort;
    Scanner stdIn;
    PrintWriter outPort;
    Socket socket;
    final String serverIP = "127.0.0.1";
     InetAddress ip;
    
    final String ownIP = "127.0.0.1";
    final int port = 8000;
    
    ServerSocket ownSocket, ownSocket2;
    String str = "";

    boolean close = false;
    int Creating_time;
    int LocalPort, LocalPort2;
    String allWorkerArr[];

    WorkerClass() throws IOException, InterruptedException {
        ip = InetAddress.getLocalHost();
        stdIn = new Scanner(System.in);
        
        ownSocket = new ServerSocket(0);
        ownSocket2 = new ServerSocket(0);

        inPort = null;outPort = null;socket = null;
        LocalPort = ownSocket.getLocalPort();//stdIn.nextInt();
        LocalPort2 = ownSocket2.getLocalPort();
        System.out.println("My localport " + LocalPort);
        Random rn = new Random();

        Creating_time = rn.nextInt(10000) + 1;
        System.out.println("My random time: " + Creating_time);
        
        Connect();
        while (true) {
            Creating_time++;
            System.out.println("TIMEEE:" + Creating_time);
            int x = rn.nextInt(50);
            long sleepinit = System.currentTimeMillis();
            new RcvSendThreadMain();
            //while ((System.currentTimeMillis() - sleepinit) < (x + 50));
               //for safety of sleep times for threads
        }
        
        //new RcvSendThreadMain();
        /*
        while (true) {
            Creating_time++;
            //System.out.println("TIMEEE:" +Creating_time);
            int x = rn.nextInt(50);
            long sleepinit = System.currentTimeMillis();
            while ((System.currentTimeMillis() - sleepinit) < (x + 50));
               //for safety of sleep times for threads
        }
        */

    }

    void Connect() throws IOException, InterruptedException {
        try {
            socket = new Socket(serverIP, port);
            outPort = new PrintWriter(socket.getOutputStream(), true);
            inPort = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException ex) {
            System.out.println("Connection Problem: " + ex);
        }

        String pid = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println("Process ID (PID): " + pid);
        
        Random rand = new Random();
 
        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(100);
        System.out.println(rand_int1/100);
        if (rand_int1 > 85) 
        {
            System.out.println("Sending a message to check whether the coordinator is dead");
            coOrdStatus = false;
        }
            else 
        {
            System.out.println("No Action");
        
        }
            
        
        //WORKER SENDING TO SYNCMAIN, SYNCMAIN WILL ADD IT TO WORKERLIST,
        //SEE run() in Class Sync, WorkerSocketList.add(inPort.readLine());
        outPort.println(ownIP + "m" + LocalPort + "m" + LocalPort2);
        
        
        String allWorker = null;
        //RECEIVE ALL WORKER LIST
        allWorker = inPort.readLine();
        {
            allWorkerArr = allWorker.split("o");
            new SendThreadMain();

        }
    }

    
    public boolean getStatus()
    {
        return coOrdStatus;
    }
    
    class SendThreadMain implements Runnable {

        String strarr[];

        public SendThreadMain() throws InterruptedException {
            for (int i = 0; i < allWorkerArr.length; i++) {
                strarr = allWorkerArr[i].split("m");
                if (ownIP.equals(strarr[0]) && LocalPort == Integer.parseInt(strarr[1])) {
                    continue;
                }
                Thread thread = new Thread(this);
                thread.start();
                Thread.sleep(1000);
            }
        }

        @Override
        public void run() {
            Socket mywsocket = null;
            try {
                if (ownIP.equals(strarr[0]) && LocalPort == Integer.parseInt(strarr[1])) {
                    return;
                }
                mywsocket = new Socket(strarr[0], Integer.parseInt(strarr[1]));
                PrintWriter mywoutPort = null;
                BufferedReader mywinPort = null;
                mywoutPort = new PrintWriter(mywsocket.getOutputStream(), true);
                mywinPort = new BufferedReader(new InputStreamReader(mywsocket.getInputStream()));
                mywoutPort.println(Creating_time);
                String str = null;
                while ((str = mywinPort.readLine()) != null) {
                    System.out.println(str);
                }
                mywinPort.close();
                mywoutPort.close();
            } catch (IOException ex) {
                return;
             }
        }
    }
   
    class RcvSendThreadMain implements Runnable {
        Socket clientSocket = null;
        RcvSendThreadMain() throws IOException {
            while (true) {
                clientSocket = ownSocket.accept();
                Thread thread = new Thread(this);
                thread.start();
            }
        }
        
        @Override
        public void run() {
            Socket wself = null;
            synchronized (clientSocket) {
                wself = clientSocket;
            }

            BufferedReader inPort = null;
            PrintWriter outPort = null;
            try {
                outPort = new PrintWriter(wself.getOutputStream(), true);
                inPort = new BufferedReader(new InputStreamReader(wself.getInputStream()));
                String str = "";
                
                //System.out.println("TIMEEE:" +Creating_time);
                
                while ((str = inPort.readLine()) != null) {
                    int time = Integer.parseInt(str);
                    System.out.println("New Time Received:" + time + " Prev my rand time: " + Creating_time);
                    if (time > Creating_time) 
                    {
                        System.out.println("Time updated");
                        outPort.print( LocalPort + "@" + ownIP + " prints -> TIME UPDTED");
                        Creating_time = time;
                        break;
                    } else if (time <= Creating_time) {
                        outPort.print(LocalPort + "@" + ownIP + " prints -> TIME NOT UPDATED");
                        //break;

                    }

                }
                outPort.close();
                inPort.close();
                
                /*try {
                    Thread.sleep (1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WorkerClass.class.getName()).log(Level.SEVERE, null, ex);
                }*/
            } catch (IOException ex) {
                Logger.getLogger(WorkerClass.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}

public class WorkerMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        WorkerClass worker = new WorkerClass();
        
        
    }

}
