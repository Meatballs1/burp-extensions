


public class BurpExtender implements IBurpExtender {

	private static boolean debug = false;
	
	public BurpExtender () {}
	
	public void setCommandLineArgs(String[] args) {
		
		if (args[0] != null)
		{
			if (args[0].equalsIgnoreCase("debug"))
			{
				debug = true;
			}
		}
	};
	
	public byte[] processProxyMessage(
            int messageReference,
            boolean messageIsRequest,
            String remoteHost,
            int remotePort,
            boolean serviceIsHttps,
            String httpMethod,
            String url,
            String resourceType,
            String statusCode,
            String responseContentType,
            byte[] message,
            int[] action)
	{
		
		if (messageIsRequest == false)
		{
	
			if (debug)
				System.out.println("DEBUG: Response for URL: " + url);
			
			String strMessage = new String(message);
			
			String[] strHeadersAndContent = strMessage.split("\\r\\n\\r\\n");
			
			byte[] byteOrigMessageContent = strHeadersAndContent[1].getBytes();
			
			// RFC1950 Compression?
			byte[] byteInflatedMessageContent = com.gdssecurity.utils.Compression.inflate(byteOrigMessageContent,false);
			
			if (debug)
			{
				if (!byteInflatedMessageContent.equals(byteOrigMessageContent))
				{
					System.out.println("DEBUG: Using RFC1950 compression");
				}
			}
			
			// RFC1951 Compression?
			if (byteInflatedMessageContent.equals(byteOrigMessageContent))
			{
				byteInflatedMessageContent = com.gdssecurity.utils.Compression.inflate(byteOrigMessageContent,true);
				
				if (debug)
				{
					if (!byteInflatedMessageContent.equals(byteOrigMessageContent))
					{
						System.out.println("DEBUG: Using RFC1951 compression");
					}
				}
			}
			
			/* If message contents have been changed, 
			*  1) Update Content-Length header 
			*  2) Remove Content-Encoding: deflate header
			*/
			
			if (!byteInflatedMessageContent.equals(byteOrigMessageContent))
			{
				
				strHeadersAndContent[0] = com.gdssecurity.utils.HttpHelper.updateContentLength(strHeadersAndContent[0],byteInflatedMessageContent.length);

				strHeadersAndContent[0] = com.gdssecurity.utils.HttpHelper.removeHttpHeader(strHeadersAndContent[0],"Content-Encoding: deflate\\r\\n");

				strHeadersAndContent[1] = new String(byteInflatedMessageContent);
				
				return (strHeadersAndContent[0] + "\r\n\r\n" + strHeadersAndContent[1]).getBytes();
			}
			else
			{
				return message;
			}
		}
		
		return message;
	}
	
	public void registerHttpRequestMethod(
            java.lang.reflect.Method makeHttpRequestMethod, 
            Object makeHttpRequestObject) {}
	
	public void applicationClosing() {}
}
