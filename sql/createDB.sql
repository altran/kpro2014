-- script to create the database for the sensor data

CREATE TABLE LightSample
(
SampleID int,
SensorID int,
Value double,
Date date
);

CREATE TABLE TemperatureSample
(
SampleID int,
SensorID int,
Value double,
Date date
);

CREATE TABLE AccelerationSample
(
SampleID int,
SensorID int,
Value double,
Date date
);

CREATE TABLE HumiditySample
(
SampleID int,
SensorID int,
Value double,
Date date
);