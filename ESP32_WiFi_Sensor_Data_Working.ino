//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 03_FR_DB_ESP32_DHT11_LED
/*
 *  Reference : https://randomnerdtutorials.com/esp32-firebase-realtime-database/
 */

//======================================== Including the libraries.
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>
#include "DHT.h"
#define DHTPIN 26
#define DHTTYPE DHT11
DHT dht11(DHTPIN, DHTTYPE);
//======================================== 

//======================================== Insert your network credentials.
#define WIFI_SSID "Virus"
#define WIFI_PASSWORD "nustiuparola"
//======================================== 

//======================================== Defines the pin and type of DHT sensor and initializes the DHT sensor.

//======================================== 

// Defines the Digital Pin of the "On Board LED".
#define On_Board_LED 2

//Provide the token generation process info.
#include "addons/TokenHelper.h"

//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert Firebase project API Key
#define API_KEY "AIzaSyAMmvjcsPfGwH9ewx7OSsYuSjT6ZnkjA54"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://homemonitoringapplogindata-default-rtdb.europe-west1.firebasedatabase.app/" 

// Define Firebase Data object.
FirebaseData fbdo;

// Define firebase authentication.
FirebaseAuth auth;

// Definee firebase configuration.
FirebaseConfig config;

//======================================== Millis variable to send/store data to firebase database.
unsigned long sendDataPrevMillis = 0;
const long sendDataIntervalMillis = 1000; //--> Sends/stores data to firebase database every 1 seconds.
//======================================== 

// Boolean variable for sign in status.
bool signupOK = false;

float Temp_Val;
int Humd_Val;
float ppm;
int Flacara;
int Vibratii;
int Infrarosu;
//############################################################
int pinGaz = 39;
int pinFlacara = 13;
int pinVibratii = 25;
int pinInfraRosu = 27;

//________________________________________________________________________________ Get temperature and humidity values from the DHT11 sensor.
void read_DHT11() {
  Temp_Val = dht11.readTemperature();
  Humd_Val = dht11.readHumidity();

  //---------------------------------------- Check if any reads failed.
  if (isnan(Temp_Val) || isnan(Humd_Val)) {
    Serial.println(F("Failed to read from DHT sensor!"));
    Temp_Val = 0.00;
    Humd_Val = 0;
  }
  //---------------------------------------- 

  Serial.println("---------------Read_DHT11");
  Serial.print(F("Humidity   : "));
  Serial.print(Humd_Val);
  Serial.println("%");
  Serial.print(F("Temperature: "));
  Serial.print(Temp_Val);
  Serial.println("°C");
  Serial.println("---------------");
}
//________________________________________________________________________________ 
void read_GasSensor()
{
  int gasSensorValue = analogRead(pinGaz);
  ppm = map(gasSensorValue,0,4095,0,1000);
  Serial.print("Nivel de gaz (ppm): ");
  Serial.println(ppm);
}
//________________________________________________________________________________ 

void read_Flaracara()
{
  Flacara = digitalRead(pinFlacara);
  if(Flacara == 0)
  {
    Serial.println("Flacara a fost detectata");
  }
  else
  {
    Serial.println("Flacara NU a fost detectata");
  }
}
//________________________________________________________________________________ 
void read_Vibratii()
{
  Vibratii = digitalRead(pinVibratii);
  if(Vibratii == 1)
  {
    Serial.println("Vibrations detected");
  }
  else
  {
    Serial.println("No vibrations detected");
  }
}

