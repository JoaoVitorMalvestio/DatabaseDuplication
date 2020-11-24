package Enum;

public enum RouterEnum {
    P1(8002, new RouterEnum[]{}, "Primario"),
    B1(8000, new RouterEnum[]{P1}, "Backup"),
    S1(8003, new RouterEnum[]{P1}, "Api-1"),
    S2(8004, new RouterEnum[]{P1}, "Api-2"),
    C1(8005, new RouterEnum[]{S1}, "Client-1"),
    C2(8006, new RouterEnum[]{S2}, "Client-2");

    public int routerPort;
    public RouterEnum[] linkRouters;
    public String description;

    RouterEnum(int routerPort, RouterEnum[] linkRouters, String description) {
        this.routerPort = routerPort;
        this.linkRouters = linkRouters;
        this.description = description;
    }
}