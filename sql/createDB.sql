-- script to create the database for the sensor data

CREATE TABLE LightSample
(
SampleID INTEGER,
SensorID INTEGER,
Value DOUBLE,
Date DATETIME
);

CREATE TABLE TemperatureSample
(
SampleID INTEGER,
SensorID INTEGER,
Value DOUBLE,
Date DATETIME
);

CREATE TABLE AccelerationSample
(
SampleID INTEGER,
SensorID INTEGER,
Value DOUBLE,
Date DATETIME
);

CREATE TABLE HumiditySample
(
SampleID INTEGER,
SensorID INTEGER,
Value DOUBLE,
Date DATETIME
);