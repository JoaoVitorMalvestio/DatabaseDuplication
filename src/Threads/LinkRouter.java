package Threads;

import Enum.Operation;
import Enum.RouterEnum;
import Enum.Action;
import Models.Request;
import Service.SocketService;
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
                if (request.getOperation().equals(Operation.REQUEST)) {
                    System.out.println("Chegou Request: " + to.description + " link router: " + request.getData());
                }
                if (request.getOperation().equals(Operation.RESPONSE)) {
//                    System.out.println("Chegou Resposta: " + linkRouter.description + " link router: " + request.getMessage());
                    if (request.getAction() == Action.INITIALIZE) {
                        System.out.println("["+RouterEnum.valueOf(request.getFrom()).description+"] "+"O socket " + to.description + " aceitou a conexão ");
                    }
                    continue;
                }
//                SendService sendService = new SendService();
//                sendService.sendMessageToRouterByInterestWithoutOrigin(request);
//                if(request.getType().name().equals("PUBLISHER")){
//                    sendService.sendMessageToSubscriberByInterest(request);
//                }
//                if (!request.getType().name().equals("PUBLISHER")) {
//                    sendService.sendSubscriberToRouterConnections(request);
//                    System.out.println("[Link Models.Router] Adicionado no socket " + request.getFrom() + " um interesse: " + request.getInterest());
//                    TCPServer.routers.add(new Router(request.getInterest(), socketService.getSocket(), RouterEnum.valueOf(request.getFrom())));
//                }
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
