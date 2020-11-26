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
    public static boolean waitingResponse = false;
    public static Request response;

    public static void main(String[] args) {
        /*sockets = new ArrayList<>();
        routerEnum = RouterEnum.valueOf(args[0]);
        linkWithRouter();*/

        new ActionsView("DatabaseReplication");
//        menu();
    }

    private static void linkWithRouter() {
        for (RouterEnum linkRouter : routerEnum.linkRouters){
            SocketService socketService = new SocketService();
            try{
                socketService.startSocket(linkRouter.routerPort);
                socketService.send(Request.send(ClientType.CLIENT, Action.INITIALIZE, "", routerEnum.name(), linkRouter.name(), Operation.REQUEST));
            } catch (IOException e) {
                e.printStackTrace();
            }
            sockets.add(new LinkRouter(linkRouter, socketService, routerEnum));
        }
    }

    private static void menu(){
        boolean leave = false;
        while(!leave){
            String option = JOptionPane.showInputDialog(
                    null,
                    "Digite: \n" +
                            "1 - inserir\n" +
                            "2 - Alterar\n" +
                            "3 - Deletar\n" +
                            "4 - Listar\n" +
                            "5 - Sair");
            leave = resolveInput(option);
        }
    }

    public static void createPerson(Person person){
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
            waitingResponse = true;
            waiting();
        } catch (IOException e) {
            System.out.println("try catch client");
            e.printStackTrace();
        }
    }

    public static String getListPerson() {
        return "id=1$name=Joao$phone=999999999$address=Rua das alamedas$numberAddress=1234$zipCode=87047650$neighborhood=Jardim Paris$city=Maringa$state=PR";
/*
        try {
            sockets.get(0).getSocketService().send(
                    Request.send(
                            ClientType.CLIENT,
                            Action.SELECT,
                            "",
                            routerEnum.name(),
                            sockets.get(0).getTo().name(),
                            Operation.REQUEST
                    )
            );

            waitingResponse = true;
            waiting();
            return response.getData();

        } catch (IOException e) {
            System.out.println("try catch client");
            e.printStackTrace();
        }

        return "";*/
    }

    public static void removePerson(Integer index){
        try {
            sockets.get(0).getSocketService().send(
                    Request.send(
                            ClientType.CLIENT,
                            Action.INSERT,
                            index.toString(),
                            routerEnum.name(),
                            sockets.get(0).getTo().name(),
                            Operation.REQUEST
                    )
            );
            waitingResponse = true;
            waiting();
        } catch (IOException e) {
            System.out.println("try catch client");
            e.printStackTrace();
        }
    }

    public static void updatePerson(Person person){
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
            waitingResponse = true;
            waiting();
        } catch (IOException e) {
            System.out.println("try catch client");
            e.printStackTrace();
        }
    }

    public static boolean resolveInput(String option){
        if(option == null){
            JOptionPane.showMessageDialog(null, "Bye Bye...");
            return true;
        }
        switch (option){
            /*Inserir*/
            case "1"/*Inserir*/:
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
                    waitingResponse = true;
                    waiting();
                    System.out.println("Salvo com sucesso");
                } catch (IOException e) {
                    System.out.println("try catch client");
                    e.printStackTrace();
                }
                break;

            /*Alterar*/
            case "2":
                System.out.println("Vai Alterar");
                break;

            /*Deletar*/
            case "3":
                System.out.println("Vai deletar");
                break;

            /*Listar*/
            case "4":
                System.out.println("Vai Listar");
                break;

            /*Vazio*/
            case "":
                System.out.println("Não selecionou nada");
                break;

            default:
                JOptionPane.showMessageDialog(null, "Bye Bye...");
                return true;
        }
        return false;
    }

    public static void waiting(){
        while (waitingResponse){

        }
    }
}
