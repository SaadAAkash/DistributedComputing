/**
 * Created by akash on 8/09/17.
 */

import java.net.*;
import java.io.*;
import java.util.*;


//starts server socket in the given port
//runs forever
//accepts clients on new client sockets & opens up a thread each time after client acceptance

public class WorkerServer extends MainController {



    public static void main(String[] args) throws IOException, InterruptedException {

        //Scanner sc=new Scanner(System.in);
        //int port=sc.nextInt();

//        String[] portStr = args;
//        String port = portStr.toString();
//        int portFinal = Integer.parseInt(port);


        System.out.println("Worker Server running at " + workerPort + " ...");
        ServerSocket s = new ServerSocket( workerPort );

        //worker server will go on & on
        while(true)
        {
            Socket client= s.accept();
            WorkerServThr r=new WorkerServThr(client);
        }

    }

    public static  synchronized int getBuffer(){

        return 0;
    }

}