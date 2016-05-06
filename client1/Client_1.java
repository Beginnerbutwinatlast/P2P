

import java.io.BufferedInputStream;  
import java.lang.Math;
import java.io.BufferedOutputStream;  
import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.net.ServerSocket;
import java.net.Socket;  
import java.net.UnknownHostException; 
import java.util.ArrayList;
import java.io.*;
   
public class Client_1 extends Thread {
    public String fileName;
    public String serverIp;
    public int serverPortno;
    public String downloadIp;
    public int downloadPortno;
    public String uploadIp;
    public int uploadPortno;
     
    public static ArrayList<Integer> mylist = new ArrayList<Integer>();
    public int choosetask;
     
   // public FileInceptor(){  
           
    //}
    public Client_1(String savePath,String ip,int port, int taskno){
        switch(taskno){
        case 0:
            this.serverIp = ip;
            this.serverPortno = port;
            this.choosetask = taskno;
            this.fileName = savePath;
            break;
        case 1:
            this.downloadIp = ip;
            this.downloadPortno = port;
            this.choosetask = taskno;
            this.fileName = savePath;
            break;
        case 2:
            this.uploadIp = ip;
            this.uploadPortno = port;
            this.choosetask = taskno;
            this.fileName = savePath;
            break;
        default:
            break;
             
        }
         
    }
    public void run(){
        switch(this.choosetask){
        case 0 :
            this.serverDownload();
            break;
        case 1:
            this.neighborDownload();
            break;
        case 2:
            this.neighborUpload();
            break;
        default:
            break;
        }
    }
    public void serverDownload()  {  
        Socket socket=null; 
   
        try {   
            socket = new Socket(this.serverIp,this.serverPortno);   
          } catch (UnknownHostException e1) {   
            e1.printStackTrace();   
          } catch (IOException e1) {   
            e1.printStackTrace();   
          }   
        DataInputStream dis=null; 
        DataOutputStream dos = null;
         
        try {   
            dis = new DataInputStream(new BufferedInputStream(socket   
                .getInputStream()));   
             dos = new DataOutputStream(socket.getOutputStream());
          } catch (IOException e1) {   
            e1.printStackTrace();   
          }   
         
        long len = 0;   
        // 获取文件名称   
       try{ 
           dos.writeUTF("pocsd.pdf");
           dos.flush();
        this.fileName = dis.readUTF(); 
        len = dis.readLong();
        
        System.out.println("The file length is :" + len + "    B");   
        //System.out.println("开始接收文件!"); 
        int chunkSize = dis.readInt();
        System.out.println("The number of chunks is "+chunkSize);
        ArrayList<Integer> receivelist = new ArrayList<Integer>();
        ArrayList<Integer> needlist = new ArrayList<Integer>();
        for(int j =0; j <chunkSize; j++){
            mylist.add(0);
        }
         
        for(int m =0; m <chunkSize; m++){
                receivelist.add(dis.readInt());
            }
        int sumOfList=0;
        for(int i = 0; i < chunkSize; i++){
            sumOfList += mylist.get(i);
        }
        while(sumOfList < chunkSize) {
            //System.out.println("mylist :" + mylist);
            needlist = new ArrayList<Integer>();
             
            for(int n =0; n <chunkSize; n++){
                if(receivelist.get(n) - mylist.get(n) == 1){
                    needlist.add(n);
               }
            }
         
            //System.out.println(receivelist);
            //System.out.println("needlist:"+needlist);
            int read = 0;
            final double d = Math.random();
            final int rnumber = (int)(d*(needlist.size()-1));
            System.out.println("request chunk " +needlist.get(rnumber)+" from fileowner");
             
            dos.writeInt(needlist.get(rnumber));
            int tmpbufferSize = dis.readInt();
            //System.out.println("temp"+tmpbufferSize);
            byte[] buf = new byte[tmpbufferSize];
           // System.out.println("request"+rnumber + "from server");
            dos.flush();
            if (dis!= null) {   
                read = dis.read(buf); 
            } 
            //System.out.println("read: "+ read);
         
            DataOutputStream fileOut = new DataOutputStream(   
                new BufferedOutputStream(new BufferedOutputStream(   
                    new FileOutputStream("chunk" + needlist.get(rnumber)+this.fileName)))); 
            fileOut.write(buf, 0, read);
            System.out.println("download  chunk " + needlist.get(rnumber)+ " from fileowner");
             
            fileOut.close();
            
            mylist.set(needlist.get(rnumber),1);
             
            sumOfList=0;
            for(int i = 0; i < chunkSize; i++){
                sumOfList += mylist.get(i);
            }
         
        }  dos.writeInt(-1);
           dos.flush();
           dis.close();
           dos.close();
           socket.close();
            
         
           int i = 0;  
           FileOutputStream outputStream = new FileOutputStream("pocsd1.pdf");
            
           while(i < chunkSize){
           String chunkFilename = "chunk" + i +this.fileName ;
           File chunkFile = new File(chunkFilename);
           
           FileInputStream inputstream = new FileInputStream(chunkFile);
           byte[] currentChunk = new byte [(int)chunkFile.length()];
           inputstream.read(currentChunk, 0 , (int)chunkFile.length());
           outputStream.write(currentChunk);
           outputStream.flush();
           i++;
         
        } //System.out.println("merge file successfully!");
           outputStream.close();
        
      }
    
              
        catch (Exception e) {   
          e.printStackTrace();   
          return;   
        } 
        
    } 
     
