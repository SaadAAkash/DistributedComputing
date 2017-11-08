/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cipherserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author tishpish
 */
public interface Cipher extends Remote
{
    //Caesar cipher
    public String Caesar()throws RemoteException;  
    public String Vigenere()throws RemoteException;  
}
