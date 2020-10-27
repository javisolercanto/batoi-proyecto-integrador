package dam.android.sergic.app2.dao;

import java.net.MalformedURLException;
import java.util.*;

import dam.android.sergic.app2.models.Address;
import dam.android.sergic.app2.models.Customer;
import dam.android.sergic.app2.models.Order;
import dam.android.sergic.app2.odoo.ConnectionAPI;
import dam.android.sergic.app2.odoo.OdooTypes;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import static java.util.Arrays.asList;

public class OrderDAO implements GenericDAO<Order>
{
    private XmlRpcClient APIConnection;

    public OrderDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public Order findByPk(int id) throws Exception
    {
        List<Object> list;

        if(id == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "search_read",
                asList(asList
                        (
                            asList("id","=",id),
                            asList("status","=",asList("1","2"))
                        )),
                new HashMap()
                {{
                    put("fields", asList("name", "date", "status", "information", "customer_id", "address_id"));
                }}
        )));

        return (Order) (getSet(list).size()>0? getSet(list).toArray()[0]: null);
    }

    @Override
    public Set<Order> findAllByPks(List<Integer> ids) throws Exception
    {
        return null;
    }

    @Override
    public Set<Order> findAll() throws Exception
    {
        return null;
    }

    private Set<Order> getSet(List<Object> list) throws Exception
    {
        Set<Order> orders = new HashSet<>();
        Order order;
        HashMap currentHashMap;
        for(Object h : list)
        {
            order = new Order();
            currentHashMap = (HashMap) h;

            order.setOrderNumber(OdooTypes.getString(currentHashMap, "name"));
            order.setDate(OdooTypes.getString(currentHashMap, "date"));
            order.setStatus(Integer.parseInt(OdooTypes.getString(currentHashMap, "status")));
            order.setAbout(OdooTypes.getString(currentHashMap, "about"));

            order.setCustomer((Customer) OdooTypes.Many2one(currentHashMap, "customer_id",
                    CustomersDAO.class.getName()));
            order.setAddress((Address) OdooTypes.Many2one(currentHashMap,"address_id",
                    AddressDAO.class.getName()));

            update(OdooTypes.getInt(currentHashMap, "id"));
            orders.add(order);
        }

        return orders;
    }

    @Override
    public boolean insert(Order object) throws Exception
    {
        return false;
    }

    @Override
    public boolean update(Order obj) throws Exception
    {
        return false;
    }

    public boolean update(int id) throws Exception
    {
        APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "write",
                asList(
                        asList(id),
                        new HashMap()
                        {{
                            put("status", "2");             // Updating status from READY to OUT FOR DELIVERY.
                        }}
                )
        ));

        return true;
    }

    @Override
    public boolean delete(int id) throws Exception
    {
        return false;
    }
}