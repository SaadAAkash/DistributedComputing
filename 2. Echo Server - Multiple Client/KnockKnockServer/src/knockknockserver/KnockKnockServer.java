/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knockknockserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class KnockKnockServer extends Thread
{
    static ServerSocket serverSocket ;
    static KnockKnockServer knock;
    static boolean noConnection = true;
    static final int PORT = 2017;
    static ArrayList<EchoThread> list;
    public static void main(String args[]) throws IOException 
    {
         knock = new KnockKnockServer();
         knock.start();
    }

    public KnockKnockServer() 
    {
        
    }
    
    public void run()
    {
        try 
        {
            serverSocket = null;
            Socket socket = null;
            list = new ArrayList();
            try
            {
                serverSocket = new ServerSocket(PORT);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                
            }
            while (checkAll())
            {
                System.out.println("checking..");
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }
                // new thread for a client
                EchoThread xx = new EchoThread(socket, knock);
                list.add(xx);
                xx.start();
            }
            
            System.out.println("Closing Server connection");
            serverSocket.close();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(KnockKnockServer.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    
    
    
    public static boolean checkAll() throws IOException
    {
            
        if (noConnection)
            return true;
        boolean allDead = true;
        for (int i=0;i<list.size();i++)
        {
            EchoThread a = list.get(i);
            if (a.isAlive())
                return false;
        }
        serverSocket.close();
        return allDead;
    }
    
}
