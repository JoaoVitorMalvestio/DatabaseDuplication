package Models;

import Enum.RouterEnum;
import Threads.LinkRouter;

import java.net.Socket;

public class Primary {
    private RouterEnum routerEnum;
    private Socket socket;
    private LinkRouter linkRouter;

    public Primary(RouterEnum routerEnum, Socket socket, LinkRouter linkRouter) {
        this.routerEnum = routerEnum;
        this.socket = socket;
        this.linkRouter = linkRouter;
    }

    public RouterEnum getRouterEnum() {
        return routerEnum;
    }

    public LinkRouter getLinkRouter() {
        return linkRouter;
    }

    public void setLinkRouter(LinkRouter linkRouter) {
        this.linkRouter = linkRouter;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
