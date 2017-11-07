/**
 * Created by akash on 8/11/17.
 */

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


//receives input from server (which was sent from the client)
//terminates upon server sending a -1 after job done in N iterations
//otherwise receives the integer which should be squared

// waits 1 sec before delivering the squared result

public class WorkerServThr extends MainController implements Runnable{

    Socket s1;
    Thread t;

    private static int index = 0;


    public WorkerServThr(Socket s1)
    {
        this.s1=s1;
        t=new Thread(this);
        t.start();

    }

    @Override
    public void run() {
        try {
            DataInputStream inp=new DataInputStream(s1.getInputStream());
            DataOutputStream out=new DataOutputStream(s1.getOutputStream());

            //terminates upon client sending -1
            //otherwise receives the integer which should be squared
            int in = inp.readInt();

            if(in==-1){
                System.exit(0);
            }
            System.out.println("Received from Server:" + in);

            workerBuffer[index] = in;
            if (index == MAXWorkerBuf)
            {

                System.out.println("Worker buffer full! Waiting till it's completely empty!\n");

            }

            //else
            //{

                //wait 1 sec
                Thread.sleep(1000);

                //returns squared result
                out.writeInt(in * in);
                //out.writeInt(workerBuffer[index] * workerBuffer[index] );


//            }
        } catch (IOException ex) {

            //Logger.getLogger(WorkerServThr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {


            // Logger.getLogger(WorkerServThr.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            s1.close();
        }
        catch (IOException ex) {

            //Logger.getLogger(WorkerServThr.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}