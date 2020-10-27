package dao;

import api_rest_odoo.ConnectionAPI;
import javafx.scene.control.ProgressBar;
import models.BatoiLogicProvider;
import models.BatoiLogicSupplierOrder;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

public class ProviderDAO implements GenericDAO<BatoiLogicProvider>
{
    private XmlRpcClient APIConnection;

    public ProviderDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicProvider findByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.provider", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "name", "cif", "email", "telephone", "supplier_orders_id", "products_supplied"));
                }}
        )));

        return (BatoiLogicProvider) (getSet(list).size()>0? getSet(list).toArray()[0]: null);
    }

    @Override
    public Set<BatoiLogicProvider> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicProvider> findAll() throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicProvider> findAll(ProgressBar pbLoad) throws Exception {
        return null;
    }

    private Set<BatoiLogicProvider> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicProvider> providers = new HashSet<>();
        BatoiLogicProvider provider;
        HashMap currentHashMap;
        for(Object h : list)
        {
            provider = new BatoiLogicProvider();
            currentHashMap = (HashMap) h;

            provider.setId(OdooTypes.getInt(currentHashMap, "id"));
            providers.add(provider);
        }

        return providers;
    }

    @Override
    public boolean insert(BatoiLogicProvider object) throws Exception {
        return false;
    }

    @Override
    public boolean update(BatoiLogicProvider object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}