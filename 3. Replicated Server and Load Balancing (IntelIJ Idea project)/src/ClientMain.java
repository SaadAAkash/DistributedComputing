/**
 * Created by akash on 8/09/17.
 */


import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

//Master Server runs at port 1234, so it connects to master server, not workers
//worker run at port 1233
//runes two threads
//one thread for sending integer reqs to masterserver
//another for receiving the result from masterserver
//uses join method for waiting for threads to die

public class ClientMain extends MainController
{


    public static void main(String[] args) throws IOException, InterruptedException {
    //public static void mainClient() throws IOException, InterruptedException {


        System.out.println("Client running ...");

        Socket s1=new Socket("127.0.0.1",1234);

        //Master Server runs at port 1234, so it connects to master server, not workers
        //worker run at port 1233

        DataOutputStream out=new DataOutputStream(s1.getOutputStream());
        DataInputStream inp=new DataInputStream(s1.getInputStream());


        //SENDER THREAD

        Runnable r=new Runnable() {

            @Override
            public void run() {
                for(int i=1;i<=50;i++)
                {
                    try {

                        //SENDS INTEGER REQ TO SERVER

                        out.writeInt(i);
                        System.out.println("SENDING REQ:" + i);

                    } catch (IOException ex) {
                        //Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        };

        Thread t=new Thread(r);
        t.start();


        ////////RECEIVER THREAD
        Runnable r1=new Runnable() {

            @Override
            public void run() {
                for(int i=1;i<= numOfIntegers; i++)
                {
                    int in=0;

                    try {

                        in = inp.readInt();
                        System.out.println("RECEIVED RESULT:" + in);

                    } catch (IOException ex) {
                        //Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                try {
                    out.writeInt(-1);
                } catch (IOException ex) {
                    //Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Thread t1=new Thread(r1);
        t1.start();


        //JOIN -> WAIT FOR THREADS TO DIE
        t.join();
        t1.join();

        System.out.println(" t status = " + t.isAlive() + "\n");
        System.out.println(" t1 status = " + t1.isAlive() + "\n");


    }
}