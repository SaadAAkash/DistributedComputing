/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cipherserver;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author tishpish
 */
public class CaesarClient
{

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {  
        try
        {  //default port is 1099
            Cipher stub=(Cipher)Naming.lookup("//localhost/tishpish");  
            System.out.println(stub.Caesar());  
            //System.out.println("end");
        }catch(Exception e)
        {
            System.out.println(e.toString());
        }  
    }  
    
}
