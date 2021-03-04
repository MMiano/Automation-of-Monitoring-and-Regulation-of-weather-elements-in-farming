/* Code read light intensity with a threshold of 700.
 *  Turn on Light after the threshold is reached
 */
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <dht.h>

#define WIFI_SSID "ssid"
#define WIFI_PASSWORD "password"

#define FIREBASE_HOST "practicals001-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "ENXolIBUTbEHkjBQYgJ9lDx1KAFEuddFaHae3uPg"

int LDRPin = A0;   // select the input pin for ldr
int sensorValue = 0;  // variable to store the value coming from the sensor
int lightReg;
int userReg;

int led = D2;

dht DHT;

#define DHT11_PIN D3 // digital pin 7


void setup() {

  pinMode(led, OUTPUT); // Declare the LED as an output
  
  
  Serial.begin(115200); //sets serial port for communication
  
  Serial.println('\n');
  wifiConnect();
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  delay(10);

}

void loop() {
  
  // Temperature:
  sensorValue = analogRead(LDRPin);
  Serial.println("Sensor Value: ");
  Serial.println(sensorValue); //prints the values coming from the sensor on the screen
  Firebase.setInt("LightIntensity", sensorValue);  //setup path and send readings
  
  lightReg = Firebase.getInt("lightReg");
  userReg = Firebase.getInt("userReg");

  if(int(userReg) == int(1)){

    if(int(sensorValue) < int(300) || int(lightReg) == int(1)){
        digitalWrite(led, HIGH); 
    }
    else{
      digitalWrite(led, LOW); // Turn the LED on
    }
    
  }else{
    digitalWrite(led, LOW); // Turn the LED on
  }
  

  // Humidity and Temperature.
  int chk = DHT.read11(DHT11_PIN); // measure humidity and temperature.

  Firebase.setInt("Humidity", int(DHT.humidity));
  Firebase.setInt("Temperature", int(sensorValue)); 

  Serial.println("Temperature: ");
  Serial.println(DHT.temperature); //prints the values coming from the sensor on the screen
  Serial.println("Humidity: ");
  Serial.println(DHT.humidity); //prints the values coming from the sensor on the screen
 
  delay(1000);
}

void wifiConnect()
{
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);             // Connect to the network
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID); Serial.println(" ...");

  int teller = 0;
  while (WiFi.status() != WL_CONNECTED)
  {                                       // Wait for the Wi-Fi to connect
    delay(1000);
    Serial.print(++teller); Serial.print(' ');
  }

  Serial.println('\n');
  Serial.println("Connection established!");  
  Serial.print("IP address:\t");
  Serial.println(WiFi.localIP());         // Send the IP address of the ESP8266 to the computer
}
