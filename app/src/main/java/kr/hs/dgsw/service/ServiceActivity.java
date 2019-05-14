package kr.hs.dgsw.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class ServiceActivity extends AppCompatActivity {

    private Messenger serviceMessenger;
    private boolean isBound;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            serviceMessenger = new Messenger(iBinder);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMessenger = null;
            isBound = false;
        }
    };

    private final Messenger messenger = new Messenger(new Handler(new IncomingHandlerCallback()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    private class IncomingHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void sendMessage(View v) {
        if (isBound) {
            Message message = Message.obtain(null, MessengerService.ECHO, "Hello");
            message.replyTo = messenger;
            try {
                serviceMessenger.send(message);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendHelloMessage(View v) {
        if (isBound) {
            Message message = Message.obtain(null, MessengerService.HELLO, "World");
            message.replyTo = messenger;
            try {
                serviceMessenger.send(message);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}
