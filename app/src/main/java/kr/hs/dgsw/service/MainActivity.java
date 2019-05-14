package kr.hs.dgsw.service;

import android.content.*;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CountService service;
    private boolean isBound = false;
    private Button buttonCount;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            CountService.CountBinder binder = (CountService.CountBinder) iBinder;
            service = binder.getService();
            isBound = true;
            buttonCount.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            buttonCount.setEnabled(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCount = findViewById(R.id.buttonCount);
    }

    public void startService(View v) {
        Intent intent = new Intent(this, SimpleIntentService.class);
        startService(intent);
    }

    public void stopService(View v) {
        Intent intent = new Intent(this, SimpleIntentService.class);
        stopService(intent);
    }

    public void onBindClick(View v) {
        Intent intent = new Intent(this, CountService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void onUnbindClick(View v) {
        if (isBound) {
            isBound = false;
            unbindService(connection);
            buttonCount.setEnabled(false);
        }
    }

    public void onCountClick(View v) {
        if (!isBound) return;
        Toast.makeText(this, "Count = " + service.getCount(), Toast.LENGTH_SHORT).show();
    }

    public void onActivityClick(View v) {
        Intent intent = new Intent(this, ServiceActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
        }
    }
}
