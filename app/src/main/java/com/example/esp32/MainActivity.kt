package com.example.esp32

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.esp32.ui.theme.ESP32Theme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.juul.kable.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.juul.kable.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class MainActivity : ComponentActivity() {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotorControlScreen()
        }
    }

    @Composable
    fun MotorControlScreen() {
        var duration by remember { mutableStateOf("1000") }
        var dutyCycle by remember { mutableStateOf("50") }
        var interval by remember { mutableStateOf("5000") }
        var isConnected by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (ms)") }
            )
            TextField(
                value = dutyCycle,
                onValueChange = { dutyCycle = it },
                label = { Text("Duty Cycle (%)") }
            )
            TextField(
                value = interval,
                onValueChange = { interval = it },
                label = { Text("Interval (ms)") }
            )
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        if (!isConnected) {
                            connectBluetooth()
                            isConnected = true
                        }
                        sendCommand("duration:$duration")
                        sendCommand("dutyCycle:$dutyCycle")
                        sendCommand("interval:$interval")
                    }
                }
            ) {
                Text(if (isConnected) "Update Settings" else "Connect and Update")
            }
        }
    }

    private fun connectBluetooth() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            val device: BluetoothDevice? = bluetoothAdapter?.bondedDevices?.find { it.name == "ESP32_Motor_Control" }
            try {
                bluetoothSocket = device?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                bluetoothSocket?.connect()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun sendCommand(command: String) {
        try {
            bluetoothSocket?.outputStream?.write((command + "\n").toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}