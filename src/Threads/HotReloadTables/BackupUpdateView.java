package Threads.HotReloadTables;

import Backup.View.ListPersonView;

import java.time.LocalTime;

public class BackupUpdateView extends Thread {
    ListPersonView view;
    public BackupUpdateView(ListPersonView view) {
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
