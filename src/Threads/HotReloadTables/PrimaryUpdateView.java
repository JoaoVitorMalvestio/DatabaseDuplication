package Threads.HotReloadTables;

import Primary.View.ListPersonView;

import java.time.LocalTime;

public class PrimaryUpdateView extends Thread {
    ListPersonView view;
    public PrimaryUpdateView(ListPersonView view) {
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
