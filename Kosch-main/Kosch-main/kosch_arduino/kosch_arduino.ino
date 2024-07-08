#include <Wire.h>
#include <ArduinoJson.h>
#include <Servo.h>
#define ULTRASONIC_TRIGGER_PIN 12
#define MAX_DISTANCE_CM 100

unsigned long CLOSE_DELAY_MS = 6000;
unsigned long DISTANCE_CALCULATION_INTERVAL = 1000;
unsigned long DISTANCE_CALCULATION_LAST = 0;

struct trash {
	int trashId, pinServo, pinDistanceSensor, distance;
	bool opened = false, mirrorred = false;
	unsigned long timeOpened = 0;
	Servo servo;
	String name;

	trash(int trashId, int pinServo, bool mirrorred, int pinDistanceSensor, String name) {
		this->trashId = trashId;
		this->pinServo = pinServo;
		this->mirrorred = mirrorred;
		this->pinDistanceSensor = pinDistanceSensor;
		this->name = name;
	}

	void init() {
		this->servo.attach(this->pinServo);
		pinMode(this->pinDistanceSensor, INPUT);
	}

	void open() {
		this->servo.write(this->mirrorred ? 0 : 120);
		this->servo.detach();
		this->servo.attach(this->pinServo);
		this->opened = true;
		this->timeOpened = millis();
	}

	void close() {
		this->opened = false;
		this->servo.write(this->mirrorred ? 120 : 0);
		this->servo.detach();
		this->servo.attach(this->pinServo);
	}

	void calculateDistance() {
		if (this->opened) {
			return;
		}
		digitalWrite(ULTRASONIC_TRIGGER_PIN, LOW);
		delayMicroseconds(2);
		digitalWrite(ULTRASONIC_TRIGGER_PIN, HIGH);
		delayMicroseconds(10);
		digitalWrite(ULTRASONIC_TRIGGER_PIN, LOW);
		long time = pulseIn(this->pinDistanceSensor, HIGH);
		int tmpDistance = time / 58;
		if (tmpDistance < MAX_DISTANCE_CM) {
			distance = tmpDistance;
		}
		delay(5);
	}
};

trash trashes[] = {
	/*id, pinServo, mirrorred, pinDistanceSensor, name*/
	trash::trash(1, 5, false, 8, "Papier"),
	trash::trash(2, 6, false, 13, "Plastik"),
	trash::trash(4, 3, true, 2, "Zmieszane"),
	trash::trash(8, 10, true, 9, "Szklo"),
};

int trashesCount = 4;

void setup() {
	Wire.begin(8);
	Wire.onReceive(espWifiReceiveEvent);
	Wire.onRequest(espWifiRequestEvent);
	Serial.begin(115200);
	Serial.println("running...");
	pinMode(ULTRASONIC_TRIGGER_PIN, OUTPUT);
	for (int i = 0; i < trashesCount; i++) {
		trashes[i].init();
		trashes[i].close();
		Serial.println(trashes[i].name);
	}
	Serial.println("setup");
}

void loop() {
	unsigned long now = millis();
	bool shouldCalculateDistance = false;
	if (now - DISTANCE_CALCULATION_LAST > DISTANCE_CALCULATION_INTERVAL) {
		DISTANCE_CALCULATION_LAST = now;
		shouldCalculateDistance = true;
		Serial.println("shouldCalculateDistance = true");
		/*for (int i = 0; i < trashesCount; i++) {
			Serial.print("id=");
			Serial.print(trashes[i].name);
			Serial.print(" dist=");
			Serial.println(trashes[i].distance);
		}*/
	}
	for (int i = 0; i < trashesCount; i++) {
		if (trashes[i].opened && now > trashes[i].timeOpened + CLOSE_DELAY_MS) {
			/*Serial.print("Zamykam klape: ");
			Serial.println(trashes[i].name);*/
			Serial.println("trashes[i].close();");
			trashes[i].close();
		}
		if (shouldCalculateDistance) {
			Serial.println("trashes[i].calculateDistance();");
			trashes[i].calculateDistance();
		}
	}
	delay(10);
}

int wireRequestIndex = 0;
char jsonResponse[512];

void espWifiReceiveEvent(int count) {
	Serial.println("espWifiReceiveEvent 1");
	String data = "";
	while (0 < Wire.available()) {
		char c = Wire.read();
		data += c;
	}
	//Serial.println(data);
	int indexOfSeparator = data.indexOf(',');
	if (indexOfSeparator < 0) {
		return;
	}
	String action = data.substring(0, indexOfSeparator);
	int trashIds = data.substring(indexOfSeparator + 1).toInt();
	Serial.print("action = ");
	Serial.println(action);
	if (action != "info") {
		Serial.print("trashIds = ");
		Serial.println(trashIds);
		for (int i = 0; i < trashesCount; i++) {
			if ((trashes[i].trashId & trashIds) == trashes[i].trashId) {
				if (action == "open") {
					trashes[i].open();
				}
				if (action == "close") {
					trashes[i].close();
				}
			}
		}
	}
	Serial.println("espWifiReceiveEvent 2");


	wireRequestIndex = 0;
	for (int i = 0; i < 512; i++) {
		jsonResponse[i] = '\0';
	}
	StaticJsonDocument<200> doc;
	for (int i = 0; i < trashesCount; i++) {
		doc["t"][i]["i"] = trashes[i].trashId;
		//doc["t"][i]["n"] = trashes[i].name.c_str();
		doc["t"][i]["o"] = trashes[i].opened ? 1 : 0;
		doc["t"][i]["d"] = trashes[i].distance;
	}
	serializeJson(doc, jsonResponse);
	Serial.println("espWifiReceiveEvent 3");
}

void espWifiRequestEvent() {
	Serial.println("espWifiRequestEvent 1");
	for (int i = 0; i < 32; i++) {
		if (jsonResponse[wireRequestIndex] == '\0') {
			Wire.write("\n");
			break;
		}
		Wire.write(jsonResponse[wireRequestIndex]);
		wireRequestIndex++;
	}
	Serial.println("espWifiRequestEvent 2");
}