
/**
 * Created by akash on 8/11/17.
 */

import java.net.*;
import java.util.*;
import java.io.*;


//creates its server socket on a port
//creates worker object with an ip & different port
//runs two threads
//One thread runs forever, adds/removes workers & shows how many worker servers are there
//Another thread, also runs forever, accepts client and starts new thread for client

public class MasterServer extends MainController {

    public static ArrayList<WorkerObj> wlist=new ArrayList<>();
    public static int count=0;
    public static int buffercount=0;

    public static void main(String[] args) throws IOException {
    //public static void mainMasterServ() throws IOException {



        ServerSocket s=new ServerSocket(1234);

        System.out.println("Master Server running at port " + 1234 + " ...");

        WorkerObj w=new WorkerObj("127.0.0.1", workerPort);

        wlist.add(w);

        Runnable inputThr = new Runnable()
        {
            @Override
            public void run() {
                Scanner sc=new Scanner(System.in);

                while(true)
                {
                    printMenu();

                    int in=sc.nextInt();

                    if(in==1)
                    {

                        System.out.println("Input ip & port of worker to be added");
                        System.out.println("Input the ip address (as in, 172.0.1.1):");
                        String str=sc.next();

                        System.out.println("Input the port:");
                        int port=sc.nextInt();

                        WorkerObj nWorkerObj=new WorkerObj(str,port);
                        addWorkerObjServer(nWorkerObj);

                    }
                    else if(in==2){
                        printWorkerList();
                    }
                    else if(in==3){

                        System.out.println("Input ip & port of worker to be removed");
                        System.out.println("Input the ip address (as in, 172.0.1.1): ");
                        String str=sc.next();

                        System.out.println("Input the port: ");
                        int port=sc.nextInt();
                        WorkerObj nWorkerObj=new WorkerObj(str,port);
                        removeWorkerObjServer(nWorkerObj);
                    }
                }
            }
        };

        Thread t=new Thread(inputThr);
        t.start();


        //initializes another thread class which runs thread

        while(true)
        {

            Socket client= s.accept();
            MasterServThr r=new MasterServThr(client);

        }
    }


    public static void printMenu()
    {
        System.out.println("\nPress 1 to add workers");
        System.out.println("Press 2 to print the active worker obj list");
        //System.out.println("Press 3 to remove workers\n");

    }

    public static  synchronized void addWorkerObjServer(WorkerObj w)
    {
        wlist.add(w);
        System.out.println("Worker Added. To view, press 2.\n");
    }


    public static synchronized void removeWorkerObjServer(WorkerObj w)
    {
        wlist.remove(w);
        System.out.println("Worker Removed. To view, press 2.\n");

    }
    public static  synchronized void printWorkerList()
    {

        System.out.println("**ACTIVE WORKER LIST**");

        for(int i=0;i<wlist.size();i++){
            WorkerObj w=wlist.get(i);
            System.out.println(w.getIp()+" "+w.getPort());
        }
        System.out.println("Current Buffer Count is: "+getBuffer() + "\n");
    }
    public static synchronized WorkerObj getWorkerObj()
    {
        WorkerObj w=wlist.get(count);
        count++;
        if(count==wlist.size())
            count=0;
        return w;
    }

    public static synchronized int getBuffer(){
        return buffercount;
    }

    public static synchronized void setBuffer(int buffer){
        buffercount=buffer;
    }

}
