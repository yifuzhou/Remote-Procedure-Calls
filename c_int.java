
public class c_int {

	byte[] buf = new byte[4]; // little endian
    
    public int getSize()
    {
    	return buf.length;
    }
    
    public int getValue()
    {
    	return 0;
    }
    
    public void setValue(byte[] buf)
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
