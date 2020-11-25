package Threads;

import Enum.ClientType;
import Enum.Operation;
import Enum.RouterEnum;
import Enum.Action;
import Models.Request;
import Models.Subscriber;
import Models.Client;
import Models.Person;
import Primary.Main;
import Service.SocketService;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Message extends Thread{
    private final RouterEnum from;
    private final RouterEnum to;
    private final Request request;
    private final Socket clientSocket;

    public Message(String data, Socket clientSocket, RouterEnum to) {
        this.clientSocket = clientSocket;
        this.to = to;
        this.request = new Request(data);
        this.from = RouterEnum.valueOf(this.request.getFrom());
        this.start();
    }

    static void resolveClient(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("Conectado a um novo socket de client " + from.description);
            if(to.type == ClientType.PRIMARY){
                Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            }
            if(to.type == ClientType.API){
                API.Main.clients.add(new Client(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            }
            new ReceiveMessage(socket, from);
        }
        if(request.getAction() == Action.INSERT){
            System.out.println("ResolveClient - Chegou Inserção de: " + to.description);
            Person person = Request.decodeData(request.getData());
            if(to.type == ClientType.API){
                try {
                    API.Main.socketPrimary.getLinkRouter().getSocketService().send(
                            Request.send(
                                    ClientType.API,
                                    Action.INSERT,
                                    Request.encodeData(person),
                                    from.name(),
                                    API.Main.socketPrimary.getRouterEnum().name(),
                                    Operation.REQUEST
                            )
                    );
                } catch (IOException e) {
                    System.out.println("Error to repassar da Api para Main");
                }
            }
        }
    }

    static void resolveApi(Request request, Socket socket, RouterEnum from, RouterEnum to){
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("[resolveApi] Conectado a um novo socket de client " + to.description);
            Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            new ReceiveMessage(socket, from);
        }
        if(request.getAction() == Action.INSERT){
            System.out.println("[resolveApi] Chegou Inserção de: " + to.description);
            Person person = Request.decodeData(request.getData());
            person.generateId();
            if(to.type == ClientType.PRIMARY){
                if(Main.backups.size() > 0){
                    try {
                        SocketService socketService = new SocketService(Main.backups.get(0).getSocket());
                        socketService.send(
                                Request.send(
                                        ClientType.PRIMARY,
                                        Action.INSERT,
                                        Request.encodeData(person),
                                        to.name(),
                                        Main.backups.get(0).getRouterEnum().name(),
                                        Operation.REQUEST
                                )
                        );
//                        System.out.println(socketService.receive());
                    } catch (IOException e) {
                        System.out.println("Error to repassar da Api para Main");
                    }
                }
            }
        }
    }

    static void resolvePrimary(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INSERT) {
            System.out.println("[resolvePrimary] Chegou Insert do: "+to.description);
            if(from.type == ClientType.BACKUP){
                Backup.Main.list.add(Request.decodeData(request.getData()));
            }
        }
    }

    static void resolveBackup(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("[resolveBackup] Conectado a um novo socket backup: " + RouterEnum.valueOf(request.getFrom()).description);
            Main.backups.add(new Models.Backup(RouterEnum.valueOf(request.getFrom()) ,socket));
            new ReceiveMessage(socket, from);
        }
        if (request.getAction() == Action.INSERT) {
            System.out.println("[resolveBackup] Chegou Insert do: "+to.description);
            Backup.Main.list.add(Request.decodeData(request.getData()));
        }
    }

    public void run(){
        try{
            DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());

            if(request.getOperation().equals(Operation.RESPONSE)){
                return;
            }
            switch (request.getType()){
                case API:
                    resolveApi(request, clientSocket, this.from, RouterEnum.valueOf(request.getFrom()));
                    break;
                case PRIMARY:
                    resolvePrimary(request, clientSocket, this.from, RouterEnum.valueOf(request.getFrom()));
                    break;
                case BACKUP:
                    resolveBackup(request, this.clientSocket, this.from, RouterEnum.valueOf(request.getFrom()));
                    break;
                case CLIENT:
                    resolveClient(request, clientSocket, this.from, RouterEnum.valueOf(request.getTo()));
                    break;
                default:
                    System.out.println("[MESSAGE] Mensagem não tratada");
                    System.out.println(request);
                    break;
            }
            out.writeUTF(Request.send(request.getType(),request.getAction(),request.getData(),request.getFrom(),request.getTo(),Operation.RESPONSE));
        }catch (IOException e){
            System.out.println("Message");
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }
}
