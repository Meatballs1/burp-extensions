package com.gdssecurity.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compression {

	public static byte[] inflate(byte data[],boolean noWrap) 
    { 
        if(data.length == 0) 
            return data; 
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length); 
        
        Inflater inflater = new Inflater(noWrap);
        boolean isDeflated = true;

        inflater.reset(); 
        inflater.setInput(data); 
        byte buf[] = new byte[1024];
            
        while(!inflater.finished() && isDeflated)  
        try 
        { 
        	int count = inflater.inflate(buf); 
            bos.write(buf, 0, count); 
        } 
        catch(DataFormatException e) 
        { 
        	isDeflated = false;
        } 
        finally 
        { 
            try
            {
            	bos.close();
            }
            catch(IOException e) 
            { 
           	 	System.out.print("IOException: " + e.getMessage()); 
            }
        } 
         
        if (!isDeflated)
        	return data;
        else
        {
        	return bos.toByteArray();
        }
    }

	public static void main(String [] args) throws IOException
	{
	
		File f = null;
		byte [] byteInput = null;
		byte [] byteResult = null;
		
		if (args.length == 3 && args[0].equalsIgnoreCase("inflate"))
		{
			f = new File(args[1]);
			FileInputStream fis = new FileInputStream (f);
			
			byteInput = new byte[(int)f.length()];
			
			int offset = 0;
			int numRead = 0;
			
			while (offset < byteInput.length && (numRead=fis.read(byteInput,offset,byteInput.length-offset)) >= 0)
			{
				offset +=numRead;
			}
			
			if (args[2].equalsIgnoreCase("RFC1950"))
				byteResult = inflate(byteInput,false);
			else if (args[2].equalsIgnoreCase("RFC1951"))
				byteResult = inflate(byteInput,true);
			else
				System.out.println("Please specify RFC compression format - 1950 or 1951");
		}
		else if (args.length == 4 && args[0].equalsIgnoreCase("deflate"))
		{
			f = new File(args[1]);
			FileInputStream fis = new FileInputStream (f);
			
			byteInput = new byte[(int)f.length()];
			
			int offset = 0;
			int numRead = 0;
			
			while (offset < byteInput.length && (numRead=fis.read(byteInput,offset,byteInput.length-offset)) >= 0)
			{
				offset +=numRead;
			}
			
			int compressLevel;
			try
			{
				 compressLevel = Integer.parseInt(args[2]) ;
				 if (compressLevel < 0 || compressLevel > 9)
					 System.out.println("Specify a compression level 1-9");
				 else
				 {
					 if (args[3].equalsIgnoreCase("RFC1950"))
							deflate(byteInput,compressLevel,false);
						else if (args[3].equalsIgnoreCase("RFC1951"))
							deflate(byteInput,compressLevel,true);
						else
							System.out.println("Please specify RFC compression format - 1950 or 1951");
				 }
			}
			catch (NumberFormatException nfex)
			{
				System.out.println("Specify a compression level 1-9");
			}
		}
		else
		{
			System.out.println("usage: com.gdssecurity.utils.Compression inflate [filename] RFC1950|RFC1951");
			System.out.println("or");
			System.out.println("usage: com.gdssecurity.utils.Compression deflate [filename] [0-9] RFC1950|RFC1951)");
			System.exit(-1);
		}
			
		if (byteResult != null)
		{
			if (!byteInput.equals(byteResult))
				System.out.println(new String(byteResult));
			else
				System.out.println("inflation/deflation operation failed - input and output are identical");
		}
	}

	public static byte[] deflate (byte data[], int level, boolean nowrap)
	 {
		 Deflater deflater = new Deflater(level,nowrap);
		 
		 ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
	     
		 deflater.reset();
	     
		 deflater.setInput(data);
	     
		 deflater.finish();
		 
		 byte buf[] = new byte[1024];
	     int count;
	     for(; !deflater.finished(); bos.write(buf, 0, count))
	    	 count = deflater.deflate(buf);
	     
	     try
	     {
	        bos.close();
	     }
	    catch(IOException e)
	     {
	    	System.out.print("IOException: " + e.getMessage()); 
	   	 	System.exit(-1);
	     }
	        
	     System.out.println(bos.toString());
	        
	     return bos.toByteArray();
	        
	 }
}
