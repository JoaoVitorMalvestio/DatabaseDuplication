package Enum;

public enum RouterEnum {
    P1(8002, new RouterEnum[]{}),
    B1(8000, new RouterEnum[]{P1}),
    C1(8003, new RouterEnum[]{P1}),
    C2(8004, new RouterEnum[]{}),
    W1(8005, new RouterEnum[]{}),
    W2(8006, new RouterEnum[]{});

    public int routerPort;
    public RouterEnum[] linkRouters;

    RouterEnum(int routerPort, RouterEnum[] linkRouters) {
        this.routerPort = routerPort;
        this.linkRouters = linkRouters;
    }
}