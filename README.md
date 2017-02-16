# Power House

Recruitment Test on Profiles and MeterReadings REST CRUD operations
Database used : MongoDB.

## Build for production

To optimize the EventService client for production, run:

    ./mvn clean package

To ensure everything worked, run:

    java -jar target/*.jar
    
    
## REST APIs available
API's of Fraction,
http://localhost:9090/api/uploadFractions - POST
http://localhost:9090/api/fractions - GET
http://localhost:9090/api/fraction/A - GET
http://localhost:9090/api/fractions - DELETE

Fraction.postman_collection.json provide a list of API 

Fraction CSV file is present under root directory of Resource Folder

API's of meterreadings,
http://localhost:9090/api/uploadMeter -POST
http://localhost:9090/api/meterreadings - GET
http://localhost:9090/api/meterreadings/A - GET
http://localhost:9090/api/meterreading/A/JAN - GET
http://localhost:9090/api/meterreadings - DELETE

Meter.postman_collection.json provide a list of API 

Meter reading CSV file is present under root directory of Resource Folder
