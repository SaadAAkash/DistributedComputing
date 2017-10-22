/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echo.server.client;


import java.net.*; 
import java.io.*;
import java.math.*;
import java.util.*;
import java.security.*;
/**
 *
 * @author Akash
 */
public class ServerMain extends EchoServerClient {
    
    
    public static void main(String[] args) throws IOException {
        
        //ServerSocket serverSocket = null; 
        
        //Socket clientSocket = null;
        
        try { 
                serverSocket = new ServerSocket(port); 
            } 
        catch (IOException e) 
            { 
                System.err.println("Could not listen on port: " + port); 
                System.exit(1); 
            } 
 
        System.out.println ("Waiting for connection.....");

        try { 
                clientSocket = serverSocket.accept(); 
            } 
        catch (IOException e) { 
                System.err.println("FAILED ON THE SERVER."); 
                System.exit(1); 
            } 

        System.out.println ("Conn successful" + "SERVER IN WAITING NOW" );
        
        System.out.println ();

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); 
        BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream())); 

        String inp; 

        while ((inp = in.readLine()) != null) 
        { 
            if(inp.equals("BEGIN"))
            {
                inp = "Conn established";
                out.println(inp); 
                while ((inp = in.readLine()) != null) 
                { 
                    if (inp.equals("END")) 
                    {
                        inp = "Conn ENDED";
                        out.println(inp); 
                        break; 
                    }
                    else
                    {
                        System.out.println(inp);
                        out.println(inp); 
                    }
                }
            }
        } 

        out.close(); 
        in.close(); 
        clientSocket.close(); 
        serverSocket.close();
    } 

}
