package Threads;

import Enum.ClientType;
import Enum.Operation;
import Enum.RouterEnum;
import Models.Backup;
import Models.Request;
import Models.Subscriber;
import Primary.Main;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Message extends Thread{
    private final String data;
    private final Socket clientSocket;

    public Message(String data, Socket clientSocket) {
        this.data = data;
        this.clientSocket = clientSocket;
        this.start();
    }

    private static void resolveClient(Request request, Socket socket) {
        if (request.getMessage().equals("Initialize")) {
            System.out.println("Conectado a um novo socket de client " + RouterEnum.valueOf(request.getFrom()).description);
            Main.subscribers.add(new Subscriber(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            new ReceiveMessage(socket, RouterEnum.valueOf(request.getFrom()));
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

    static void resolveBackup(Request request, Socket socket) {
//        SendService sendService = new SendService();
        if (request.getMessage().equals("Initialize")) {
            System.out.println("Conectado a um novo socket backup: " + RouterEnum.valueOf(request.getFrom()).description);
            Main.backups.add(new Backup(RouterEnum.valueOf(request.getFrom()).name() ,socket));
            new ReceiveMessage(socket, RouterEnum.valueOf(request.getFrom()));
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

            if(request.getType().name().equals(ClientType.BACKUP.name())) {
                resolveBackup(request, this.clientSocket);
                out.writeUTF(Request.send(request.getType(),request.getInterest(),request.getMessage(),request.getFrom(),request.getTo(),Operation.RESPONSE));
                return;
            }

            resolveClient(request, clientSocket);
            out.writeUTF(Request.send(request.getType(),request.getInterest(),request.getMessage(),request.getFrom(),request.getTo(),Operation.RESPONSE));
        }catch (IOException e){
            System.out.println("Message");
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }
}
