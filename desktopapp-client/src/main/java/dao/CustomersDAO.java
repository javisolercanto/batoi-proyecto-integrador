package dao;

import api_rest_odoo.ConnectionAPI;
import java.net.MalformedURLException;
import java.util.*;

import javafx.scene.control.ProgressBar;
import models.BatoiLogicCustomer;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;
import tools.Tools;

import static java.util.Arrays.asList;

public class CustomersDAO implements GenericDAO<BatoiLogicCustomer>
{
    private final XmlRpcClient APIConnection;
    
    public CustomersDAO() throws MalformedURLException, XmlRpcException
    {
       APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicCustomer findByPk(int id) throws Exception
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
                    put("fields", asList("id", "name", "nickname", "password", "email", "phone", "addresses", "orders"));
                }}
        )));

        return (BatoiLogicCustomer) getSet(list).toArray()[0];
    }

    @Override
    public Set<BatoiLogicCustomer> findAllByPks(List<Integer> ids) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.customer", "search_read",
                asList(asList
                        (
                            asList("id","=",ids)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "nickname", "password", "email", "phone", "addresses", "orders"));
                }}
        )));

        return getSet(list);
    }
    
    public BatoiLogicCustomer findByNickname(String nickname) throws Exception 
    {
        List<Object> list;

        if(nickname.length() == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.customer", "search_read",
                asList(asList
                        (
                            asList("nickname","=",nickname)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "nickname", "password", "email", "phone", "addresses", "orders"));
                }}
        )));

        return (BatoiLogicCustomer) getSet(list).toArray()[0];
    }

    @Override
    public Set<BatoiLogicCustomer> findAll() throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.customer", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "nickname", "password", "email", "phone", "addresses", "orders"));
                }}
        )));

        return getSet(list);
    }

    @Override
    public Set<BatoiLogicCustomer> findAll(ProgressBar pbLoad) throws Exception {
        return null;
    }

    private Set<BatoiLogicCustomer> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicCustomer> customers = new HashSet<>();
        BatoiLogicCustomer customer;
        HashMap currentHashMap;
        for(Object h : list)
        {
            customer = new BatoiLogicCustomer();
            currentHashMap = (HashMap) h;

            customer.setId(OdooTypes.getInt(currentHashMap, "id"));
            customer.setName(OdooTypes.getString(currentHashMap, "name"));
            customer.setNickname(OdooTypes.getString(currentHashMap, "nickname"));
            customer.setPassword(OdooTypes.getString(currentHashMap, "password"));
            customer.setEmail(OdooTypes.getString(currentHashMap, "email"));
            customer.setPhone(OdooTypes.getInt(currentHashMap, "phone"));
            customer.setAddresses_id(OdooTypes.One2many(currentHashMap, "addresses"));
            customer.setOrders_id(OdooTypes.One2many(currentHashMap, "orders"));

            customers.add(customer);
        }

        return customers;
    }

    @Override
    public boolean insert(BatoiLogicCustomer object) throws Exception 
    {
        final Integer id = (Integer)APIConnection.execute("execute_kw", asList(
            ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
            "batoi_logic.customer", "create",
            asList(new HashMap() {{ 
                put("name", object.getName());
                put("nickname", object.getNickname());
                put("password", object.getPassword());
            }})
        ));
        
        return findByPk(id) != null;
    }

    @Override
    public boolean update(BatoiLogicCustomer object) throws Exception
    {
        APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.customer", "write",
                asList(
                        asList(object.getId()),
                        new HashMap()
                        {{
                            put("name", object.getName());
                            put("nickname", object.getNickname());
                            put("password", Tools.encryptThisSHA1(object.getPassword()));
                            put("email", object.getEmail());
                            put("phone", object.getPhone());
                        }}
                )
        ));

        return true;
    }

    @Override
    public boolean delete(int id) throws Exception
    {
        APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.customer", "unlink",
                asList(asList(id))));

        return findByPk(id) == null;
    }
}