package dam.android.sergic.app2.dao;

import java.net.MalformedURLException;
import java.util.*;
import dam.android.sergic.app2.models.Customer;
import dam.android.sergic.app2.odoo.ConnectionAPI;
import dam.android.sergic.app2.odoo.OdooTypes;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import static java.util.Arrays.asList;

public class CustomersDAO implements GenericDAO<Customer>
{
    private final XmlRpcClient APIConnection;
    
    public CustomersDAO() throws MalformedURLException, XmlRpcException
    {
       APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public Customer findByPk(int id) throws Exception
    {
        List<Object> list;

        if(id == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.customer", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "email", "phone"));
                }}
        )));

        return (Customer) getSet(list).toArray()[0];
    }

    @Override
    public Set<Customer> findAllByPks(List<Integer> ids) throws Exception
    {
        return null;
    }

    @Override
    public Set<Customer> findAll() throws Exception
    {
        return null;
    }

    private Set<Customer> getSet(List<Object> list)
    {
        Set<Customer> customers = new HashSet<>();
        Customer customer;
        HashMap currentHashMap;
        for(Object h : list)
        {
            customer = new Customer();
            currentHashMap = (HashMap) h;

            customer.setId(OdooTypes.getInt(currentHashMap, "id"));
            customer.setName(OdooTypes.getString(currentHashMap, "name"));
            customer.setEmail(OdooTypes.getString(currentHashMap, "email"));
            customer.setPhone(OdooTypes.getInt(currentHashMap, "phone"));

            customers.add(customer);
        }

        return customers;
    }

    @Override
    public boolean insert(final Customer object) throws Exception
    {
       return false;
    }

    @Override
    public boolean update(final Customer object) throws Exception
    {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception
    {
        return false;
    }
}