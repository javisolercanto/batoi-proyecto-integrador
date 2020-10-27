package odooAPI;

import java.net.MalformedURLException;
import java.util.*;
import java.util.List;
import javafx.scene.image.Image;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;
import static java.util.Arrays.asList;

public class SimpleProductDAO
{
    private XmlRpcClient APIConnection;

    public SimpleProductDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    public Image findImageByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.product", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("image", "providers_available"));
                }}
        )));

        if(list.size()>0)
        {
            HashMap currentHashMap = (HashMap) list.get(0);
            return OdooTypes.getImage(currentHashMap, "image");
        }

        return null;
    }

    public List<Integer> findProvidersByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.product", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("providers_available"));
                }}
        )));

        if(list.size()>0)
        {
            HashMap currentHashMap = (HashMap) list.get(0);
            return OdooTypes.One2many(currentHashMap, "providers_available");
        }

        return null;
    }
}