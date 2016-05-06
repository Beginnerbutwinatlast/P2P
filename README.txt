

To start the programs, unzip the file, you will get six directories, they are client0 -client4 and a server. Then start the terminal, get into the directory, using "javac FileSender.java" to compile the file owner program, then start file owner using 
"java FileSender". Next get into the directory of five clients respectively,compile the five java files,and then start the five client programs in whatever way you like.

******************************************************************************************
If you are interested in the details of the program, please read the following:










This project contains one file owner and five clients. Each client has an upload neighbor and a download neighbor.The relationship between them is as follows:

client0 has upload neighbor client1 and download neighbor client4

client1 has upload neighbor client2 and download neighbor client0

client2 has upload neighbor client1 and download neighbor client3

client3 has upload neighbor client2 and download neighbor client4

client4 has upload neighbor client3 and download neighbor client0

In this file, FileSender.java is the file owner program,and Client_0.java -Client_4.java are five clients. The five port numbers for fileowner to connect to the clients are8821,8822,8823,8824,8825 respectively. The port numbers for each client from client0 to client4 are 8830, 8826, 8826,8828, 8829 respectively.
