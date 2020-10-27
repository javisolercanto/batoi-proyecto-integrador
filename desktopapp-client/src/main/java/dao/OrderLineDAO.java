package dao;

import java.net.MalformedURLException;
import java.util.*;
import api_rest_odoo.ConnectionAPI;
import javafx.scene.control.ProgressBar;
import models.*;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;

import static java.util.Arrays.asList;

public class OrderLineDAO implements GenericDAO<BatoiLogicOrderLine>
{
    private XmlRpcClient APIConnection;

    public OrderLineDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicOrderLine findByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order_line", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "quantity", "order_id", "product_id"));
                }}
        )));

        return (BatoiLogicOrderLine) (getSet(list).size()>0? getSet(list).toArray()[0]: null);
    }

    @Override
    public Set<BatoiLogicOrderLine> findAllByPks(List<Integer> ids) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order_line", "search_read",
                asList(asList
                        (
                            asList("id","=",ids)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "quantity", "order_id", "product_id"));
                }}
        )));

        return getSet(list);
    }

    public Set<BatoiLogicOrderLine> findAllByPks(List<Integer> ids, ProgressBar pb) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order_line", "search_read",
                asList(asList
                        (
                                asList("id","=",ids)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "quantity", "order_id", "product_id"));
                }}
        )));

        return getSet(list, pb);
    }

    @Override
    public Set<BatoiLogicOrderLine> findAll() throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order_line", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id", "quantity", "order_id", "product_id"));
                }}
        )));

        return getSet(list);
    }

    @Override
    public Set<BatoiLogicOrderLine> findAll(ProgressBar pbLoad) throws Exception {
        return null;
    }

    private Set<BatoiLogicOrderLine> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicOrderLine> lines = new HashSet<>();
        BatoiLogicOrderLine line;
        HashMap currentHashMap;
        for(Object h : list)
        {
            line = new BatoiLogicOrderLine();
            currentHashMap = (HashMap) h;

            line.setId(OdooTypes.getInt(currentHashMap, "id"));
            line.setQuantity(OdooTypes.getInt(currentHashMap, "quantity"));

            line.setBatoiLogicOrder((BatoiLogicOrder) OdooTypes.Many2one(currentHashMap,
                    "order_id", OrderDAO.class.getName()));
            line.setBatoiLogicProduct((BatoiLogicProduct) OdooTypes.Many2one(currentHashMap,
                    "product_id", ProductDAO.class.getName()));

            lines.add(line);
        }

        return lines;
    }

    private Set<BatoiLogicOrderLine> getSet(List<Object> list, ProgressBar pb) throws Exception
    {
        Set<BatoiLogicOrderLine> lines = new HashSet<>();
        BatoiLogicOrderLine line;
        HashMap currentHashMap;

        double progress = 1.0 / list.size();
        double totalProgress = 0.0;
        for(Object h : list)
        {
            line = new BatoiLogicOrderLine();
            currentHashMap = (HashMap) h;

            line.setId(OdooTypes.getInt(currentHashMap, "id"));
            line.setQuantity(OdooTypes.getInt(currentHashMap, "quantity"));

            line.setBatoiLogicOrder((BatoiLogicOrder) OdooTypes.Many2one(currentHashMap,
                    "order_id", OrderDAO.class.getName()));
            line.setBatoiLogicProduct((BatoiLogicProduct) OdooTypes.Many2one(currentHashMap,
                    "product_id", ProductDAO.class.getName()));

            lines.add(line);

            totalProgress += progress;
            pb.setProgress(totalProgress);
        }

        return lines;
    }

    @Override
    public boolean insert(BatoiLogicOrderLine object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int insertRecoverId(BatoiLogicOrderLine object) throws Exception 
    {
        final Integer id = (Integer)APIConnection.execute("execute_kw", asList(
            ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
            "batoi_logic.order_line", "create",
            asList(new HashMap() {{ 
                put("id", object.getId());
                put("quantity", object.getQuantity());
                put("order_id", object.getBatoiLogicOrder().getId());
                put("product_id", object.getBatoiLogicProduct().getId());
            }})
        ));
        
        return id;
    }

    @Override
    public boolean update(BatoiLogicOrderLine object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(int id) throws Exception
    {
        APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.order_line", "unlink",
                asList(asList(id))));

        return findByPk(id) == null;
    }
}