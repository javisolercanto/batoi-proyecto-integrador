package dao;

import java.net.MalformedURLException;
import java.util.*;
import api_rest_odoo.ConnectionAPI;
import javafx.scene.control.ProgressBar;
import models.BatoiLogicAddress;
import models.BatoiLogicCustomer;
import models.BatoiLogicOrder;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;
import static java.util.Arrays.asList;

public class OrderDAO implements GenericDAO<BatoiLogicOrder>
{
    private XmlRpcClient APIConnection;

    public OrderDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicOrder findByPk(int id) throws Exception
    {
        List<Object> list;

        if(id == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "search_read",
                asList(asList
                        (
                                asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "date", "status", "information", "customer_id", "lines", "address_id"));
                }}
        )));

        return (BatoiLogicOrder) (getSet(list).size()>0? getSet(list).toArray()[0]: null);
    }
    
    public BatoiLogicOrder findByName(String name) throws Exception
    {
        List<Object> list;

        if(name.length() == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "search_read",
                asList(asList
                        (
                                asList("name","=",name)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "date", "status", "information", "customer_id", "lines", "address_id"));
                }}
        )));

        return (BatoiLogicOrder) (getSet(list).size()>0? getSet(list).toArray()[0]: null);
    }

    @Override
    public Set<BatoiLogicOrder> findAllByPks(List<Integer> ids) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "search_read",
                asList(asList
                        (
                                asList("id","=",ids)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "date", "status", "information", "customer_id", "lines", "address_id"));
                }}
        )));

        return getSet(list);
    }

    public Set<BatoiLogicOrder> findAllBy(List<Integer> ids, ProgressBar pb) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "search_read",
                asList(asList
                        (
                                asList("id","=",ids)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "date", "status", "information", "customer_id", "lines", "address_id"));
                }}
        )));

        return getSet(list, pb);
    }

    @Override
    public Set<BatoiLogicOrder> findAll() throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "date", "status", "information", "customer_id", "lines", "address_id"));
                }}
        )));

        return getSet(list);
    }

    @Override
    public Set<BatoiLogicOrder> findAll(ProgressBar pbLoad) throws Exception {
        return null;
    }

    private Set<BatoiLogicOrder> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicOrder> orders = new HashSet<>();
        BatoiLogicOrder order;
        HashMap currentHashMap;
        for(Object h : list)
        {
            order = new BatoiLogicOrder();
            currentHashMap = (HashMap) h;

            order.setId(OdooTypes.getInt(currentHashMap, "id"));
            order.setName(OdooTypes.getString(currentHashMap, "name"));
            order.setDate(OdooTypes.getDate(currentHashMap, "date"));
            order.setStatus(OdooTypes.getString(currentHashMap, "status"));
            order.setInformation(OdooTypes.getString(currentHashMap, "information"));

            order.setBatoiLogicCustomer((BatoiLogicCustomer) OdooTypes.Many2one(currentHashMap, "customer_id",
                    CustomersDAO.class.getName()));
            order.setLines_id(OdooTypes.One2many(currentHashMap, "lines"));
            order.setBatoiLogicAddress((BatoiLogicAddress) OdooTypes.Many2one(currentHashMap,"address_id",
                    AddressDAO.class.getName()));

            orders.add(order);
        }

        return orders;
    }

    private Set<BatoiLogicOrder> getSet(List<Object> list, ProgressBar pb) throws Exception
    {
        Set<BatoiLogicOrder> orders = new HashSet<>();
        BatoiLogicOrder order;
        HashMap currentHashMap;

        double progress = 1.0 / list.size();
        double totalProgress = 0.0;
        for(Object h : list)
        {
            order = new BatoiLogicOrder();
            currentHashMap = (HashMap) h;

            order.setId(OdooTypes.getInt(currentHashMap, "id"));
            order.setName(OdooTypes.getString(currentHashMap, "name"));
            order.setDate(OdooTypes.getDate(currentHashMap, "date"));
            order.setStatus(OdooTypes.getString(currentHashMap, "status"));
            order.setInformation(OdooTypes.getString(currentHashMap, "information"));

            order.setBatoiLogicCustomer((BatoiLogicCustomer) OdooTypes.Many2one(currentHashMap, "customer_id",
                    CustomersDAO.class.getName()));
            order.setLines_id(OdooTypes.One2many(currentHashMap, "lines"));
            order.setBatoiLogicAddress((BatoiLogicAddress) OdooTypes.Many2one(currentHashMap,"address_id",
                    AddressDAO.class.getName()));

            orders.add(order);

            totalProgress += progress;
            pb.setProgress(totalProgress);
        }
        return orders;
    }

    @Override
    public boolean insert(BatoiLogicOrder object) throws Exception
    {
        final Integer id = (Integer)APIConnection.execute("execute_kw", asList(
            ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
            "batoi_logic.order", "create",
            asList(new HashMap() {{ 
                put("name", object.getName());
                put("date", object.getDate());
                put("status", object.getStatus());
                put("information", object.getInformation());
                put("customer_id", object.getBatoiLogicCustomer().getId());
                put("lines", object.getLines_id());
                put("address_id", object.getBatoiLogicAddress().getId());
            }})
        ));
        
        return findByPk(id) != null;
    }
    
    public int insertNoLines(BatoiLogicOrder object) throws Exception
    {
        final Integer id = (Integer)APIConnection.execute("execute_kw", asList(
            ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
            "batoi_logic.order", "create",
            asList(new HashMap() {{ 
                put("name", object.getName());
                put("date", object.getDate().toString());
                put("status", object.getStatus());
                put("information", object.getInformation());
                put("customer_id", object.getBatoiLogicCustomer().getId());
                put("address_id", object.getBatoiLogicAddress().getId());
            }})
        ));
        
        return id;
    }

    @Override
    public boolean update(BatoiLogicOrder object) throws Exception
    {
        APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "write",
                asList(
                        asList(object.getId()),
                        new HashMap()
                        {{
                            put("name", object.getName());
                            put("lines", object.getLines_id());
                            put("information", object.getInformation());
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
                "batoi_logic.order", "unlink",
                asList(asList(id))));

        return findByPk(id) == null;
    }
    
    public int getMaxOrderNumber() throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "date", "status", "information", "customer_id", "lines", "address_id"));
                }}
        )));
        
        int max = 0;
        for (BatoiLogicOrder o : getSet(list)) {
            if (Integer.parseInt(o.getName()) > max) max = Integer.parseInt(o.getName());
        }
        
        return max;
    }
}