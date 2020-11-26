package Models;

import Enum.ClientType;
import Enum.Operation;
import Enum.Action;

import java.util.Arrays;

public class Request {
    private ClientType type;
    private Action action;
    private String data;
    private String from;
    private String to;
    private Operation operation;

    public Request(ClientType type, Action action, String data, String from, String to, Operation operation) {
        this.type = type;
        this.action = action;
        this.data = data;
        this.from = from;
        this.to = to;
        this.operation = operation;
    }

    public Request(String request){
        String[] requestArray = request.split("\\|");

        this.type = ClientType.valueOf(requestArray[0]);
        this.to = requestArray[1];
        this.from = requestArray[2];
        this.action = Action.valueOf(requestArray[3]);
        this.data = requestArray[4];
        this.operation = Operation.valueOf(requestArray[5]);
    }

    public static String send(ClientType type, Action action, String data, String from, String to, Operation operation){
        return type.name()+"|"+to+"|"+from+"|"+action+"|"+data+"|"+operation.name();
    }

    public ClientType getType() {
        return type;
    }

    public Action getAction() {
        return action;
    }

    public String getData() {
        return data;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Operation getOperation() {
        return operation;
    }

    public static Person decodeData(String data){
        Person person = new Person();
        String[] stringFields = data.split("\\$");
        for (String fields: stringFields) {
            String[] field = fields.split("=");
            if(field[0].equals("name")){
                person.setName(field[1]);
            }
        }
        return person;
    }

    public static String encodeData(Person person){
        return person.encodePerson();
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", action='" + action + '\'' +
                ", data='" + data + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", operation=" + operation +
                '}';
    }
}
