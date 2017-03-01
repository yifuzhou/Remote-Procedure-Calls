import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;  
import java.io.OutputStream; 

public class GetLocalTime {

	public c_int time; 
    public c_char valid;
    
    public GetLocalTime()
    {
    	time = new c_int();
    	valid = new c_char();
    }
     
    public int execute(String IP, int port)
    {
    	int length = time.getSize()+valid.getSize();
    	byte[] buf = new byte[100+4+length];
    	int offset = 0;
    	System.out.println(length);

    	String str = "GetLocalTime";
    	byte[] str_b = str.getBytes();
    	for (int i = 0; i < str_b.length; i++) {
    		buf[offset + i] = str_b[i];
    	}
    	offset = 100;
    	
    	byte[] length_b = int2byte(length);
    	System.out.println(length_b.length);
    	for (int i = 0; i < length_b.length; i++) {
    		buf[offset + i] = length_b[i];
    	}
    	offset = 104;
    	
    	byte[] time_b = time.toByte();
    	for (int i = 0; i < time_b.length; i++) {
    		buf[offset + i] = time_b[i];
    	}
    	offset = offset + time_b.length;
    	
    	byte[] valid_b = valid.toByte();
    	for (int i = 0; i < valid_b.length; i++) {
    		buf[offset + i] = valid_b[i];
    	}
    	offset = offset + valid_b.length;
    	
    	System.out.println(offset);
    	System.out.println(buf);
    	
    	//create a socket to send
    	try {
            //Create Socket object
            Socket socket=new Socket("localhost",8888);

            OutputStream  out = socket.getOutputStream();
            out.write(buf);

            
            out.close();
            socket.close();
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    	return 0;
    }
    
    public byte[] int2byte(int i) 
    {  
    	byte[] result = new byte[4];  
    	  
    	result[0] = (byte) (i & 0xff); 
    	result[1] = (byte) ((i >> 8) & 0xff);  
    	result[2] = (byte) ((i >> 16) & 0xff);  
    	result[3] = (byte) (i >>> 24);   
    	return result; 
    }
    
    public static void main(String args[])
    {
    	GetLocalTime gt = new GetLocalTime();
    	gt.execute("", 1);
    }

}
