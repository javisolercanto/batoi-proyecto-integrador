package dao;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import api_rest_odoo.ConnectionAPI;
import javafx.scene.control.ProgressBar;
import models.BatoiLogicInventory;
import models.BatoiLogicProduct;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;
import static java.util.Arrays.asList;

public class InventoryDAO implements GenericDAO<BatoiLogicInventory>
{
    private final XmlRpcClient APIConnection;

    public InventoryDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicInventory findByPk(int id) throws Exception
    {
        List<Object> list;

        if(id == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.inventory", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "stock", "location", "product_id"));
                }}
        )));

        return (BatoiLogicInventory) getSet(list).toArray()[0];
    }

    public BatoiLogicInventory findByProductId(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.inventory", "search_read",
                asList(asList
                        (
                            asList("product_id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "stock", "location", "product_id"));
                }}
        )));

        return (BatoiLogicInventory) getSet(list).toArray()[0];
    }

    @Override
    public Set<BatoiLogicInventory> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicInventory> findAll() throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicInventory> findAll(ProgressBar pbLoad) throws Exception {
        return null;
    }

    private Set<BatoiLogicInventory> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicInventory> inventories = new HashSet<>();
        BatoiLogicInventory inventory;
        HashMap currentHashMap;
        for(Object h : list)
        {
            inventory = new BatoiLogicInventory();
            currentHashMap = (HashMap) h;

            inventory.setId(OdooTypes.getInt(currentHashMap, "id"));
            inventory.setLocation(OdooTypes.getString(currentHashMap, "location"));
            inventory.setStock(OdooTypes.getInt(currentHashMap, "stock"));
            inventory.setBatoiLogicProduct((BatoiLogicProduct) OdooTypes.Many2one(currentHashMap, "product_id",
                    ProductDAO.class.getName()));

            inventories.add(inventory);
        }

        return inventories;
    }

    @Override
    public boolean insert(BatoiLogicInventory object) throws Exception {
        return false;
    }

    @Override
    public boolean update(BatoiLogicInventory object) throws Exception {
        APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.inventory", "write",
                asList(
                        asList(object.getId()),
                        new HashMap()
                        {{
                            put("stock", object.getStock());
                            put("product_id", object.getBatoiLogicProduct().getId());
                        }}
                )
        ));

        return true;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}