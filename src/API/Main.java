package API;

import API.View.ListPersonView;
import Enum.RouterEnum;
import Enum.ClientType;
import Enum.Operation;
import Enum.Action;
import Models.Client;
import Models.Person;
import Models.Primary;
import Models.Request;
import Service.SocketService;
import Threads.LinkRouter;
import Threads.Message;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static RouterEnum routerEnum;
    private static ServerSocket listenSocket;
    public static ArrayList<Client> clients = new ArrayList<>();
    public static Primary socketPrimary;
    public static List<Person> list = new ArrayList<>();
    public static boolean waitingResponse = false;
    public static Request response;

    public static void main(String[] args) {
        routerEnum = RouterEnum.valueOf(args[0]);
        new ListPersonView("Listagem "+routerEnum.description, list);
        try{
            listenSocket = new ServerSocket(routerEnum.routerPort);
            linkWithRouter();
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
                socketService.send(Request.send(ClientType.API, Action.INITIALIZE, "", API.Main.routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            }catch (ConnectException e){
                System.out.println("Não foi possível conectar no socket: "+ linkRouter.description);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            socketPrimary = new Primary(linkRouter, socketService.getSocket(), new LinkRouter(linkRouter, socketService, routerEnum));
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
    public static void waiting(){
        LocalTime lastPrint = null;
        while (waitingResponse){
            if(lastPrint == null || lastPrint.plusSeconds(1).isBefore(LocalTime.now())){
                lastPrint = LocalTime.now();
                System.out.println("Aguardando...");
            }
        }
    }
}
