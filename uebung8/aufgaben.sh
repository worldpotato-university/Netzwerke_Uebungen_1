#!/bin/sh
# echo "a:"
# read null
# # a:
# echo "wget http://mmix.cs.hm.edu/bin/optmmix-2011-5-6.tgz"
#
# echo "b:"
# read null
# # b:
# sudo tc qdisc change dev ifb0 handle 10: netem delay 500ms
#
# ping -c 4 www.hm.edu
#
# echo "test mit in anderem terminal:"
# echo "wget http://mmix.cs.hm.edu/bin/optmmix-2011-5-6.tgz"

echo "c:"
read null
sudo tc qdisc change dev ifb0 handle 10: netem delay 0ms

echo "gesetzt auf 2%:"
sudo tc qdisc change dev ifb0 handle 10: netem loss 2% 
read null

echo "gesetzt auf 5%"
sudo tc qdisc change dev ifb0 handle 10: netem loss 5% 
read null

echo "gesetzt auf 10%"
sudo tc qdisc change dev ifb0 handle 10: netem loss 10% 

# echo "d:"
# read null
# sudo tc qdisc change dev ifb0 handle 10: netem loss 0%
# sudo tc qdisc change dev ifb0 handle 10: netem delay 200ms
# sudo tc qdisc change dev ifb0 handle 1: tbf rate 128kbit buffer 1600 limit 2000
#
# echo "e:"
# read null
# sudo tc qdisc add dev ifb0 root handle 1: tbf rate 1024kbit buffer 1600 limit 2000
# sudo tc qdisc change dev ifb0 handle 10: netem delay 0ms
# sudo tc qdisc add dev enp4s0 root netem loss 10%
#
# echo "f:"
# read null
