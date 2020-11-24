package Enum;

public enum RouterEnum {
    P1(8002, new RouterEnum[]{}, "Primario", ClientType.PRIMARY),
    B1(8000, new RouterEnum[]{P1}, "Backup", ClientType.BACKUP),
    S1(8003, new RouterEnum[]{P1}, "Api-1", ClientType.API),
    S2(8004, new RouterEnum[]{P1}, "Api-2", ClientType.API),
    C1(8005, new RouterEnum[]{S1}, "Client-1", ClientType.CLIENT),
    C2(8006, new RouterEnum[]{S2}, "Client-2", ClientType.CLIENT);

    public int routerPort;
    public RouterEnum[] linkRouters;
    public String description;
    public ClientType type;

    RouterEnum(int routerPort, RouterEnum[] linkRouters, String description, ClientType type) {
        this.routerPort = routerPort;
        this.linkRouters = linkRouters;
        this.description = description;
        this.type = type;
    }
}