    public void neighborDownload(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        Socket socket=null; 
           
        try {   
            socket = new Socket(this.downloadIp,this.downloadPortno);   
          } catch (UnknownHostException e1) {   
            e1.printStackTrace();   
          } catch (IOException e1) {   
            //e1.printStackTrace();
              //System.out.println("waiting for 2 second to connect of client_2");
              try {
                Thread.sleep(2000);
            } catch (InterruptedException e11) {
                e11.printStackTrace();}
                 
            this.neighborDownload();
            return;
          }   
        DataInputStream dis=null; 
        DataOutputStream dos = null;
         
        try {   
            dis = new DataInputStream(new BufferedInputStream(socket   
                .getInputStream()));   
             dos = new DataOutputStream(socket.getOutputStream());
          } catch (IOException e1) {   
            //e1.printStackTrace();  
              //System.out.println("waiting for 2 second to connect of client_2");
              try {
                Thread.sleep(2000);
            } catch (InterruptedException e11) {
                e11.printStackTrace();}
                 
            this.neighborDownload();
            return;
          
          } 
        
        System.out.println("connected to download neighbor!");
         
        // 获取文件名称   
       try{
           int sumOfList=0;
           for(int i = 0; i < mylist.size(); i++){
            sumOfList += mylist.get(i);
           }
          
            
            
       
         
        //System.out.println("received list from download neighbor: " + receivelist); 
        ArrayList<Integer> needlist = new ArrayList<Integer>();
         
         
         
        while(sumOfList < mylist.size()) {
            System.out.println("own chunk list :" + mylist);
             
               if(sumOfList != mylist.size()){
                   dos.writeInt(-2);
                   dos.flush();
                   //System.out.println("send -2 to cient_1" );
               }
            ArrayList<Integer> receivelist = new ArrayList<Integer>();
            for(int m =0; m <mylist.size(); m++){
                receivelist.add(dis.readInt());
                
                
            }
            System.out.println("received list from download neighbor: " + receivelist); 
            needlist = new ArrayList<Integer>();
             
            for(int n =0; n <mylist.size(); n++){
                if(receivelist.get(n) - mylist.get(n) == 1){
                    needlist.add(n);
               }
            }//System.out.println("receive chunklist from download neighbor :" + receivelist);
            if(needlist.size() == 0 ){
                int sumofNeedlist=0;
                for(int i = 0; i < mylist.size(); i++){
                    sumofNeedlist += mylist.get(i);
                }
                if(sumofNeedlist == mylist.size()){
                    dos.writeInt(-1);
                    dos.flush();
                    break;
                }
                else{
                    dos.writeInt(-4);
                    dos.flush();
                    sleep(500);
                    //System.out.println("send -4 to cient_1" );
                   continue;
                }
                     
              
                 
            }else{
                dos.writeInt(-6);
                dos.flush();
                 //System.out.println("send -6 to cient_1" );
            }
         
             
            //System.out.println("needlist:(download from client_1)"+needlist);
            int read = 0;
            final double d = Math.random();
            final int rnumber = (int)(d*(needlist.size()-1));
            
             
            dos.writeInt(needlist.get(rnumber));
            System.out.println("reuquest chunk " +needlist.get(rnumber)+ " from download neighbor");
             
            dos.flush();
            int chunklength = dis.readInt();
            //System.out.println("chunklength: "+chunklength);
            byte[] buf = new byte[chunklength];
            read = dis.read(buf); 
            //System.out.println("read: "+ read);
         
            DataOutputStream fileOut = new DataOutputStream(   
                new BufferedOutputStream(new BufferedOutputStream(   
                    new FileOutputStream("chunk" + needlist.get(rnumber)+this.fileName)))); 
            fileOut.write(buf, 0, read);
            System.out.println("download  chunk " + needlist.get(rnumber)+" from download neighbor");
             
            fileOut.close();
            mylist.set(needlist.get(rnumber),1);
            sumOfList=0;
            for(int i = 0; i < mylist.size(); i++){
                sumOfList += mylist.get(i);
            }
            if(sumOfList == mylist.size()){
                dos.writeInt(-1);
                dos.flush();
                //System.out.println("send -1 to cient_1" );
            }
            else{
                dos.writeInt(-3);
                dos.flush();
                //System.out.println("send -3 to cient_1" );
            }
                
             
            }//if(sumOfList == mylist.size()){
                 
             
            
         
       // }  
            
           int i = 0;  
           FileOutputStream outputStream = new FileOutputStream("newpo.pdf");
            
           while(i < mylist.size()){
           String chunkFilename = "chunk" + i +this.fileName ;
           File chunkFile = new File(chunkFilename);
           
           FileInputStream inputstream = new FileInputStream(chunkFile);
           byte[] currentChunk = new byte [(int)chunkFile.length()];
           inputstream.read(currentChunk, 0 , (int)chunkFile.length());
           outputStream.write(currentChunk);
           outputStream.flush();
           i++;
         
        }System.out.println("merge file successfully!" );
           outputStream.close();
        
      }
    
              
        catch (Exception e) {   
          e.printStackTrace();   
          return;   
        } 
       finally {   
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
             if (socket != null)   
               socket.close();   
           } catch (IOException e) {   
           }
     }
    } 
    public void neighborUpload(){
         
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        DataOutputStream dos=null;  
        DataInputStream dis=null;
        DataInputStream dis1=null;
        ServerSocket ss = null; 
        Socket socket=null;  
        try { 
             
             
            ss=new ServerSocket(this.uploadPortno);  
            socket=ss.accept();
            System.out.println("connected to upload neighbor!");
            dos=new DataOutputStream(socket.getOutputStream());  
           // dis=new DataInputStream(new BufferedInputStream(new FileInputStream(this.fileName)));
             
            dis1=new DataInputStream(socket.getInputStream());
             
            
            while(true){
                 if(dis1.readInt() == -2){
                   // System.out.println("receive -2 from client_0");
                    for(int i = 0; i < mylist.size(); i++){
                        dos.writeInt(mylist.get(i));
                        dos.flush();
                    }
                    //System.out.println("mylist from client_1 : "+ mylist);
                     
                 };
                int specialnumber = dis1.readInt();
                //System.out.println("receive specialnumber from client_0" + specialnumber);
                if(specialnumber == -4){
                    //System.out.println("receive -4 from client_0");
                    continue;
                }
                else if(specialnumber == -1) {
                    break;
                     
                }
                int rnumber = dis1.readInt();
                System.out.println("receive request for chunk "+rnumber+" from upload neighbor");
                String chunkFilename = "chunk" + rnumber +this.fileName ;
                File chunkFile = new File(chunkFilename);
                dos.writeInt((int)chunkFile.length());
                dos.flush();
                FileInputStream inputstream = new FileInputStream(chunkFile);
                byte[] currentChunk = new byte [(int)chunkFile.length()];
                inputstream.read(currentChunk, 0 , (int)chunkFile.length());
                dos.write(currentChunk);
                dos.flush();
                System.out.println("upload chunk "+ rnumber + " to neighbor");
                int signal = dis1.readInt();
                if (signal == -3){
                    //System.out.println("receive -3 from client_0");
                    continue;
                }
                else{
                    break;
                }
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
     
     
    public static void main(String[] args) { 
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
        Client_1 client1 = new Client_1("pocsd.pdf", "localhost", Integer.parseInt(reader[0]),0); 
        client1.start();
        Client_1 client2 = new Client_1("pocsd.pdf", "localhost", Integer.parseInt(reader[1]),1); 
        client2.start();
        Client_1 client0 = new Client_1("pocsd.pdf", "localhost", Integer.parseInt(reader[2]),2); 
        client0.start();
      }    
}
