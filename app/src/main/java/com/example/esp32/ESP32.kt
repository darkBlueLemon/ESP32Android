package com.example.esp32

/*
#include <BluetoothSerial.h>

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

const int motorPin = 13;  // GPIO pin connected to the motor
const int pwmChannel = 0;
const int pwmFreq = 5000;  // 5 kHz PWM frequency
const int pwmResolution = 8;  // 8-bit resolution (0-255)

int duration = 1000;  // Duration in milliseconds
int dutyCycle = 50;   // Duty cycle in percent
int interval = 5000;  // Repeating interval in milliseconds

unsigned long lastRunTime = 0;

void setup() {
  Serial.begin(115200);
  SerialBT.begin("ESP32_Motor_Control");  // Bluetooth device name

  ledcSetup(pwmChannel, pwmFreq, pwmResolution);
  ledcAttachPin(motorPin, pwmChannel);

  Serial.println("The device started, now you can pair it with Bluetooth!");
}

void loop() {
  if (SerialBT.available()) {
    String command = SerialBT.readStringUntil('\n');
    processCommand(command);
  }

  unsigned long currentTime = millis();
  if (currentTime - lastRunTime >= interval) {
    runMotor();
    lastRunTime = currentTime;
  }
}

void processCommand(String command) {
  int separatorIndex = command.indexOf(':');
  if (separatorIndex != -1) {
    String key = command.substring(0, separatorIndex);
    int value = command.substring(separatorIndex + 1).toInt();

    if (key == "duration") {
      duration = value;
    } else if (key == "dutyCycle") {
      dutyCycle = constrain(value, 0, 100);
    } else if (key == "interval") {
      interval = value;
    }

    Serial.println("Updated " + key + " to " + String(value));
  }
}

void runMotor() {
  int pwmValue = map(dutyCycle, 0, 100, 0, 255);
  ledcWrite(pwmChannel, pwmValue);
  delay(duration);
  ledcWrite(pwmChannel, 0);
}
 */