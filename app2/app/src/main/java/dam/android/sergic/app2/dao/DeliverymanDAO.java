package dam.android.sergic.app2.dao;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dam.android.sergic.app2.models.Deliveryman;
import dam.android.sergic.app2.odoo.ConnectionAPI;
import dam.android.sergic.app2.odoo.OdooTypes;

import static java.util.Arrays.asList;

public class DeliverymanDAO implements GenericDAO<Deliveryman>
{
    private final XmlRpcClient APIConnection;

    public DeliverymanDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public Deliveryman findByPk(int id) throws Exception {
        return null;
    }

    @Override
    public Set<Deliveryman> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    public Deliveryman findByNickname(String nickname) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.delivery_man", "search_read",
                asList(asList
                        (
                            asList("nickname","=",nickname)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "nickname", "password", "email", "phone", "routes_id", "lorry_id"));
                }}
        )));

        return (Deliveryman) (getSet(list).size()>0? getSet(list).toArray()[0] : null);
    }

    @Override
    public Set<Deliveryman> findAll() throws Exception {
        return null;
    }

    private Set<Deliveryman> getSet(List<Object> list) throws Exception
    {
        Set<Deliveryman> deliverymen = new HashSet<>();
        Deliveryman deliveryman;
        HashMap currentHashMap;
        for(Object h : list)
        {
            deliveryman = new Deliveryman();
            currentHashMap = (HashMap) h;

            deliveryman.setId(OdooTypes.getInt(currentHashMap, "id"));
            deliveryman.setName(OdooTypes.getString(currentHashMap, "name"));
            deliveryman.setNickname(OdooTypes.getString(currentHashMap, "nickname"));
            deliveryman.setPassword(OdooTypes.getString(currentHashMap, "password"));
            deliveryman.setEmail(OdooTypes.getString(currentHashMap, "email"));
            deliveryman.setPhone(OdooTypes.getInt(currentHashMap, "phone"));

            // TODO OTHER ATTRIBUTES

            deliverymen.add(deliveryman);
        }

        return deliverymen;
    }

    @Override
    public boolean insert(Deliveryman object) throws Exception {
        return false;
    }

    @Override
    public boolean update(Deliveryman object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}