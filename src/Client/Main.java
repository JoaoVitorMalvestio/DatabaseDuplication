package Client;

import Client.View.ActionsView;
import Models.Person;
import Models.Request;
import Service.SocketService;
import Threads.LinkRouter;
import java.io.IOException;
import java.util.ArrayList;
import Enum.Action;
import Enum.RouterEnum;
import Enum.ClientType;
import Enum.Operation;

import javax.swing.*;

public class Main {
    private static RouterEnum routerEnum;
    private static ArrayList<LinkRouter> sockets;

    public static void main(String[] args) {
        sockets = new ArrayList<>();
        routerEnum = RouterEnum.valueOf(args[0]);
        linkWithRouter();

        new ActionsView("DatabaseReplication");
    }

    private static void linkWithRouter() {
        for (RouterEnum linkRouter : routerEnum.linkRouters){
            SocketService socketService = new SocketService();
            try{
                socketService.startSocket(linkRouter.routerPort);
                socketService.send(Request.send(ClientType.CLIENT, Action.INITIALIZE, "", Client.Main.routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            } catch (IOException e) {
                e.printStackTrace();
            }
            sockets.add(new LinkRouter(linkRouter, socketService, routerEnum));
        }
    }
    private static boolean resolveInput(String option){
        if(option == null){
            JOptionPane.showMessageDialog(null, "Bye Bye...");
            return true;
        }
        switch (option){
            case "1"/*Inserir*/:
                System.out.println("Vai inserir");
                Person person = new Person();
                person.setName("Michel");
                try {
                    sockets.get(0).getSocketService().send(
                            Request.send(
                                    ClientType.CLIENT,
                                    Action.INSERT,
                                    Request.encodeData(person),
                                    routerEnum.name(),
                                    sockets.get(0).getTo().name(),
                                    Operation.REQUEST
                            )
                    );
                } catch (IOException e) {
                    System.out.println("try catch client");
                    e.printStackTrace();
                }
                break;
            case "2"/*Alterar*/:
                System.out.println("Vai Alterar");
                break;
            case "3"/*Deletar*/:
                System.out.println("Vai deletar");
                break;
            case "4"/*Listar*/:
                System.out.println("Vai Listar");
                break;
            case ""/*Vazio*/:
                System.out.println("Não selecionou nada");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Bye Bye...");
                return true;
        }
        System.out.println(option);
        return false;
    }
}
