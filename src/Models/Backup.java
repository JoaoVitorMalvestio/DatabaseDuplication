package Models;

import java.net.Socket;
import Enum.RouterEnum;

public class Backup {
    private RouterEnum routerEnum;
    private Socket socket;

    public Backup(RouterEnum routerEnum, Socket socket) {
        this.routerEnum = routerEnum;
        this.socket = socket;
    }

    public RouterEnum getRouterEnum() {
        return routerEnum;
    }

    public void setRouterEnum(RouterEnum name) {
        this.routerEnum = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
