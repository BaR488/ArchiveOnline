/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DispatcherServices;

/**
 *
 * @author minel
 */
public class RegistrationFailedException extends Exception{
    
    public RegistrationFailedException(){
        
    }
    
    public  RegistrationFailedException(String message){
        super(message);
    }
}
