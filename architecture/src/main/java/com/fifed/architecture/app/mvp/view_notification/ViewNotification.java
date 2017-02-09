package com.fifed.architecture.app.mvp.view_notification;

import com.fifed.architecture.app.mvp.view_data_pack.core.DataPack;

/**
 * Created by Fedir on 12.11.2016.
 */

public class ViewNotification {
    private DataPack pack;

    public ViewNotification(DataPack pack) {
        this.pack = pack;
    }

    public DataPack getDataPack() {
        return pack;
    }

    public ViewNotification() {
    }
}
