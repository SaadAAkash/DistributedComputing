import java.io.IOException;
import java.util.Scanner;

/**
 * Created by akash on 8/19/17.
 */
public class MainController
{
        //1st WorkerServer, inp: 1233
        // 2nd masterSserver, inp: 1 for..., 2 for show buffer & ip
        //client main

    public static int numOfIntegers = 50; //how many numbers each client will request for to be squared (1 to 50)

    public static int MAXMasterServerBuf = 50;

    public  static int MAXWorkerBuf = 10;

    public static int workerBuffer[] = new int[MAXWorkerBuf];

    public static int workerPort = 1233;

//    public static void main(String[] args) throws IOException, InterruptedException
//    {
//
//        Scanner sc=new Scanner(System.in);
//        workerPort=sc.nextInt();
////
////        WorkerServer ws = new WorkerServer();
////        ws.mainWorker();
////
////        MasterServer ms = new MasterServer();
////        ms.mainMasterServ();
////
////        ClientMain cl = new ClientMain();
////        cl.mainClient();
//
//    }


}
