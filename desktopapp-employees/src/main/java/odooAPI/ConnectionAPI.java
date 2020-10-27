package odooAPI;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;

public class ConnectionAPI
{
    public static final String url = "http://grup2.cipfpbatoi.es";
    public static final String db = "batoi_logic_db";
    public static final String username = "demo@gmail.com";
    public static final String password = "ser";

    private static XmlRpcClient api;
    public static Object uid;

    private ConnectionAPI()
    {
        api = null;
        uid = 0;
    }

    public static XmlRpcClient getAPIConnection() throws MalformedURLException, XmlRpcException
    {
        if(api==null)
        {
            api = new XmlRpcClient();
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", url)));

            uid = api.execute(config, "authenticate", asList(db, username, password, emptyMap()));

            XmlRpcClientConfigImpl config2 = new XmlRpcClientConfigImpl();
            config2.setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
            api.setConfig(config2);
        }

        return api;
    }
}