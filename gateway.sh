echo '' > /tmp/latest.txt
wget -O /tmp/latest.txt http://localhost/cgi-bin/luci/wisense/info
ifconfig eth0|grep "inet addr:"|awk '{print $2}'|awk -F : '{print $2}' >> /tmp/latest.txt
curl -X POST -d @latest.txt http://192.168.1.1:4901/iot/observe/radiosensor
