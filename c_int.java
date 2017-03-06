
public class c_int {

	byte[] buf = new byte[4]; // little endian
    
    public int getSize()
    {
    	return buf.length;
    }
    
    public int getValue()
    {
    	int result = 0;
    	for (int i = 0; i < 4; i++) {
    		if (buf[i] < 0) {
    			result += (buf[i] + 256) * Math.pow(256,i);
    		}
    		else
    			result += buf[i] * Math.pow(256,i); 		
    	}
    	return result;
    }
    
    public void setValue(byte[] buf)
    {
    	this.buf = buf;
    }
    
    public void setValue(int v)
    {
    	
    }
    
    public byte[] toByte()
    {
    	return buf;
    }

}
