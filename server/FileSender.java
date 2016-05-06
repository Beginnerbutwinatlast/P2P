

import java.io.BufferedInputStream; 
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.io.*;
import java.net.ServerSocket;  
import java.net.Socket;  
   
public class FileSender extends Thread { 
    private ServerSocket ss=null;
    private int port;
    public FileSender(int port){ 
        this.port = port;
           
    }  
    public void run(){  
        DataOutputStream dos=null;  
        DataInputStream dis=null;
        DataInputStream dis1=null;
           
        Socket socket=null;  
        try { 
             
             
            ss=new ServerSocket(port);  
            socket=ss.accept();  
            System.out.println("waiting for connection...");
            dos=new DataOutputStream(socket.getOutputStream());  
               
            dis1=new DataInputStream(socket.getInputStream());
             
            String filepath = dis1.readUTF();
            dis=new DataInputStream(new BufferedInputStream(new FileInputStream(filepath)));
            File file=new File(filepath);  
             
            int bufferSize=102400; 
            dos.writeUTF(file.getName());   
            dos.flush();   
            dos.writeLong((long) file.length());   
            dos.flush(); 
            int remainSize = (int)file.length();
            int readSize = 0;
            ArrayList<byte[]> list = new ArrayList<byte[]>();
             
            while(remainSize > 0){
                if(remainSize < bufferSize){
                    readSize = remainSize;
                }else{
                    readSize = bufferSize;
                }
                int read = 0;   
                byte[] bufArray = new byte[readSize];
                 
                if (dis!= null) {   
                    read = dis.read(bufArray);   
                }   
   
                if (read == -1) {   
                        break;   
                }   
                
                 list.add(bufArray);
                 remainSize = remainSize - readSize;
                  
                }
                //System.out.println(list);
             
               ArrayList<Integer> filestat = new ArrayList<Integer>();
               for(int i = 0; i < list.size(); i++){
                   filestat.add(1);
               }
               //System.out.println(filestat);
               dos.writeInt(list.size());
               dos.flush();
               for(int j = 0; j < filestat.size();j++)   {
                    dos.writeInt(filestat.get(j));
                    dos.flush();
                    }
                     
                 
                while(true){
                     
                    int rnumber = dis1.readInt();
                    if(rnumber == -1){
                        break;
                    }
                    System.out.println("Send chunk "+rnumber);
                    dos.writeInt((list.get(rnumber)).length);
                    //System.out.println((list.get(rnumber)).length);
                    dos.flush();
                    dos.write(list.get(rnumber), 0, (list.get(rnumber)).length);
                    dos.flush();
                  
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    //System.out.println(dos);
                }
                 
                 
              
        }   
            catch (FileNotFoundException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } finally {   
                  // 关闭所有连接   
                  try {   
                    if (dos != null)   
                      dos.close();   
                  } catch (IOException e) {   
                  }   
                  try {   
                    if (dis != null)   
                      dis.close();   
                  } catch (IOException e) {   
                  }   
                  try {   
                    if (socket != null)   
                      socket.close();   
                  } catch (IOException e) {   
                  }   
                  try {   
                    if (ss != null)   
                      ss.close();   
                  } catch (IOException e) {   
                  }   
              }
               
       
       
        }  
                 
              
     public static void main(String []args){ 
      String[] reader= null;
      try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader("configure");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            reader = line.split(" ");

            // Always close files.
            bufferedReader.close();         
        } 
        
       catch (Exception e){
       
          } 
       FileSender server = new  FileSender(Integer.parseInt(reader[0]));
       FileSender server1 = new  FileSender(Integer.parseInt(reader[1]));
       FileSender server2 = new  FileSender(Integer.parseInt(reader[2]));
       FileSender server3 = new  FileSender(Integer.parseInt(reader[3]));
       FileSender server4 = new  FileSender(Integer.parseInt(reader[4]));
        
      server.start();
      server1.start();
      server2.start();
      server3.start();
      server4.start();
    }  
        
        
      
}
  
