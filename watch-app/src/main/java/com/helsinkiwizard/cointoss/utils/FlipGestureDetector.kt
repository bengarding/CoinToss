package com.helsinkiwizard.cointoss.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs

class FlipGestureDetector(
    context: Context,
    private val onFlipDetected: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastZ: Float = 0f
    private var lastFlipTime: Long = 0
    private val debounceMillis = 500L // prevent multiple triggers too quickly

    fun start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val z = event.values[2]
        val now = System.currentTimeMillis()

        if (abs(z - lastZ) > 10 && (now - lastFlipTime) > debounceMillis) {
            lastFlipTime = now
            onFlipDetected()
        }

        lastZ = z
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}