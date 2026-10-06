package com.example.activefit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class StepCounterManager implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private OnStepChangedListener listener;

    public interface OnStepChangedListener {
        void onStepChanged(int totalSteps);
    }


    public StepCounterManager(Context context, OnStepChangedListener listener) {

        this.listener = listener;

        sensorManager =
                (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }
    }

    public boolean isSensorAvailable() {
        return stepSensor != null;
    }

    public void start() {

        if (stepSensor != null) {

            sensorManager.registerListener(
                    this,
                    stepSensor,
                    SensorManager.SENSOR_DELAY_UI
            );
        }
    }

    public void stop() {

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            int totalSteps = (int) event.values[0];

            android.util.Log.d("STEP_SENSOR", "Steps = " + totalSteps);

            if (listener != null) {
                listener.onStepChanged(totalSteps);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}