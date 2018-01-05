/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a6_bullyalgonew;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Akash
 */
public class WorkerNodes{
    
    Socket socket;
    String ip;
    int port;
    
    public WorkerNodes(Socket socket, String ip, int port){
            this.socket = socket;
            this.ip = ip;
            this.port = port;
    }
}
