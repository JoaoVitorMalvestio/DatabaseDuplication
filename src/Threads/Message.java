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
import java.util.Set;
import java.util.stream.Collectors;

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

    private static String resolveClient(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("Conectado a um novo socket de client " + from.description);
            if(to.type == ClientType.PRIMARY){
                Primary.Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            }
            if(to.type == ClientType.API){
                API.Main.clients.add(new Models.Client(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            }
            new ReceiveMessage(socket, from);
            return "";
        }
        if(request.getAction() == Action.INSERT){
            System.out.println("ResolveClient - Chegou Inserção de: " + from.description);
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
                    API.Main.list.add(Person.decodeData(API.Main.response.getData()));
                    return API.Main.response.getData();
                } catch (IOException e) {
                    System.out.println("Error to repassar da Api para Main");
                    return "[resolveClient] Erro";
                }
            }
            return "[resolveClient] Não é API";
        }
        if(request.getAction() == Action.SELECT){
            System.out.println("ResolveClient - Chegou Select de: " + from.description);
            if(to.type == ClientType.API){
                return Request.encodeListData(API.Main.list);
            }
        }
        if(request.getAction() == Action.DELETE){
            System.out.println("ResolveClient - Chegou Delete de: " + from.description);
            if(to.type == ClientType.API){
                try {
                    API.Main.socketPrimary.getLinkRouter().getSocketService().send(
                            Request.send(
                                    ClientType.API,
                                    Action.DELETE,
                                    request.getData(),
                                    to.name(),
                                    API.Main.socketPrimary.getRouterEnum().name(),
                                    Operation.REQUEST
                            )
                    );
                    API.Main.waitingResponse = true;
                    API.Main.waiting();
                    API.Main.list.removeIf(person -> person.getId().toString().equals(request.getData()));
                    System.out.println(API.Main.list.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "[resolveClient] Erro";
    }

    private static String resolveApi(Request request, Socket socket, RouterEnum from, RouterEnum to){
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("[resolveApi] Conectado a um novo socket de client " + to.description);
            Primary.Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            new ReceiveMessage(socket, from);
            return "";
        }
        if(request.getAction() == Action.INSERT){
            System.out.println("[resolveApi] Chegou Inserção de: " + from.description);
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
                        return Request.encodeData(person);
                    } catch (IOException e) {
                        System.out.println("Error to repassar da Api para Main");
                        return "Erro";
                    }
                }
                if(Primary.Main.subscribers.size() > 0){
                    //Precisa repassar para os outros subscribers também.
                }
                return "[resolveApi]Qtd Backups = 0";
            }
            return "[resolveApi]Não é Primário";
        }
        if(request.getAction() == Action.DELETE){
            System.out.println("ResolveApi - Chegou Delete de: " + from.description);
            if(to.type == ClientType.PRIMARY){
                try {
                    SocketService socketService = new SocketService(Primary.Main.backups.get(0).getSocket());
                    socketService.send(
                            Request.send(
                                    ClientType.PRIMARY,
                                    Action.DELETE,
                                    request.getData(),
                                    to.name(),
                                    Primary.Main.backups.get(0).getRouterEnum().name(),
                                    Operation.REQUEST
                            )
                    );
                    Primary.Main.waitingResponse = true;
                    Primary.Main.waiting();
                    Primary.Main.list.removeIf(person -> person.getId().toString().equals(request.getData()));
                    System.out.println(Primary.Main.list.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "[resolveApi]Não é Insert";
    }

    private static String resolvePrimary(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INSERT) {
            System.out.println("[resolvePrimary] Chegou Insert do: "+from.description);
            if(from.type == ClientType.BACKUP){
                Person person = Person.decodeData(request.getData());
                Backup.Main.list.add(person);
                return Request.encodeData(person);
            }
            return "[resolvePrimary]Não é Backup";
        }
        if(request.getAction() == Action.DELETE) {
            if (from.type == ClientType.BACKUP) {
                System.out.println("Vai deletar o de ID ="+request.getData());
                Backup.Main.list.removeIf(person -> person.getId().toString().equals(request.getData()));
                return "";
            }
        }
        return "[resolvePrimary]Não é Insert";
    }

    private static String resolveBackup(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("[resolveBackup] Conectado a um novo socket backup: " + RouterEnum.valueOf(request.getFrom()).description);
            Primary.Main.backups.add(new Models.Backup(RouterEnum.valueOf(request.getFrom()) ,socket));
            new ReceiveMessage(socket, from);
            return "";
        }
        if (request.getAction() == Action.INSERT) {
            System.out.println("[resolveBackup] Chegou Insert do: "+from.description);
            Person person = Person.decodeData(request.getData());
            Backup.Main.list.add(person);
            return Request.encodeData(person);
        }
        return "[resolveBackup]Não é insert nem initialize";
    }

    public void run(){
        try{
            DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());
            if(request.getOperation().equals(Operation.RESPONSE)){
                Message.setWaitingFalseByType(request);
                return;
            }
            String data = Message.resolveByType(request, clientSocket, from, RouterEnum.valueOf(request.getFrom()));
            out.writeUTF(Request.send(request.getType(),request.getAction(),data,request.getFrom(),request.getTo(),Operation.RESPONSE));
        }catch (IOException e){
            System.out.println("Message");
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    static String resolveByType(Request request, Socket clientSocket, RouterEnum from, RouterEnum to){
        String data;
        switch (request.getType()){
            case API:
                data = resolveApi(request, clientSocket, from, to);
                break;
            case PRIMARY:
                data = resolvePrimary(request, clientSocket, from, to);
                break;
            case BACKUP:
                data = resolveBackup(request, clientSocket, from, to);
                break;
            case CLIENT:
                data = resolveClient(request, clientSocket, from, to); //this.from, RouterEnum.valueOf(request.getTo())
                break;
            default:
                System.out.println("[MESSAGE] Mensagem não tratada");
                System.out.println(request);
                data = "[MESSAGE] Mensagem não tratada";
                break;
        }
        return data;
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
