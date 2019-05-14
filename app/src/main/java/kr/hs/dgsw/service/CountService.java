package kr.hs.dgsw.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CountService extends Service {
    private final IBinder binder = new CountBinder();
    private int count = 0;
    public CountService() { }
    public int getCount() {
        count++;
        return count;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class CountBinder extends Binder {
        CountService getService() { return CountService.this; }
    }
}
