package Primary;

import Models.Backup;
import Models.Person;
import Enum.RouterEnum;
import Models.Subscriber;
import Threads.Message;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Person> list = new ArrayList<>();
    public static ArrayList<Subscriber> subscribers = new ArrayList<>();
    public static ArrayList<Backup> backups = new ArrayList<>();
    private static RouterEnum routerEnum;
    private static ServerSocket listenSocket;


    public static void main(String[] args) {
        try{
            routerEnum = RouterEnum.valueOf(args[0]);
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
                new Message(data,clientSocket);
            }
        } catch(IOException e) {
            System.out.println("Listen:"+e.getMessage());
        }
    }
}