//________________________________________________________________________________ 
void read_Infrarosu()
{
  Infrarosu = digitalRead(pinInfraRosu);
  if(Infrarosu == 1)
  {
    Serial.println("Motion detected");
  }
  else
  {
    Serial.println("No motion detected");
  }
}
//________________________________________________________________________________ 
void store_data_to_firebase_database() {
 
  digitalWrite(On_Board_LED, HIGH);
  
  // Write an Int number on the database path DHT11_Data/Temperature.
  if (Firebase.RTDB.setFloat(&fbdo, "DHT11_Data/Temperature", Temp_Val)) {
    Serial.println("PASSED");
    Serial.println("PATH: " + fbdo.dataPath());
    Serial.println("TYPE: " + fbdo.dataType());
  }
  else {
    Serial.println("FAILED");
    Serial.println("REASON: " + fbdo.errorReason());
  }
  
  // Write an Float number on the database path DHT11_Data/Humidity.
  if (Firebase.RTDB.setInt(&fbdo, "DHT11_Data/Humidity", Humd_Val)) {
    Serial.println("PASSED");
    Serial.println("PATH: " + fbdo.dataPath());
    Serial.println("TYPE: " + fbdo.dataType());
  }
  else {
    Serial.println("FAILED");
    Serial.println("REASON: " + fbdo.errorReason());
  }

  if (Firebase.RTDB.setFloat(&fbdo, "GasSensor/ppm", ppm)) {
    Serial.println("PASSED");
    Serial.println("PATH: " + fbdo.dataPath());
    Serial.println("TYPE: " + fbdo.dataType());
  }
  else {
    Serial.println("FAILED");
    Serial.println("REASON: " + fbdo.errorReason());
  }

  if (Firebase.RTDB.setFloat(&fbdo, "FlacaraSensor/Flacara", Flacara)) {
    Serial.println("PASSED");
    Serial.println("PATH: " + fbdo.dataPath());
    Serial.println("TYPE: " + fbdo.dataType());
  }
  else {
    Serial.println("FAILED");
    Serial.println("REASON: " + fbdo.errorReason());
  }

  if (Firebase.RTDB.setFloat(&fbdo, "VibrationSensor/Vibrations", Vibratii)) {
    Serial.println("PASSED");
    Serial.println("PATH: " + fbdo.dataPath());
    Serial.println("TYPE: " + fbdo.dataType());
  }
  else {
    Serial.println("FAILED");
    Serial.println("REASON: " + fbdo.errorReason());
  }

  if (Firebase.RTDB.setFloat(&fbdo, "MotionSensor/Motion", Infrarosu)) {
    Serial.println("PASSED");
    Serial.println("PATH: " + fbdo.dataPath());
    Serial.println("TYPE: " + fbdo.dataType());
  }
  else {
    Serial.println("FAILED");
    Serial.println("REASON: " + fbdo.errorReason());
  }

  digitalWrite(On_Board_LED, LOW);
  Serial.println("---------------");
}
//________________________________________________________________________________ 


void setup() {
  // put your setup code here, to run once:
  
  Serial.begin(115200);
  Serial.println();

  pinMode(On_Board_LED, OUTPUT);
  

  //---------------------------------------- The process of connecting the WiFi on the ESP32 to the WiFi Router/Hotspot.
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.println("---------------Connection");
  Serial.print("Connecting to : ");
  Serial.println(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");

    digitalWrite(On_Board_LED, HIGH);
    delay(250);
    digitalWrite(On_Board_LED, LOW);
    delay(250);
  }
  digitalWrite(On_Board_LED, LOW);
  Serial.println();
  Serial.print("Successfully connected to : ");
  Serial.println(WIFI_SSID);
  //Serial.print("IP : ");
  //Serial.println(WiFi.localIP());
  Serial.println("---------------");
  //---------------------------------------- 

  // Assign the api key (required).
  config.api_key = API_KEY;

  // Assign the RTDB URL (required).
  config.database_url = DATABASE_URL;

  // Sign up.
  Serial.println();
  Serial.println("---------------Sign up");
  Serial.print("Sign up new user... ");
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  }
  else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }
  Serial.println("---------------");
  
  // Assign the callback function for the long running token generation task.
  config.token_status_callback = tokenStatusCallback; //--> see addons/TokenHelper.h
  
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
//####################################################
  dht11.begin();
  pinMode(pinGaz,INPUT);
  pinMode(pinFlacara,INPUT);
  pinMode(pinVibratii,INPUT);
  pinMode(pinInfraRosu,INPUT);
//####################################################
  delay(1000);
}
//________________________________________________________________________________ 

//________________________________________________________________________________ VOID LOOP
void loop() {
  // put your main code here, to run repeatedly:
  
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > sendDataIntervalMillis || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();
    read_Flaracara();
    read_GasSensor();
    read_DHT11();
    read_Infrarosu();
    read_Vibratii();
    store_data_to_firebase_database();
    
  }
}

