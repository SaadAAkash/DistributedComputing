/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cipherserver;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 *
 * @author tishpish
 */
public class CipherServer
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, RemoteException, Exception
    {
        String hostname = InetAddress.getLocalHost().getHostAddress();
        System.out.println(hostname);
        
        try
        {  
            CipherRemote stub=new CipherRemote();  
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/tishpish",stub);  
        }
        catch(Exception e)
        {
            System.out.println(e);
        }  
    }
    
}
