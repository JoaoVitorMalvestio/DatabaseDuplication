package Threads;

import Enum.ClientType;
import Enum.Operation;
import Enum.RouterEnum;
import Enum.Action;
import Models.*;
import Primary.Main;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Message extends Thread{
    private final String data;
    private final RouterEnum from;
    private final Socket clientSocket;

    public Message(String data, Socket clientSocket, RouterEnum from) {
        this.data = data;
        this.clientSocket = clientSocket;
        this.from = from;
        this.start();
    }

    static void resolveClient(Request request, Socket socket, RouterEnum from, RouterEnum to) {
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("["+from.description+"] "+"Conectado a um novo socket de client " + to.description);
            if(from.type == ClientType.PRIMARY){
                Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            }
            if(from.type == ClientType.API){
                API.Main.clients.add(new Client(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            }
            new ReceiveMessage(socket, from);
        }
        if(request.getAction() == Action.INSERT){
            System.out.println("["+from.description+"] "+" Chegou Inserção de: " + to.description);
            Person person = Request.decodeData(request.getData());
            if(from.type == ClientType.API){
//                parei aqui
//                API.Main.sockets.get(0).getSocketService().send(
//                        Request.send(
//                                ClientType.CLIENT,
//                                Action.INSERT,
//                                Request.encodeData(person),
//                                routerEnum.name(),
//                                sockets.get(0).getTo().name(),
//                                Operation.REQUEST
//                        )
//                );
            }
        }
//        SendService sendService = new SendService();
//        if(request.getType().name().equals(ClientType.SUBSCRIBER.name())){
//            System.out.println("Criado novo Inscrito: "+request.getFrom()+" Interesse: "+request.getInterest());
//            Subscriber subscriber = new Subscriber(request.getFrom(), request.getInterest(), socket);
//            TCPServer.subscribers.add(subscriber);
//            sendService.sendSubscriberToRouterConnections(request);
//            return;
//        }
//        System.out.println("Publisher enviou dados para o interesse: "+request.getInterest() + " Mensagem: "+ request.getMessage());
//        sendService.sendMessageToSubscriberByInterest(request);
//        sendService.sendMessageToRouterByInterest(request);
//        if(!request.getType().name().equals(ClientType.PUBLISHER.name())){
//            sendService.sendSubscriberToRouterConnections(request);
//        }
    }

    static void resolveApi(Request request, Socket socket, RouterEnum from, RouterEnum to){
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("["+from.description+"] "+"Conectado a um novo socket de client " + to.description);
            Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            new ReceiveMessage(socket, from);
        }
    }

    static void resolveBackup(Request request, Socket socket, RouterEnum from) {
//        SendService sendService = new SendService();
        if (request.getAction() == Action.INITIALIZE) {
            System.out.println("["+from.description+"] "+"Conectado a um novo socket backup: " + RouterEnum.valueOf(request.getFrom()).description);
            Main.backups.add(new Backup(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            new ReceiveMessage(socket, from);
        }
//        RouterEnum from = RouterEnum.valueOf(request.getFrom());
//        if(request.getType().equals(ClientType.PUBLISHER)){
//            System.out.println("Recebido Mensagem Publisher: "+ request.getMessage()+" - Interesse:"+request.getInterest());
//            sendService.sendMessageToSubscriberByInterest(request);
//            sendService.sendMessageToRouterByInterestWithoutOrigin(request);
//        }else{
//            if(TCPServer.canAddNewRouter(request.getInterest(), from)){
//                System.out.println("[Threads.Message] Adicionado no socket "+request.getFrom()+" um interesse: "+request.getInterest());
//                TCPServer.routers.add(new Router(request.getInterest(), socket, from));
//                sendService.sendSubscriberToRouterConnections(request);
//            }else{
//                System.out.println("Mensagem não mapeada");
//                System.out.println(request.toString());
//            }
//        }
    }

    public void run(){
        try{
            Request request = new Request(this.data);
            DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());

            if(request.getOperation().equals(Operation.RESPONSE)){
                return;
            }

            if(request.getType() == ClientType.BACKUP) {
                resolveBackup(request, this.clientSocket, this.from);
                out.writeUTF(Request.send(request.getType(),request.getAction(),request.getData(),request.getFrom(),request.getTo(),Operation.RESPONSE));
                return;
            }
            if(request.getType() == ClientType.CLIENT) {
                resolveClient(request, clientSocket, this.from, RouterEnum.valueOf(request.getFrom()));
                out.writeUTF(Request.send(request.getType(),request.getAction(),request.getData(),request.getFrom(),request.getTo(),Operation.RESPONSE));
                return;
            }
            if(request.getType() == ClientType.API) {
                resolveApi(request, clientSocket, this.from, RouterEnum.valueOf(request.getFrom()));
                out.writeUTF(Request.send(request.getType(), request.getAction(), request.getData(), request.getFrom(), request.getTo(), Operation.RESPONSE));
                return;
            }
            System.out.println("Mensagem não tratada");
            out.writeUTF(Request.send(request.getType(), request.getAction(), request.getData(), request.getFrom(), request.getTo(), Operation.RESPONSE));
        }catch (IOException e){
            System.out.println("Message");
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }
}
