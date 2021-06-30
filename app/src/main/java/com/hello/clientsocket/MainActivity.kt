package com.hello.clientsocket

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hello.clientsocket.databinding.ActivityMainBinding
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.net.SocketException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.setOnClickListener{
            ClientThread().start()
        }
    }

    inner class ClientThread : Thread() {
        override fun run() {
            val ipAddress = "92.168.1.119"
            val port = 10001

            Log.d("clientServer", "ip주소 : $ipAddress")
            try {
                val socket = Socket(ipAddress, port)

                val outputStream = ObjectOutputStream(socket.getOutputStream())
                outputStream.writeObject(binding.editText.text.toString()) // write 함수
                outputStream.flush()

                val inputStream = ObjectInputStream(socket.getInputStream())
                val input = inputStream.readObject() as String // read 함수

                handler.post {
                    binding.tvResult.append("$input\n")
                }

            } catch (e: SocketException) {
                handler.post {
                    Toast.makeText(applicationContext, "소켓 연결 실패", Toast.LENGTH_SHORT).show()
                }
            }

            super.run()
        }
    }
}