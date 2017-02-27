package com.google.android.libraries.remixer.ui.gesture;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentActivity;

import com.google.android.libraries.remixer.ui.view.RemixerFragment;
/**
 * A Gesture Listener that listens for magnitude of 3D acceleration to exceed a given threshold, triggering the display of a
 * RemixerFragment.
 *
 * <p>It can be set up by calling {@link #attach(FragmentActivity, double threshold, RemixerFragment)}
 */
public class ShakeListener implements SensorEventListener {
    private double lastMagnitude;

    private double threshold;
    private final FragmentActivity activity;
    private RemixerFragment remixerFragment;

    public ShakeListener(final FragmentActivity activity, final double threshold, RemixerFragment remixerFragment) {
      this.activity = activity;
      this.threshold = threshold;
      this.remixerFragment = remixerFragment;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
      double currentMagnitude = Math.sqrt(
          event.values[0] * event.values[0]
          + event.values[1] * event.values[1]
          + event.values[2] * event.values[2]);

      if ((currentMagnitude - lastMagnitude) > threshold ) {
        remixerFragment.showRemixer(activity.getSupportFragmentManager(), RemixerFragment.REMIXER_TAG);
      }

      this.lastMagnitude = currentMagnitude;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    /**
     * Attaches a ShakeListener to {@code activity} that listens for acceleration change that exceeds {@code threshold}
     * and shows {@code remixerFragment} when satisfied.
     */
    public void attach() {
      SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
      sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Detaches {@code SensorListener} {@code this} from the stored activity
     */
    public void detach() {
      ((SensorManager) activity.getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
    }

}