package API;

import Enum.RouterEnum;
import Enum.ClientType;
import Enum.Operation;
import Enum.Action;
import Models.Request;
import Service.SocketService;
import Threads.LinkRouter;
import Threads.Message;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static RouterEnum routerEnum;
    private static ServerSocket listenSocket;

    public static void main(String[] args) {
        try{
            routerEnum = RouterEnum.valueOf(args[0]);
            listenSocket = new ServerSocket(routerEnum.routerPort);
            linkWithRouter();
//            acceptConnections();
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }

    private static void linkWithRouter() {
        for (RouterEnum linkRouter : routerEnum.linkRouters){
            SocketService socketService = new SocketService();
            try{
                socketService.startSocket(linkRouter.routerPort);
                socketService.send(Request.send(ClientType.API, Action.INITIALIZE, "", API.Main.routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            }catch (ConnectException e){
                System.out.println("["+routerEnum.description+"] "+"Não foi possível conectar no socket: "+ linkRouter.description);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            new LinkRouter(linkRouter, socketService, routerEnum);
        }
    }

    private static void acceptConnections(){
        try{
            while(true) {
                Socket clientSocket = listenSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                String data = in.readUTF();
                new Message(data, clientSocket, routerEnum);
            }
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }
}
