package com.zhy.collector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zhy.lib_common.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.i(TAG, "onCreate ..");
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
        LogUtils.i(TAG, sample_text.text.toString());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private external fun stringFromJNI(): String


    companion object {
        val TAG = MainActivity::class.java.simpleName

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
