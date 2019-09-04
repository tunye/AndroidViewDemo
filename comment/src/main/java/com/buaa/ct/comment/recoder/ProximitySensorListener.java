package com.buaa.ct.comment.recoder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.ThreadUtils;

public class ProximitySensorListener implements SensorEventListener {

    private SensorManager sensorManager;
    private float maxRange;
    private boolean hasChange; //
    private Sensor sensor; //

    public ProximitySensorListener() {
        init();
    }

    private void init() {//
        try {
            sensorManager = (SensorManager) RuntimeManager.getInstance().getContext().getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            maxRange = sensor.getMaximumRange();
            if (maxRange > 10.0F) {
                maxRange = 10.0F;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void sendMessage() {
        ThreadUtils.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte mode = 0;
                boolean flag = hasChange();
                if (flag)
                    AudioMediaPlayer.getInstance().seekTo(0);
                if (flag)
                    mode = 2;
                AudioFocusChangeManager.updateMode(mode);
            }
        });
    }

    public void registerListener() {//
        try {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void unregisterListener() {//
        try {
            sensorManager.unregisterListener(this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean hasChange() {
        return hasChange;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean flag = true;
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float value = event.values[0];
            boolean v = false;
            if (value < maxRange) {
                v = flag;
            }
            if (hasChange != v) {
                if (value >= maxRange) {
                    flag = false;
                }
                hasChange = flag;
                sendMessage();
            }
        }
    }
}
