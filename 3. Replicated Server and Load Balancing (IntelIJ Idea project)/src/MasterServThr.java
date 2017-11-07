
/**
 * Created by akash on 8/11/17.
 */


import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//maintains a buffer , if full, terminates means drops reqs
// if buffer is not full, sends the int req to worker
//receives the squared result from worker

public class MasterServThr extends MainController implements Runnable {

    Thread t;

    DataInputStream inp;
    DataOutputStream out;

    Socket s1;
    MasterServer s;

    public MasterServThr(Socket s1) throws IOException {

        this.s1=s1;

        inp=new DataInputStream(s1.getInputStream());
        out=new DataOutputStream(s1.getOutputStream());

        t=new Thread(this);
        t.start();

    }
    @Override
    public void run() {
        while(true){

            int in=0;

            try {
                in = inp.readInt();
                if(in==-1){
                    break;
                }

            } catch (IOException ex) {
            //    Logger.getLogger(MasterServThr.class.getName()).log(Level.SEVERE, null, ex);
            }

            int count=MasterServer.getBuffer();


            //MASTERSERVER buffer 50
            if(count > MAXMasterServerBuf ){
                try {

                    System.out.println("Oops! Server Buffer ran out! \n");

                    out.writeInt(-1);


                } catch (IOException ex) {
            //        Logger.getLogger(MasterServThr.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //COMMUNICATION WITH WORKERSERVER
            else{

                MasterServer.setBuffer(MasterServer.getBuffer()+1);
                WorkerObj inservice=MasterServer.getWorkerObj();
                Socket s2;
                try {
                    s2 = new Socket(inservice.getIp(),inservice.getPort());

                    DataInputStream winp=new DataInputStream(s2.getInputStream());
                    DataOutputStream wout=new DataOutputStream(s2.getOutputStream());

                    wout.writeInt(in);

                    int sqval=winp.readInt();
                    out.writeInt(sqval);

                    s2.close();

                } catch (IOException ex) {
                    //Logger.getLogger(MasterServThr.class.getName()).log(Level.SEVERE, null, ex);
                }

                // after finishing buffer is freed
                MasterServer.setBuffer(MasterServer.getBuffer()-1);

            }

        }

    }



}
