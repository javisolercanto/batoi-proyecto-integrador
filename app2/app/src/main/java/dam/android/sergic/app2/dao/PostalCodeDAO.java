package dam.android.sergic.app2.dao;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dam.android.sergic.app2.models.Address;
import dam.android.sergic.app2.models.City;
import dam.android.sergic.app2.models.PostalCode;
import dam.android.sergic.app2.odoo.ConnectionAPI;
import dam.android.sergic.app2.odoo.OdooTypes;

import static java.util.Arrays.asList;

public class PostalCodeDAO implements GenericDAO<PostalCode>
{
    private XmlRpcClient APIConnection;

    public PostalCodeDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public PostalCode findByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.postal_code", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("name", "city_id"));
                }}
        )));

        return (PostalCode) getSet(list).toArray()[0];
    }

    @Override
    public Set<PostalCode> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<PostalCode> findAll() throws Exception {
        return null;
    }

    private Set<PostalCode> getSet(List<Object> list) throws Exception
    {
        Set<PostalCode> postalCodes = new HashSet<>();
        PostalCode postalCode;
        HashMap currentHashMap;
        for(Object h : list)
        {
            postalCode = new PostalCode();
            currentHashMap = (HashMap) h;

            postalCode.setZip(OdooTypes.getInt(currentHashMap, "name"));
            postalCode.setCity((City) OdooTypes.Many2one(currentHashMap, "city_id",
                    CityDAO.class.getName()));

            postalCodes.add(postalCode);
        }

        return postalCodes;
    }

    @Override
    public boolean insert(PostalCode object) throws Exception {
        return false;
    }

    @Override
    public boolean update(PostalCode object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}