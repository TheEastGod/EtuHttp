package com.zxd.httpdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MockWebServerHandler.MockWebServerCallBack {


    val param  :String = "1023232&PUT\r\n" + "/api/devices/recognition\r\n" +"{\n" +
            "    \"face\": {\n" +
            "        \"sharpness_threshold\": 70,\n" +
            "        \"pitch_threshold\": 35.0,\n" +
            "        \"roll_threshold\": 35.0,\n" +
            "        \"yaw_threshold\": 35.0\n" +
            "    },\n" +
            "    \"ir_detect\": true,\n" +
            "    \"liveness_mode\": true,\n" +
            "    \"mask_mode\": true,\n" +
            "    \"rgb_liveness_threshold\": 99.4,\n" +
            "    \"max_recognition_distance\": 1.5,\n" +
            "    \"mask_threshold\": 71.0,\n" +
            "    \"idcard_recognition_threshold\": 60.0,\n" +
            "    \"recognition_mode\": \"single\",\n" +
            "    \"second_recognition_interval\": 4,\n" +
            "    \"recognition_threshold\": 71.0,\n" +
            "    \"camera_wdr\": 1\n" +
            "}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MockWebServerHandler.instances.setMockWebServerCallBack(this)
        MockWebServerHandler.instances.connect()

        button1.setOnClickListener {
            MockWebServerHandler.instances.sendMessage(param)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onConnect(hostName: String, port: Int) {
        textView1.post {
            textView1.text = "hostName  $hostName   port   $port"
        }
    }

    override fun onMessage(text: String) {
        Log.i("MockWebServerHandler","onMessage  $text")
        textView1.post {
            textView1.text = text
        }

    }

    override fun onFailure(t: Throwable) {
        textView1.text = t.message
    }


}