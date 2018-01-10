/*
  WiFi UDP Send and Receive String

  This sketch wait an UDP packet on localPort using a WiFi shield.
  When a packet is received an Acknowledge packet is sent to the client on port remotePort

  Circuit:
   WiFi shield attached

  created 30 December 2012
  by dlf (Metodo2 srl)

*/


#include <SPI.h>

#include <WiFi101.h>
#include <WiFiUdp.h>
#include <FastLED.h>
#define NUM_LEDS 33
#define DATA_PIN 5
#define BRIGHTNESS  100

int status = WL_IDLE_STATUS;
#include "arduino_secrets.h"
///////please enter your sensitive data in the Secret tab/arduino_secrets.h
char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)
char lum1[10];
unsigned int localPort = 4096;      // local port to listen on

int r; int g; int b;

WiFiServer server(2048);
WiFiClient client;
WiFiUDP Udp;
CRGB leds[NUM_LEDS];

void setup() {

  FastLED.addLeds<NEOPIXEL, DATA_PIN>(leds, NUM_LEDS);
  //Initialize serial and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

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

  // if you get a connection, report back via serial:
  Udp.begin(localPort);
  server.begin();
}

void loop() {
  // if there's data available, read a packet
  // int packetSize = Udp.parsePacket();
  if (status == WL_CONNECTED)
  {

    //SERVE PER RICEVERE PACCHETTI UDP
    /*Serial.print("Received packet of size ");
      Serial.println(packetSize);
      Serial.print("From ");
      IPAddress remoteIp = Udp.remoteIP();
      Serial.print(remoteIp);
      Serial.print(", port ");
      Serial.println(Udp.remotePort());*/

    /*int len = Udp.read(packetBuffer, 255);
      if (len > 0) packetBuffer[len] = 0;
      Serial.println("Contents:");
      Serial.println(packetBuffer);

      char lcdBuffer[16];
      sprintf(lcdBuffer, "%d.%d.%d.%d:%d", myIP[0], myIP[1], myIP[2], myIP[3]);
      Serial.println(myIP);*/
    IPAddress myIP = WiFi.localIP();
    IPAddress ip(255, 255, 255, 255);

    Udp.beginPacket(ip, localPort);
    Udp.write("LAMP_4");
    Udp.endPacket();
  }



  //
  //TCP Connection
  //

  WiFiClient client = server.available();   // listen for incoming clients

  if (client) {                             // if you get a client,
    Serial.println("new client");           // print a message out the serial port
    String currentLine = "";                // make a String to hold incoming data from the client
    while (client.connected()) {            // loop while the client's connected
      if (client.available()) {             // if there's bytes to read from the client,
        //char s = client.read();                  // read a byte, then
        //Serial.println(s);                  // print it out the serial monitor




        String c = client.readString();
        int i1 = c.indexOf(",");
        int i2 = c.indexOf("*");
        int i3 = c.indexOf("/");
        String state = c.substring(0, i1--);
        int lum = c.substring(i1 + 2, i2).toInt();
        Serial.println(lum);
        String col = "#" + c.substring(i2 + 3, i3--);
        Serial.println(col);

        long number = (long) strtol( &col[1], NULL, 16);
        r = number >> 16;
        g = number >> 8 & 0xFF;
        b = number & 0xFF;






        /*
          String inputString = "";
          inputString.reserve(100);
          while (client.available()) {
          char c = (char)client.read();
            inputString1 += c;
          if (c == ',')
            //Serial.println(inputString);
            //inputString = "";
          }
          Serial.print(inputString);*/

        // Check to see if the client request was "true" or "false":
        if (state.equals("true")) {
          server.write("OK!");
          Serial.println("acceso");
          OnLED();                          // true turns the LED on
          Brightness(lum);
        }
        if (state.equals("false")) {
          server.write("OK!");
          Serial.println("spento");
          OffLED();                         // false turns the LED off
        }
      }
      // close the connection:
      client.stop();
      Serial.println("client disonnected");
    }
  }
}




void OnLED() {Serial.print("red is ");
    Serial.println(r);
    Serial.print("green is ");
    Serial.println(g);
    Serial.print("blue is ");
    Serial.println(b);
  for (int dot = 0; dot < NUM_LEDS; dot++) {
    leds[dot]= CRGB(r, g, b);
    FastLED.show();

  }
}

void OffLED() {
  for (int dot = 0; dot < NUM_LEDS; dot++) {
    leds[dot] = CRGB::Black;
    FastLED.show();
  }
}

void Brightness(int brightness) {
  FastLED.setBrightness(brightness);
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




