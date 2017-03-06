import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.net.*;
import java.io.*;

public class GetLocatlOS {

    public c_char[] OS = new c_char[20];
    public c_char valid;
    
    public GetLocatlOS()
    {
    	valid = new c_char();
    	//OS = new char[20];
    }
     
    public int execute(String IP, int port)
    {
    	int length = OS.length;
    	byte[] buf = new byte[100+4+length];
    	int offset = 0;

    	String str = "GetLocalOS";
    	byte[] str_b = str.getBytes();
    	for (int i = 0; i < str_b.length; i++) {
    		buf[offset + i] = str_b[i];
    	}
    	offset = 100;
    	
    	byte[] length_b = InttoBytes(length);
    	for (int i = 0; i < length_b.length; i++) {
    		buf[offset + i] = length_b[i];
    	}
    	offset = 104;

    	
    	//create a socket to send
    	try {
            Socket socket=new Socket(IP, port);
            
            // create text reader and writer
    		DataInputStream inStream  = new DataInputStream(socket.getInputStream());
    		DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

    		byte[] bufLengthInBinary = InttoBytes(buf.length);
    		
    		// send 4 bytes
    		outStream.write(bufLengthInBinary, 0, bufLengthInBinary.length);
    		// send the string
    		outStream.write(buf, 0, buf.length);
    		outStream.flush();
    		
    		// read the data back
            inStream.readFully(bufLengthInBinary); // ignore the first 4 bytes
            inStream.readFully(buf); 
            char[]os_value = new char[20];
            for (int i = 0; i < 20; i++) {
            	os_value[i] = (char)buf[104 + i];
            }
            
            System.out.println(os_value);

         
            // convert the binary bytes to string
            String ret = new String(buf);
            
            
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    	return 0;
    }
    
	static private byte[] InttoBytes(int i)
	{
	  byte[] result = new byte[4];

	  result[0] = (byte) (i >> 24);
	  result[1] = (byte) (i >> 16);
	  result[2] = (byte) (i >> 8);
	  result[3] = (byte) (i /*>> 0*/);

	  return result;
	}
}
