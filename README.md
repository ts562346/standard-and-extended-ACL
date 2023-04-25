# standard-and-extended-ACL

Write two programs (one for standard and one for extended) that simulate the processing of an ACL at a router’s interface. Each program should read two text files, one containing ACL statements, and another containing a list of IP addresses that represent packets coming into the interface. The input to the standard ACL program will be just a list of source IP addresses while the input to the extended ACL program will be a list of source IP address, destination IP address and port number. The program should process each packet according to the ACL statements and decide to permit or deny each packet.You may assume that the program is simulating the ACL (either in or out) at just one interface. You may also assume that the input files are error-free, that is, you need not check for syntax and IP address errors.

Here’s an example for how the standard ACL program should work:

Read Input text file 1: <br/>

access-list 3 deny 172.16.4.0 0.0.0.255 <br/>
access-list 3 permit 172.16.0.0 0.0.255.255 <br/>
interface EO <br/>
ip access-group 3 out <br/>

Read Input text file 2 (consists of a list of source IP addresses): <br/>

172.16.4.1 <br/>
172.16.3.5 <br/>
201.15.3.4 <br/>

Display the following output: <br/>

Packet from 172.16.4.1 denied <br/>
Packet from 172.16.3.5 permitted <br/>
Packet from 201.15.3.4 denied <br/>

As you can notice, the three packets in the second text file test all the boundary conditions. Here’s an example for how the extended ACL program should work:

Read Input text file 1: <br/>

access-list 101 deny tcp 172.16.0.0 0.0.255.255 172.16.3.0 0.0.0.255 range 20-21 <br/>
access-list 101 permit ip 172.16.0.0 0.0.255.255 172.16.3.0 0.0.0.255 <br/>
interface EO <br/>
ip access-group 101 out <br/>

Read Input text file 2 (consists of a list of source IP, destination IP addresses and port numbers): <br/>

172.16.4.4 172.16.3.1 20 <br/>
172.16.4.4 172.16.3.5 22 <br/>
172.25.3.1 172.16.3.4 22 <br/>

Display the following output: <br/> 

Packet from 172.16.4.4 to 172.16.3.1 on port 20 denied <br/>
Packet from 172.16.4.4 to 172.16.3.5 on port 22 permitted <br/>
Packet from 172.25.3.1 to 172.16.3.4 on port 22 denied <br/>
