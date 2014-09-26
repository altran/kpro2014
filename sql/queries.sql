-- this file contains useful database queries
SELECT DISTINCT * FROM (
	SELECT MAX(Date) AS MaxDate, SensorID
	FROM TemperatureSample
	GROUP BY SensorID
) AS T1 JOIN TemperatureSample AS T2
ON MaxDate = Date AND T1.SensorID = T2.SensorID;