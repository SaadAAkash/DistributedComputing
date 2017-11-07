/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Admin
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) 
    {
        String line ;
        Scanner sc = new Scanner(System.in);
        
        /*while (sc.hasNext())
        {
            line = sc.nextLine();
            if (line.matches("BEGIN"))
            {*/
                System.out.println("Begining Connection");
                startConnector();
           /* }
        }*/
    }  
    
    public static void startConnector()
    {
        String hostname = "localhost";
        int port = 2017;

        Socket clientSocket = null;  
        DataOutputStream os = null;
        BufferedReader is = null;

        
        try 
        {
            clientSocket = new Socket(hostname, port);
            os = new DataOutputStream(clientSocket.getOutputStream());
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } 
        
        catch (Exception e) 
        {
            System.err.println(e);
        }

        if (clientSocket == null || os == null || is == null) 
        {
            System.err.println( "Something is wrong. One variable is null." );
            return;
        }
        else
        {
            System.out.println("Connection Established");
        }

        try 
        {
            //String response = is.readLine();
            //System.out.println( response);
            String responseLine = "";
            int line = 0;
            while ( true ) 
            {
                responseLine = is.readLine();
                System.out.println("" + responseLine);
                line++;
                
                if (responseLine.contains("like to listen to"))
                {
                    line++;
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String keyboardInput = br.readLine();
                    os.writeBytes( keyboardInput + "\n" );
                    
                   
                    if ( keyboardInput.matches("N")) 
                    {
                        break;
                    }
                    else
                    {
                        line = 0;
                    }
                }
                else if (responseLine.contains("are supposed to say"))
                {
                    line = 0;
                }
                else
                {
                    line++;
                    if (line<5)
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String keyboardInput = br.readLine();
                        os.writeBytes( keyboardInput + "\n" );
                    }
                    
                }
                
            }

            System.out.println("Closing connection");
            os.close();
            is.close();
            clientSocket.close();   
        } 
        catch (Exception e) 
        {
            System.err.println( e);
        } 
        
    }
}