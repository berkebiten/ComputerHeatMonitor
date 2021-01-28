#include <SoftwareSerial.h>

SoftwareSerial BTserial(10,11); //RX/TX

int sensorPin = 0;
int redPin = 7;
int greenPin = 6;
int bluePin = 5;

float temp;

void setup() {

  Serial.begin(9600);
  BTserial.begin(9600);
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);

}

void loop() {
  temp = analogRead(sensorPin);
  temp = (5.0 * temp * 1000.0) / (1024 * 10);

  Serial.println(temp);
  BTserial.println(temp);

  if(temp < 25){
    setColor(0, 255, 0);
  } else {
    setColor(255, 0, 0);
  }

  delay(5000);
}

void setColor(int redValue, int greenValue, int blueValue){
  analogWrite(redPin, redValue);
  analogWrite(greenPin, greenValue);
  analogWrite(bluePin, blueValue);
}
