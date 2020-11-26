package Threads.HotReloadTables;

import API.View.ListPersonView;
import Enum.Action;
import Enum.Operation;
import Enum.RouterEnum;
import Models.Request;
import Service.SocketService;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalTime;

public class ApiUpdateView extends Thread {
    ListPersonView view;
    public ApiUpdateView(ListPersonView view) {
        this.view = view;
        this.start();
    }

    public void run() {
        LocalTime lastRefresh = null;
        while (true){
            if(lastRefresh == null || lastRefresh.plusSeconds(1).isBefore(LocalTime.now())){
                lastRefresh = LocalTime.now();
                this.view.refreshTable();
            }
        }
    }
}
