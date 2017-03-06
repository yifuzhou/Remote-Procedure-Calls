
public class c_char {
	byte[] buf = new byte[1]; // little endian
    
    public int getSize()
    {
    	return buf.length;
    }
    
    public int getValue()
    {
    	return 0;
    }
    
    public void setValue(String s)
    {
    	
    }
    
    public void setValue(int v)
    {
    	
    }
    
    public byte[] toByte()
    {
    	return buf;
    }
}
