package Threads;

import Enum.ClientType;
import Enum.Operation;
import Enum.RouterEnum;
import Enum.Action;
import Models.Request;
import Models.Subscriber;
import Models.Person;
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

    private static void resolveClient(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("Conectado a um novo socket de client " + from.description);
            if(to.type == ClientType.PRIMARY){
                Primary.Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            }
            if(to.type == ClientType.API){
                API.Main.clients.add(new Models.Client(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            }
            new ReceiveMessage(socket, from);
        }
        if(request.getAction() == Action.INSERT){
            System.out.println("ResolveClient - Chegou Inserção de: " + to.description);
            Person person = Person.decodeData(request.getData());
            if(to.type == ClientType.API){
                try {
                    API.Main.socketPrimary.getLinkRouter().getSocketService().send(
                            Request.send(
                                    ClientType.API,
                                    Action.INSERT,
                                    Request.encodeData(person),
                                    to.name(),
                                    API.Main.socketPrimary.getRouterEnum().name(),
                                    Operation.REQUEST
                            )
                    );
                    API.Main.waitingResponse = true;
                    API.Main.waiting();
                    API.Main.list.add(person);
                } catch (IOException e) {
                    System.out.println("Error to repassar da Api para Main");
                }
            }
        }
    }

    private static void resolveApi(Request request, Socket socket, RouterEnum from, RouterEnum to){
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("[resolveApi] Conectado a um novo socket de client " + to.description);
            Primary.Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            new ReceiveMessage(socket, from);
        }
        if(request.getAction() == Action.INSERT){
            System.out.println("[resolveApi] Chegou Inserção de: " + to.description);
            Person person = Person.decodeData(request.getData());
            person.generateId();
            if(to.type == ClientType.PRIMARY){
                if(Primary.Main.backups.size() > 0){
                    try {
                        SocketService socketService = new SocketService(Primary.Main.backups.get(0).getSocket());
                        socketService.send(
                                Request.send(
                                        ClientType.PRIMARY,
                                        Action.INSERT,
                                        Request.encodeData(person),
                                        to.name(),
                                        Primary.Main.backups.get(0).getRouterEnum().name(),
                                        Operation.REQUEST
                                )
                        );
                        Primary.Main.waitingResponse = true;
                        Primary.Main.waiting();
                        Primary.Main.list.add(person);
                    } catch (IOException e) {
                        System.out.println("Error to repassar da Api para Main");
                    }
                }
            }
        }
    }

    private static void resolvePrimary(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INSERT) {
            System.out.println("[resolvePrimary] Chegou Insert do: "+to.description);
            if(from.type == ClientType.BACKUP){
                Person person = Person.decodeData(request.getData());
                Backup.Main.list.add(person);
            }
        }
    }

    private static void resolveBackup(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("[resolveBackup] Conectado a um novo socket backup: " + RouterEnum.valueOf(request.getFrom()).description);
            Primary.Main.backups.add(new Models.Backup(RouterEnum.valueOf(request.getFrom()) ,socket));
            new ReceiveMessage(socket, from);
        }
        if (request.getAction() == Action.INSERT) {
            System.out.println("[resolveBackup] Chegou Insert do: "+to.description);
            Person person = Person.decodeData(request.getData());
            Backup.Main.list.add(person);
        }
    }

    public void run(){
        try{
            DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());
            if(request.getOperation().equals(Operation.RESPONSE)){
                Message.setWaitingFalseByType(request);
                return;
            }
            Message.resolveByType(request, clientSocket, from, RouterEnum.valueOf(request.getFrom()));
            out.writeUTF(Request.send(request.getType(),request.getAction(),request.getData(),request.getFrom(),request.getTo(),Operation.RESPONSE));
        }catch (IOException e){
            System.out.println("Message");
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    static void resolveByType(Request request, Socket clientSocket, RouterEnum from, RouterEnum to){
        switch (request.getType()){
            case API:
                resolveApi(request, clientSocket, from, to);
                break;
            case PRIMARY:
                resolvePrimary(request, clientSocket, from, to);
                break;
            case BACKUP:
                resolveBackup(request, clientSocket, from, to);
                break;
            case CLIENT:
                resolveClient(request, clientSocket, from, to); //this.from, RouterEnum.valueOf(request.getTo())
                break;
            default:
                System.out.println("[MESSAGE] Mensagem não tratada");
                System.out.println(request);
                break;
        }
    }

    static void setWaitingFalseByType(Request request){
        switch (RouterEnum.valueOf(request.getFrom()).type){
            case API:
                if(API.Main.waitingResponse){
                    API.Main.response = request;
                    API.Main.waitingResponse = false;
                }
                break;
            case PRIMARY:
                if(Primary.Main.waitingResponse){
                    Primary.Main.response = request;
                    Primary.Main.waitingResponse = false;
                }
                break;
            case BACKUP:
                if(Backup.Main.waitingResponse){
                    Backup.Main.response = request;
                    Backup.Main.waitingResponse = false;
                }
                break;
            case CLIENT:
                if(Client.Main.waitingResponse){
                    Client.Main.response = request;
                    Client.Main.waitingResponse = false;
                }
                break;
            default:
                System.out.println(request);
                System.out.println("[RECEIVE-MESSAGE] Mensagem não tratada");
                break;
        }
    }
}
