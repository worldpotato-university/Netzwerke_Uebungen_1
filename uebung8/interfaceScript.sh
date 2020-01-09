#!/bin/sh

# laden des kernel moduls
sudo modprobe ifb

# einschalten der virtuellen netzwerkkarte
sudo ip link set dev ifb0 up

ip a

echo ""
echo "Wie heist das gewünschte interface?"

read INTERFACE

# verbindung zwischen dem gegebenen interface und der virtuellen Netwerkkarte
sudo tc qdisc add dev $INTERFACE ingress

# installieren des Netzwerkfilters
sudo tc filter add dev $INTERFACE parent ffff: protocol ip u32 match u32 0 0 flowid 1:1 action mirred egress redirect dev ifb0

# einhängen des netzwerkfilters
sudo tc qdisc add dev ifb0 root handle 1: tbf rate 1024kbit buffer 1600 limit 2000 


