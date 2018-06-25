
//initialize libraries
#include "Arduino.h"
#include <SPI.h>
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <FastLED.h>
#include <Servo.h>
#define NUM_LEDS 33
#define DATA_PIN 5
#define BRIGHTNESS  100


//initialize wi fi port
int status = WL_IDLE_STATUS;
char ssid[] = "AndroidAP";        // your network SSID (name)
char pass[] = "111111";    // your network password (use for WPA, or use as key for WEP)
char lum1[10];
unsigned int localPort = 4096;      // local port to listen on

  
//initialize inputs options
int r; int g; int b;
char recive[] ="";
String readString = "";
String state="false";
String col="00000";
int lum=0;
int wing=0;

WiFiServer server(2048);
WiFiClient client;
WiFiUDP Udp;
CRGB leds[NUM_LEDS];
Servo servo1;
Servo servo2;
unsigned long previousMillis = 0; 
const long interval = 5000; 

void setup() {

  FastLED.addLeds<NEOPIXEL, DATA_PIN>(leds, NUM_LEDS);

  //Initialize serial and wait for port to open:
  Serial.begin(9600);

  // check for the presence of the shield:
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue:
    while (true);
  }

  // attempt to connect to WiFi network:
  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
    status = WiFi.begin(ssid, pass);

    // wait 10 seconds for connection:
    delay(10000);
  }
  Serial.println("Connected to wifi");
  printWiFiStatus();

  server.begin();
  servo1.attach(5);//corrisponde al pin d1
  servo2.attach(4);//corrisponde al pin d2
  OffLED();
}

void loop() {
  
  WiFiClient client = server.available();   // listen for incoming clients
  if (!client) {
    SendUDP();
    return;
  }
  
  // Wait until the client sends some data
  Serial.println("new client");
  while (!client.available()) {
      Serial.println("aspetto client");
  }
  while (client.available())  {
    delay(3);  
    char c = client.read();
    readString += c; 
  }
  readString.trim();

  
  //char s = client.read();           // read a byte, then
  //Serial.println(s);                  // print it out the serial monitor
  //String c = client.readString();
  //Serial.println(c);
  int i1 = readString.indexOf(",");
  int i2 = readString.indexOf("*");
  int i3 = readString.indexOf("/");
  
  state = readString.substring(0, i1--);
  lum = readString.substring(i1 + 2, i2).toInt();
  col = "#" + readString.substring(i2 + 3, i3--);
  wing = readString.substring(i3+2,readString.length()).toInt();
 
  long number = (long) strtol( &col[1], NULL, 16);
  r = number >> 16;
  g = number >> 8 & 0xFF;
  b = number & 0xFF;


  Serial.println(readString);

  // Check to see if the client request was "true" or "false":
  if (state.equals("true")) {
    client.print("OK!");
    server.write("OK!");
    Serial.println("acceso");
    OnLED();                          // true turns the LED on
    Brightness(lum);
    Wings(wing);
    client.stop();
    readString="";
  }
  if (state.equals("false")) {
    client.print("OK!");
    server.write("OK!");
    Serial.println("spento");
    OffLED();                         // false turns the LED off
    Brightness(0);
    Wings(0);
    client.stop();
    readString="";
  }
  // close the connection:
  readString="";
  client.stop();
  Serial.println("client disonnected");
}

void OnLED() {
  Serial.print("red is ");
  Serial.println(r);
  Serial.print("green is ");
  Serial.println(g);
  Serial.print("blue is ");
  Serial.println(b);
  for (int dot = 0; dot < NUM_LEDS; dot++) {
    leds[dot] = CRGB(r, g, b);
  }
  FastLED.show();
}

void OffLED() {
  for (int dot = 0; dot < NUM_LEDS; dot++) {
    leds[dot] = CRGB::Black;
  }
  FastLED.show();
}

void Brightness(int brightness) {
  FastLED.setBrightness(brightness);
  FastLED.show();

}

void Wings(int wing){
  int pos = 90;
  Serial.print(wing);
  servo1.write(pos-wing);
  servo2.write(wing);
}

void SendUDP () {
  if (status == WL_CONNECTED)
  {
    unsigned long currentMillis = millis();
    if (currentMillis - previousMillis >= interval) {
      Serial.print("invio");
      previousMillis = currentMillis;   
      IPAddress ip(255, 255, 255, 255);   
      const char *st = state.c_str();   
      const char *cl = intCol.c_str();
      char lu[10];    
      sprintf(lu, "%d", lum);   
      char wi[10];
      sprintf(wi, "%d", wing);
      Udp.beginPacket(ip, localPort);
      Udp.write("LAMP_4");
      Udp.write("*");   
      Udp.write(st);
      Udp.write("-");
      Udp.write(lu);
      Udp.write("+");
      Udp.write(cl);
      Udp.write(",");
      Udp.write(wi);
      Udp.write("<");
      Udp.write("https://firebasestorage.googleapis.com/v0/b/lampapp-6688e.appspot.com/o/Flower_Lamp.png?alt=media&token=50681145-8f46-4cfe-8ea0-e2bebb78785a");
      Udp.endPacket();
      delay(10);
    }
  }
}


void printWiFiStatus() {

  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}




