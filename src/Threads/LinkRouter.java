package Threads;

import Enum.Operation;
import Enum.RouterEnum;
import Models.Request;
import Service.SocketService;
import java.io.IOException;

public class LinkRouter extends Thread {

    private RouterEnum linkRouter;
    private SocketService socketService;
    private Request request;

    public LinkRouter(RouterEnum linkRouter, SocketService socketService) {
        this.linkRouter = linkRouter;
        this.socketService = socketService;
        this.start();
    }

    public void run() {
        try {
            while (!socketService.isClosed()) {
                request = new Request(socketService.receive());
                if (request.getOperation().equals(Operation.REQUEST)) {
                    System.out.println("Chegou Request: " + linkRouter.description + " link router: " + request.getMessage());
                }
                if (request.getOperation().equals(Operation.RESPONSE)) {
//                    System.out.println("Chegou Resposta: " + linkRouter.description + " link router: " + request.getMessage());
                    if (request.getMessage().equals("Initialize")) {
                        System.out.println("O socket " + linkRouter.description + " se conectou com o socket " + RouterEnum.valueOf(request.getFrom()).description);
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
            e.printStackTrace();
        }
    }
}
