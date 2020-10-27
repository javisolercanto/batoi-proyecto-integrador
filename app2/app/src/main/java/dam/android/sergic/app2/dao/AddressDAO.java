package dam.android.sergic.app2.dao;

import java.net.MalformedURLException;
import java.util.*;
import dam.android.sergic.app2.models.Address;
import dam.android.sergic.app2.models.PostalCode;
import dam.android.sergic.app2.odoo.ConnectionAPI;
import dam.android.sergic.app2.odoo.OdooTypes;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import static java.util.Arrays.asList;

public class AddressDAO implements GenericDAO<Address>
{
    private XmlRpcClient APIConnection;

    public AddressDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public Address findByPk(int id) throws Exception
    {
        List<Object> list;

        if(id == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.address", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id","name", "postal_code_id"));
                }}
        )));

        return (Address) getSet(list).toArray()[0];
    }

    @Override
    public Set<Address> findAllByPks(List<Integer> ids) throws Exception
    {
        return null;
    }

    @Override
    public Set<Address> findAll() throws Exception
    {
        return null;
    }

    private Set<Address> getSet(List<Object> list) throws Exception
    {
        Set<Address> addresses = new HashSet<>();
        Address address;
        HashMap currentHashMap;
        for(Object h : list)
        {
            address = new Address();
            currentHashMap = (HashMap) h;

            address.setId(OdooTypes.getInt(currentHashMap, "id"));
            address.setStreet(OdooTypes.getString(currentHashMap, "name"));

            PostalCode pc = (PostalCode) OdooTypes.Many2one(currentHashMap, "postal_code_id",
                    PostalCodeDAO.class.getName());

            address.setZip(pc.getZip());
            address.setCity(pc.getCity().getName());
            address.setProvince(pc.getCity().getProvince());

            addresses.add(address);
        }

        return addresses;
    }

    @Override
    public boolean insert(Address object) throws Exception {
        return false;
    }

    @Override
    public boolean update(Address object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}