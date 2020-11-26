package Backup;

import Backup.View.ListPersonView;
import Enum.RouterEnum;
import Enum.ClientType;
import Enum.Operation;
import Enum.Action;
import Models.Person;
import Models.Request;
import Service.SocketService;
import Threads.LinkRouter;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static RouterEnum routerEnum;
    public static List<Person> list = new ArrayList<>();
    public static boolean waitingResponse = false;
    public static Request response;

    public static void main(String[] args) {
        routerEnum = RouterEnum.valueOf(args[0]);
//        ListPersonView.main(args);
        linkWithRouter();
    }

    private static void linkWithRouter() {
        for (RouterEnum linkRouter : routerEnum.linkRouters){
            SocketService socketService = new SocketService();
            try{
                socketService.startSocket(linkRouter.routerPort);
                socketService.send(Request.send(ClientType.BACKUP, Action.INITIALIZE, "", Main.routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            }catch (ConnectException e){
                System.out.println("Não foi possível conectar no socket: "+ linkRouter.description);
                return;
            }catch (IOException e) {
                e.printStackTrace();
                return;
            }
            new LinkRouter(linkRouter, socketService, routerEnum);
        }
    }
}
