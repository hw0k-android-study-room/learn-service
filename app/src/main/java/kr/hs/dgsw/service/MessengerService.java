package kr.hs.dgsw.service;

import android.app.Service;
import android.content.Intent;
import android.os.*;

public class MessengerService extends Service {
    public static final int ECHO = 1;
    public static final int HELLO = 2;
    final Messenger messenger = new Messenger(new Handler(new ServiceHandlerCallback()));
    public MessengerService() { }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    class ServiceHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == ECHO || msg.what == HELLO) {
                Messenger replyMessenger = msg.replyTo;
                try {
                    replyMessenger.send(Message.obtain(null, 1, msg.what == HELLO ? "Hello, " + msg.obj : msg.obj));
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
