
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
public class EchoServerClient {

    public int a,b,c;
    private float af,bf,cf;
    private String str1, str2, str3;
    
    static public int port = 10007;
    
    
    //for: server
        static ServerSocket serverSocket = null; 
        static Socket clientSocket = null;
    
    //for: client
    
        static String serverResponse;
	static String userInput;
        static Socket echoSocket = null;
        
    
    
}
