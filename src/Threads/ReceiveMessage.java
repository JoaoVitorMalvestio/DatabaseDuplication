package Threads;

import Models.Request;
import Enum.Operation;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
                String receive = in.readUTF();
                Request request = new Request(receive);
                if(request.getOperation() == Operation.RESPONSE){
                    Message.setWaitingFalseByType(request);
                    continue;
                }
                String data = Message.resolveByType(request, socket, RouterEnum.valueOf(request.getFrom()), RouterEnum.valueOf(request.getTo()));
                DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
                out.writeUTF(Request.send(request.getType(), request.getAction(), data, request.getFrom(), request.getTo(), Operation.RESPONSE));
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
