package Client;

import Models.Request;
import Service.SocketService;
import Threads.LinkRouter;
import java.io.IOException;
import Enum.RouterEnum;
import Enum.ClientType;
import Enum.Operation;

public class Main {
    private static RouterEnum routerEnum;

    public static void main(String[] args) {
        routerEnum = RouterEnum.valueOf(args[0]);
        linkWithRouter();
    }

    private static void linkWithRouter() {
        for (RouterEnum linkRouter : routerEnum.linkRouters){
            SocketService socketService = new SocketService();
            try{
                socketService.startSocket(linkRouter.routerPort);
                socketService.send(Request.send(ClientType.CLIENT, "", "Initialize", Client.Main.routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            } catch (IOException e) {
                e.printStackTrace();
            }
            new LinkRouter(linkRouter, socketService);
        }
    }
}
