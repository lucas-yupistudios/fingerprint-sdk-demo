package com.example.fingerprintsdkdemo

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.biopassid.fingerprintsdk.engine.FingerprintCaptureListener
import br.com.biopassid.fingerprintsdk.enums.FingerprintCaptureState
import br.com.biopassid.fingerprintsdk.ui.view.FingerprintView

class CameraActivity : AppCompatActivity() {
    private lateinit var fingerprint: FingerprintView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        fingerprint = findViewById(R.id.fingerprint)

        // Handle Fingerprint callback
        fingerprint.setCaptureListener(object : FingerprintCaptureListener {
            override fun onFingerCapture(images: List<Bitmap>) {
                Person.fingers = images
                setResult(RESULT_OK)
                finish()
            }

            override fun onCaptureCanceled() {}
            override fun onStatusChanged(state: FingerprintCaptureState) {}
            override fun onFingerDetected(displayFingerRects: Array<Rect>) {}
            override fun onClassificationChanged(classif: String) {}
        })
    }
}