package dao;

import java.net.MalformedURLException;
import java.util.*;
import api_rest_odoo.ConnectionAPI;
import javafx.scene.control.ProgressBar;
import models.BatoiLogicAddress;
import models.BatoiLogicCustomer;
import models.BatoiLogicPostalCode;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;
import static java.util.Arrays.asList;

public class AddressDAO implements GenericDAO<BatoiLogicAddress>
{
    private XmlRpcClient APIConnection;

    public AddressDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicAddress findByPk(int id) throws Exception
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
                    put("fields", asList("id", "name", "customer_id", "postal_code_id", "orders_id"));
                }}
        )));

        return getSet(list).size() > 0 ? (BatoiLogicAddress) getSet(list).toArray()[0] : null;
    }

    @Override
    public Set<BatoiLogicAddress> findAllByPks(List<Integer> ids) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.address", "search_read",
                asList(asList
                        (
                            asList("id","=",ids)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "customer_id", "postal_code_id", "orders_id"));
                }}
        )));

        return getSet(list);
    }

    @Override
    public Set<BatoiLogicAddress> findAll() throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.address", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "customer_id", "postal_code_id", "orders_id"));
                }}
        )));

        return getSet(list);
    }

    @Override
    public Set<BatoiLogicAddress> findAll(ProgressBar pbLoad) throws Exception
    {
        return null;
    }

    private Set<BatoiLogicAddress> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicAddress> addresses = new HashSet<>();
        BatoiLogicAddress address;
        HashMap currentHashMap;
        for(Object h : list)
        {
            address = new BatoiLogicAddress();
            currentHashMap = (HashMap) h;

            address.setId(OdooTypes.getInt(currentHashMap, "id"));
            address.setName(OdooTypes.getString(currentHashMap, "name"));
            address.setBatoiLogicCustomer((BatoiLogicCustomer) OdooTypes.Many2one(currentHashMap, "customer_id",
                    CustomersDAO.class.getName()));
            address.setBatoiLogicPostalCode((BatoiLogicPostalCode) OdooTypes.Many2one(currentHashMap, "postal_code_id",
                    PostalCodeDAO.class.getName()));
            address.setOrders(OdooTypes.One2many(currentHashMap, "orders_id"));

            addresses.add(address);
        }

        return addresses;
    }

    @Override
    public boolean insert(BatoiLogicAddress object) throws Exception {
        final Integer id = (Integer)APIConnection.execute("execute_kw", asList(
            ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
            "batoi_logic.address", "create",
            asList(new HashMap() {{ 
                put("name", object.getName());
                put("customer_id", object.getBatoiLogicCustomer().getId());
                put("postal_code_id", object.getBatoiLogicPostalCode().getId());
            }})
        ));
        
        return findByPk(id) != null;
    }

    @Override
    public boolean update(BatoiLogicAddress object) throws Exception {
        APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.address", "write",
                asList(
                        asList(object.getId()),
                        new HashMap()
                        {{
                            put("name", object.getName());
                            put("customer_id", object.getBatoiLogicCustomer().getId());
                            put("postal_code_id", object.getBatoiLogicPostalCode().getId());
                        }}
                )
        ));

        return true;
    }

    @Override
    public boolean delete(int id) throws Exception {
        APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.address", "unlink",
                asList(asList(id))));

        return findByPk(id) == null;
    }
}