package API;

import Enum.RouterEnum;
import Enum.ClientType;
import Enum.Operation;
import Models.Request;
import Service.SocketService;
import Threads.LinkRouter;
import Threads.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static RouterEnum routerEnum;
    private static ServerSocket listenSocket;

    public static void main(String[] args) {
        try{
            routerEnum = RouterEnum.valueOf(args[0]);
            listenSocket = new ServerSocket(routerEnum.routerPort);
            acceptConnections();
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }

    private static void linkWithRouter() {
        for (RouterEnum linkRouter : routerEnum.linkRouters){
            SocketService socketService = new SocketService();
            try{
                socketService.startSocket(linkRouter.routerPort);
                socketService.send(Request.send(ClientType.API, "", "Initialize", API.Main.routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            } catch (IOException e) {
                e.printStackTrace();
            }
            new LinkRouter(linkRouter, socketService);
        }
    }

    private static void acceptConnections(){
        try{
            while(true) {
                Socket clientSocket = listenSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                String data = in.readUTF();
                new Message(data,clientSocket);
            }
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }
}
