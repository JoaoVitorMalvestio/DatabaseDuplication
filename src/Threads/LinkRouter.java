package Threads;

import Client.Main;
import Enum.Operation;
import Enum.RouterEnum;
import Enum.Action;
import Enum.ClientType;
import Models.Request;
import Service.SocketService;
import java.io.DataOutputStream;
import java.io.IOException;

public class LinkRouter extends Thread {

    private RouterEnum to;
    private RouterEnum from;
    private SocketService socketService;
    private Request request;

    public LinkRouter(RouterEnum to, SocketService socketService, RouterEnum from) {
        this.to = to;
        this.from = from;
        this.socketService = socketService;
        this.start();
    }

    public void run() {
        try {
            while (!socketService.isClosed()) {
                request = new Request(socketService.receive());
                DataOutputStream out = new DataOutputStream(socketService.getSocket().getOutputStream());
                if (request.getOperation().equals(Operation.RESPONSE)) {
                    System.out.println("[Link-Router] Response: "+request);
                    if (request.getAction() == Action.INITIALIZE) {
                        System.out.println("O socket " + to.description + " aceitou a conexão ");
                        continue;
                    }
                    if(RouterEnum.valueOf(request.getFrom()).type == ClientType.CLIENT){
                        if(Main.waitingResponse){
                            Main.response = request;
                            Main.waitingResponse = false;
                        }
                    }
                    continue;
                }
                switch (request.getType()){
                    case API:
                        Message.resolveApi(request, socketService.getSocket(), RouterEnum.valueOf(request.getTo()), RouterEnum.valueOf(request.getFrom()));
                        break;
                    case PRIMARY:
                        Message.resolvePrimary(request, socketService.getSocket(), RouterEnum.valueOf(request.getTo()), RouterEnum.valueOf(request.getFrom()));
                        break;
                    case BACKUP:
                        Message.resolveBackup(request, socketService.getSocket(), RouterEnum.valueOf(request.getTo()), RouterEnum.valueOf(request.getFrom()));
                        break;
                    case CLIENT:
                        Message.resolveClient(request, socketService.getSocket(), RouterEnum.valueOf(request.getTo()), RouterEnum.valueOf(request.getFrom()));
                        break;
                    default:
                        System.out.println("[LINK-ROUTER] Mensagem não tratada");
                        System.out.println(request);
                        break;
                }
                System.out.println("[Link-Router] Send response");
                out.writeUTF(Request.send(request.getType(),request.getAction(),request.getData(),request.getFrom(),request.getTo(),Operation.RESPONSE));
            }
        } catch (IOException e) {
            System.out.println("O socket "+ this.to.description  + " se desconectou");
            System.out.println("Desligando o "+ from.description+" ...");
        }
    }

    public RouterEnum getTo() {
        return to;
    }

    public RouterEnum getFrom() {
        return from;
    }

    public SocketService getSocketService() {
        return socketService;
    }

    public Request getRequest() {
        return request;
    }

}
