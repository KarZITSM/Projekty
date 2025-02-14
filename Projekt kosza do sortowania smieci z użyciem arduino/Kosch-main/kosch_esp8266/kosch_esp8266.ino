#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <Wire.h>
//#include <ArduinoJson.h>
#define MAX_TIME_WAIT_FOR_ARDUINO 1000

//char *ssid = "PBL_WiFi";
//char *password = "IIRPW2020";

char *ssid = "kuba";
char *password = "kubakuba7";

char *hostname = "kosch";

ESP8266WebServer server(80);

String action = "unknown";
String trashIds = "unknown";

void setup() {
	Serial.begin(115200);
	Wire.begin(D1, D2);//join i2c bus with SDA=D1 and SCL=D2 of NodeMCU
	Serial.println();

	WiFi.mode(WIFI_STA);
	WiFi.setHostname(hostname);

	WiFi.begin(ssid, password);
	while (WiFi.status() != WL_CONNECTED) {
		delay(1000);
		Serial.print(".");
	}
	Serial.println();
	Serial.println("WiFi connected. IP address: ");
	Serial.println(WiFi.localIP());

	server.on("/", handleRoot);
	server.on("/api/", handleApi);
	server.begin();
}

void loop() {
	server.handleClient();
}

void handleRoot() {
	String text = "hello!";
	server.send(200, "text/html", text);
}

void handleApi() {
	/*if (server.arg("trashIds") == "" || server.arg("action") == "") {
		String text = "{\"error\":\"invalidRequest\"}";
		server.send(400, "application/json", text);
	}*/
	trashIds = server.arg("trashIds");
	Serial.println("trashIds=" + trashIds);
	action = server.arg("action");
	Serial.println("action=" + action);
	String comm = action + "," + trashIds;
	Wire.beginTransmission(8);
	Wire.write(comm.c_str());
	Wire.endTransmission();

	String data = "";
	int i = 0;
	unsigned long startTime = millis();
	while (i < 512 && millis() < startTime + MAX_TIME_WAIT_FOR_ARDUINO) {
		delay(1);//Wait for Slave to calculate response.
		Wire.requestFrom(8, 32);
		while (Wire.available()) {
			char c = Wire.read();
			if (c == '\n') {
				i = 10000;
				break;
			}
			data += c;
			i++;
		}
	}

	server.send(200, "application/json", data);
}