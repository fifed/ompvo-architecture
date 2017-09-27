package com.fifed.architecture.datacontroller.interactor.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import static com.fifed.architecture.datacontroller.interactor.utils.InternetConnectionObserver.ConnectionState.DISCONNECTED;
import static com.fifed.architecture.datacontroller.interactor.utils.InternetConnectionObserver.ConnectionState.MOBILE_NETWORK_CONNECTED;
import static com.fifed.architecture.datacontroller.interactor.utils.InternetConnectionObserver.ConnectionState.WIFI_CONNECTED;
import static com.fifed.architecture.datacontroller.interactor.utils.NetworkUtil.NETWORK_STATUS_MOBILE;
import static com.fifed.architecture.datacontroller.interactor.utils.NetworkUtil.NETWORK_STATUS_NOT_CONNECTED;
import static com.fifed.architecture.datacontroller.interactor.utils.NetworkUtil.NETWORK_STATUS_WIFI;
import static com.fifed.architecture.datacontroller.interactor.utils.NetworkUtil.getConnectivityStatusString;

/**
 * Created by Fedir on 09.08.2017.
 */

public class InternetConnectionObserver {
    private BroadcastReceiver receiver;
    private ConnectionListener listener;
    private boolean isStartState = true;
    private int lastStatus;

    public InternetConnectionObserver(Context context, ConnectionListener listener) {
        this.listener = listener;
        createBroadcastReceiver(context);
    }

    private void createBroadcastReceiver(Context context){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isStartState) {
                    isStartState = false;
                    lastStatus = getConnectivityStatusString(context);
                    return;
                }
                int status = getConnectivityStatusString(context);
                if(status != lastStatus) {
                    lastStatus = status;
                    switch (status) {
                        case NETWORK_STATUS_MOBILE:
                            listener.onConnectionStateChanged(MOBILE_NETWORK_CONNECTED);
                            break;
                        case NETWORK_STATUS_WIFI:
                            listener.onConnectionStateChanged(WIFI_CONNECTED);
                            break;
                        case NETWORK_STATUS_NOT_CONNECTED:
                            listener.onConnectionStateChanged(DISCONNECTED);
                            break;
                    }
                }


            }
        };
        context.registerReceiver(receiver, filter);
    }

    public interface ConnectionListener {
        void onConnectionStateChanged(ConnectionState state);
    }

    public enum ConnectionState {
        WIFI_CONNECTED, MOBILE_NETWORK_CONNECTED, DISCONNECTED
    }
}


