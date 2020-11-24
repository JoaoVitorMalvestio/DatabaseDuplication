package Threads;

import Models.Request;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import Enum.RouterEnum;
import Enum.ClientType;

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
                String receive = in.readUTF();
                System.out.println(receive);
                Request request = new Request(receive);
                if(request.getType() == ClientType.BACKUP) {
                    Message.resolveBackup(request, this.socket, this.routerEnum);
                }
                if(request.getType() == ClientType.CLIENT) {
                    Message.resolveClient(request, this.socket, this.routerEnum, RouterEnum.valueOf(request.getFrom()));
                }
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
