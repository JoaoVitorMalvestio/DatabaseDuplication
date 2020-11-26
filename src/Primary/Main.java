package Primary;

import Models.Backup;
import Models.Person;
import Enum.RouterEnum;
import Models.Request;
import Models.Subscriber;
import Primary.View.ListPersonView;
import Threads.Message;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<Person> list = new ArrayList<>();
    public static ArrayList<Subscriber> subscribers = new ArrayList<>();
    public static ArrayList<Backup> backups = new ArrayList<>();
    private static RouterEnum routerEnum;
    private static ServerSocket listenSocket;
    public static boolean waitingResponse = false;
    public static Request response;
    public static int count = 0;


    public static void main(String[] args) {
        routerEnum = RouterEnum.valueOf(args[0]);
        new ListPersonView("Listagem "+routerEnum.description, list);
        try{
            listenSocket = new ServerSocket(routerEnum.routerPort);
            acceptConnections();
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }

    private static void acceptConnections(){
        try{
            while(true) {
                Socket clientSocket = listenSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                String data = in.readUTF();
                new Message(data, clientSocket, routerEnum);
            }
        } catch(IOException e) {
            System.out.println("Accept");
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }
    public static void waiting(){
        LocalTime lastPrint = null;
        while (waitingResponse){
            if(lastPrint == null || lastPrint.plusSeconds(1).isBefore(LocalTime.now())){
                lastPrint = LocalTime.now();
                System.out.println("Aguardando...");
            }
        }
    }
}
