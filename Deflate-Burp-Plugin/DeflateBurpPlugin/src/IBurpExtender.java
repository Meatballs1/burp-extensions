

/**
 * This interface allows third-party code to extend burp suite's functionality.
 *
 * Implementations must be called BurpExtender, must be declared public, and 
 * must provide a default (public, no-argument) constructor. On startup, burp 
 * searches its classpath for a class called BurpExtender, and attempts to 
 * dynamically load and instantiate this class. The IBurpExtender methods
 * implemented will then be dynamically invoked as appropriate.<p>
 *
 * Partial implementations are acceptable. The class will be used provided at
 * least one of the interface's methods is implemented. A single instance of 
 * BurpExtender will be instantiated and shared between all plugins.<p>
 *
 * To make use of the interface, create a class called BurpExtender which
 * implements one or more methods of the interface, and place this into the 
 * application's classpath at startup. For example, if burp.jar contains the 
 * burp suite or individual plugin, and BurpProxyExtender.jar contains the 
 * class BurpExtender, use the following command to launch burp and load the 
 * IBurpExtender implementation:<p>
 *
 * <PRE>
 *    java -classpath burp.jar;BurpProxyExtender.jar burp.StartBurp
 * </PRE>
 */
public interface IBurpExtender
{
    /**
     * This method is invoked immediately after the implementation's constructor
     * to pass the command-line arguments passed to the calling application on
     * startup. It allows implementations to control aspects of their behaviour
     * at runtime by defining their own command-line arguments
     *
     * @param args The command-line arguments passed to the calling application
     * on startup.
     */
    public void setCommandLineArgs(String[] args);
    
    
    /**
     * This method is invoked by burp proxy whenever a client request or server
     * response is received. It allows implementations to perform logging 
     * functions, modify the message, specify an action (intercept, drop, etc.)
     * and perform any other arbitrary processing.
     *
     * @param messageReference An identifier which is unique to a single 
     * request/response pair. This can be used to correlate details of requests
     * and responses and perform processing on the response message accordingly.
     * @param messageIsRequest Flags whether the message is a client request or
     * a server response.
     * @param remoteHost The hostname of the remote HTTP server.
     * @param remotePort The port of the remote HTTP server.
     * @param serviceIsHttps Flags whether the protocol is HTTPS or HTTP.
     * @param httpMethod The method verb used in the client request.
     * @param url The requested URL.
     * @param resourceType The filetype of the requested resource, or a 
     * zero-length string if the resource has no filetype.
     * @param statusCode The HTTP status code returned by the server. This value
     * is <code>null</code> for request messages.
     * @param responseContentType The content-type string returned by the 
     * server. This value is <code>null</code> for request messages.
     * @param message The full HTTP message.
     * @param action An array containing a single integer, allowing the
     * implementation to communicate back to burp a non-default interception
     * action for the message. The default value is 
     * <code>ACTION_FOLLOW_RULES</code>. Set <code>action[0]</code> to one of 
     * the other possible values to perform a different action.
     * @return Implementations should return either (a) the same object received
     * in the <code>message</code> paramater, or (b) a different object 
     * containing a modified message.
     */
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
            int[] action);
    
    /** 
     * Causes burp proxy to follow the current interception rules to determine
     * the appropriate action to take for the message.
     */
    public final static int ACTION_FOLLOW_RULES = 0;
    /** 
     * Causes burp proxy to present the message to the user for manual
     * review or modification.
     */
    public final static int ACTION_DO_INTERCEPT = 1;
    /** 
     * Causes burp proxy to forward the message to the remote server.
     */
    public final static int ACTION_DONT_INTERCEPT = 2;
    /** 
     * Causes burp proxy to drop the message and close the client connection.
     */
    public final static int ACTION_DROP = 3;    
    
    
    
    /**
     * This method is invoked on startup by burp repeater. It registers a method
     * that may be invoked by the implementation to issue arbitrary HTTP/S
     * requests and receive responses.
     * <p>
     *
     * The method registered has the following signature:<p>
     *
     * <PRE>
     *     public byte[] makeHttpRequest(
     *         String host,
     *         int port,
     *         boolean useHttps,
     *         byte[] request) throws Exception;
     * </PRE>
     *
     * The makeHttpRequest method returns the full response from the server, or 
     * throws an Exception with an informative message if any kind of error 
     * occurs.<p>
     *
     * Implementations must invoke the makeHttpRequest method dynamically, for 
     * example:<p>
     *
     * <PRE>
     *     byte[] response = (byte[]) makeHttpRequestMethod.invoke(
     *         makeHttpRequestObject,
     *         new Object[] 
     *         {
     *             "target.com",
     *             new Integer(80),
     *             new Boolean(false),
     *             "GET / HTTP/1.0\r\n\r\n".getBytes()
     *         });
     * </PRE>
     *
     * The call to registerHttpRequestMethod need not return, and 
     * implementations may use the invoking thread for any purpose.<p>
     *
     * @param makeHttpRequestMethod A method which may be dynamically invoked by
     * implementations to issue arbitrary HTTP/S requests.
     * @param makeHttpRequestObject An object upon which the method must be
     * invoked.
     */
    public void registerHttpRequestMethod(
            java.lang.reflect.Method makeHttpRequestMethod, 
            Object makeHttpRequestObject);
    
    
    
    /**
     * This method is invoked immediately before the calling application exits. 
     * It allows implementations to carry out any clean-up actions necessary
     * (e.g. flushing log files or closing database resources)
     */
    public void applicationClosing();
}
