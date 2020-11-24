package Threads;

import Models.Request;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import Enum.RouterEnum;

public class ReceiveMessage extends Thread {

    private Socket socket;
    private RouterEnum routerEnum;

    ReceiveMessage(Socket socket, RouterEnum routerEnum){
        this.socket = socket;
        this.routerEnum = routerEnum;
        this.start();
    }

    public void run() {
        try{
            while(!socket.isClosed()){
                DataInputStream in = new DataInputStream(this.socket.getInputStream());
                Message.resolveBackup(new Request(in.readUTF()), this.socket, this.routerEnum);
            }
        }catch (SocketException e){
            if(e.getMessage().equals("Connection reset")){
                System.out.println("O socket "+ this.routerEnum.description +" se desconectou");
            }
        }catch (IOException e){
            System.out.println("IOException");
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }
}
