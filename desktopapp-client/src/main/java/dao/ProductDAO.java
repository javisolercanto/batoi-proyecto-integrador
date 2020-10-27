package dao;

import java.net.MalformedURLException;
import java.util.*;
import api_rest_odoo.ConnectionAPI;
import javafx.scene.control.ProgressBar;
import models.BatoiLogicProduct;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;
import static java.util.Arrays.asList;

public class ProductDAO implements GenericDAO<BatoiLogicProduct>
{
    private XmlRpcClient APIConnection;

    public ProductDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicProduct findByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.product", "search_read",
                asList(asList
                        (
                                asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "price", "kg", "description", "image", "order_lines_id",
                            "supply_provider_id", "providers_available"));
                }}
        )));

        return (BatoiLogicProduct) getSet(list).toArray()[0];
    }

    @Override
    public Set<BatoiLogicProduct> findAllByPks(List<Integer> ids) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.product", "search_read",
                asList(asList
                        (
                                asList("id","=",ids)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "price", "kg", "description", "image", "order_lines_id",
                            "supply_provider_id", "providers_available"));
                }}
        )));

        return getSet(list);
    }

    @Override
    public Set<BatoiLogicProduct> findAll() throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.product", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "price", "kg", "description", "image", "order_lines_id",
                            "supply_provider_id", "providers_available"));
                }}
        )));

        return getSet(list);
    }

    public Set<BatoiLogicProduct> findAll(ProgressBar pbLoad) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.product", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "price", "kg", "description", "image", "order_lines_id",
                            "supply_provider_id", "providers_available"));
                }}
        )));

        return getSet(list, pbLoad);
    }

    private Set<BatoiLogicProduct> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicProduct> products = new HashSet<>();
        BatoiLogicProduct product;
        HashMap currentHashMap;
        for(Object h : list)
        {
            product = new BatoiLogicProduct();
            currentHashMap = (HashMap) h;

            product.setId(OdooTypes.getInt(currentHashMap, "id"));
            product.setName(OdooTypes.getString(currentHashMap, "name"));
            product.setPrice(OdooTypes.getDouble(currentHashMap, "price"));
            product.setKg(OdooTypes.getDouble(currentHashMap, "kg"));
            product.setDescription(OdooTypes.getString(currentHashMap, "description"));
            product.setImage(OdooTypes.getImage(currentHashMap, "image"));
            product.setLines(OdooTypes.One2many(currentHashMap, "order_lines_id"));
            product.setBatoiLogicProductBatoiLogicProviderRels(OdooTypes.One2many(currentHashMap, "providers_available"));

            products.add(product);
        }

        return products;
    }

    private Set<BatoiLogicProduct> getSet(List<Object> list, ProgressBar pb) throws Exception
    {
        Set<BatoiLogicProduct> products = new HashSet<>();
        BatoiLogicProduct product;
        HashMap currentHashMap;

        double progress = 1.0 / list.size();
        double totalProgress = 0.0;
        for(Object h : list)
        {
            product = new BatoiLogicProduct();
            currentHashMap = (HashMap) h;

            product.setId(OdooTypes.getInt(currentHashMap, "id"));
            product.setName(OdooTypes.getString(currentHashMap, "name"));
            product.setPrice(OdooTypes.getDouble(currentHashMap, "price"));
            product.setKg(OdooTypes.getDouble(currentHashMap, "kg"));
            product.setDescription(OdooTypes.getString(currentHashMap, "description"));
            product.setImage(OdooTypes.getImage(currentHashMap, "image"));
            product.setLines(OdooTypes.One2many(currentHashMap, "order_lines_id"));

            products.add(product);

            totalProgress += progress;
            pb.setProgress(totalProgress);
        }

        return products;
    }

    @Override
    public boolean insert(BatoiLogicProduct object) throws Exception {
        return false;
    }

    @Override
    public boolean update(BatoiLogicProduct object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}