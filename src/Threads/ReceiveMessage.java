package Threads;

import Models.Request;
import Enum.Operation;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
                Request request = new Request(receive);
                if(request.getOperation() == Operation.RESPONSE){
                    System.out.println("[Receive-Message] Response: "+request);
                    return;
                }
                switch (request.getType()){
                    case API:
                        Message.resolveApi(request, this.socket, RouterEnum.valueOf(request.getFrom()), RouterEnum.valueOf(request.getTo()));
                        break;
                    case PRIMARY:
                        Message.resolvePrimary(request, this.socket, this.routerEnum, RouterEnum.valueOf(request.getFrom()));
                        break;
                    case BACKUP:
                        Message.resolveBackup(request, this.socket, this.routerEnum, RouterEnum.valueOf(request.getFrom()));
                        break;
                    case CLIENT:
                        Message.resolveClient(request, this.socket, RouterEnum.valueOf(request.getFrom()), RouterEnum.valueOf(request.getTo()));
                        break;
                    default:
                        System.out.println(request);
                        System.out.println("[RECEIVE-MESSAGE] Mensagem não tratada");
                        break;
                }
                sendResponse(request);
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
    private void sendResponse(Request request){
        DataOutputStream out;
        try {
            out = new DataOutputStream(this.socket.getOutputStream());
            out.writeUTF(Request.send(request.getType(), request.getAction(), request.getData(), request.getFrom(), request.getTo(), Operation.RESPONSE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
