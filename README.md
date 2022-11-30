# BioPass ID Fingerprint SDK Demo

# Using Activity

```Kotlin
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import br.com.biopassid.fingerprintsdk.Fingerprint
import br.com.biopassid.fingerprintsdk.FingerprintConfig
import br.com.biopassid.fingerprintsdk.engine.FingerprintCaptureListener
import br.com.biopassid.fingerprintsdk.enums.FingerprintCaptureState
import com.example.fingerprintsdkdemo.model.EnrollPersonRequest
import com.example.fingerprintsdkdemo.model.EnrollPersonResponse
import com.example.fingerprintsdkdemo.model.FingerPersonRequest
import com.example.fingerprintsdkdemo.model.PersonRequest
import com.example.fingerprintsdkdemo.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private val TAG = "FingerDemo"
    private lateinit var btnFingerCapture: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFingerCapture = findViewById(R.id.btnFingerCapture)

        // Instantiate Fingerprint config by passing your license key
        val config = FingerprintConfig()
            .setLicenseKey("your-license-key")

        // Handle Fingerprint callback
        val callback = object : FingerprintCaptureListener {
            override fun onFingerCapture(images: List<Bitmap>) {
                // Encode Bitmap to base64 string
                val finger1 = bitmapToBas64(images[0])
                val finger2 = bitmapToBas64(images[1])
                val finger3 = bitmapToBas64(images[2])
                val finger4 = bitmapToBas64(images[3])

                // Instantiate Enroll request
                val enrollPersonRequest =
                    EnrollPersonRequest(
                        PersonRequest(
                            "your-customID",
                            listOf(FingerPersonRequest(finger1, finger2, finger3, finger4))
                        )
                    )

                // Get retrofit
                val retrofit = Network.getRetrofitInstance()

                // Execute request to the BioPass ID API
                val callback = retrofit.enrollPerson(enrollPersonRequest)

                // Handle API response
                callback.enqueue(object : Callback<EnrollPersonResponse> {
                    override fun onFailure(call: Call<EnrollPersonResponse>, t: Throwable) {
                        Log.e(TAG, "Error trying to call enroll person. ${t.message}")
                    }

                    override fun onResponse(
                        call: Call<EnrollPersonResponse>,
                        response: Response<EnrollPersonResponse>
                    ) {
                        Log.d(TAG, "EnrollPersonResponse: ${response.body()}")
                    }
                })
            }

            override fun onCaptureCanceled() {}
            override fun onStatusChanged(state: FingerprintCaptureState) {}
            override fun onFingerDetected(displayFingerRects: Array<Rect>) {}
            override fun onClassificationChanged(classif: String) {}
        }

        // Build Fingerprint camera view
        btnFingerCapture.setOnClickListener {
            Fingerprint.buildCameraView(this, config, callback)
        }
    }

    private fun bitmapToBas64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        stream.close()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}
```

# Using Fingerprint View

```Kotlin
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.example.fingerprintsdkdemo.model.EnrollPersonRequest
import com.example.fingerprintsdkdemo.model.EnrollPersonResponse
import com.example.fingerprintsdkdemo.model.FingerPersonRequest
import com.example.fingerprintsdkdemo.model.PersonRequest
import com.example.fingerprintsdkdemo.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private val TAG = "FingerDemo"
    private lateinit var btnFingerCapture: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFingerCapture = findViewById(R.id.btnFingerCapture)

        // Register ActivityForResult
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (Person.fingers.isNotEmpty()) {
                    // Encode Bitmap to base64 string
                    val finger1 = bitmapToBas64(Person.fingers[0])
                    val finger2 = bitmapToBas64(Person.fingers[1])
                    val finger3 = bitmapToBas64(Person.fingers[2])
                    val finger4 = bitmapToBas64(Person.fingers[3])

                    // Instantiate Enroll request
                    val enrollPersonRequest =
                        EnrollPersonRequest(
                            PersonRequest(
                                "your-customID",
                                listOf(FingerPersonRequest(finger1, finger2, finger3, finger4))
                            )
                        )

                    // Get retrofit
                    val retrofit = Network.getRetrofitInstance()

                    // Execute request to the BioPass ID API
                    val callback = retrofit.enrollPerson(enrollPersonRequest)

                    // Handle API response
                    callback.enqueue(object : Callback<EnrollPersonResponse> {
                        override fun onFailure(call: Call<EnrollPersonResponse>, t: Throwable) {
                            Log.e(TAG, "Error trying to call enroll person. ${t.message}")
                        }

                        override fun onResponse(
                            call: Call<EnrollPersonResponse>,
                            response: Response<EnrollPersonResponse>
                        ) {
                            Log.d(TAG, "EnrollPersonResponse: ${response.body()}")
                        }
                    })
                }
            }
        }

        // Start CameraActivity
        btnFingerCapture.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun bitmapToBas64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        stream.close()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}
```

## CameraActivity XML

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".CameraActivity">

    <br.com.biopassid.fingerprintsdk.ui.view.FingerprintView
        android:id="@+id/fingerprint"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:captureType="leftHandFingers"
        app:fingersToCapture="4"
        app:licenseKey="your-license-key"
        app:outputType="captureAndSegmentation"
        app:showDistanceIndicatorView="true"
        app:showFingersIndicatorView="true"
        app:showStatusView="true" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## CameraActivity

```Kotlin
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
```

## Person Object

```Kotlin
import android.graphics.Bitmap
import java.util.ArrayList

object Person {
    var fingers: List<Bitmap> = ArrayList()
}
```
