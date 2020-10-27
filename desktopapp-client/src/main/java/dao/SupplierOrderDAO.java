package dao;

import api_rest_odoo.ConnectionAPI;
import javafx.scene.control.ProgressBar;
import models.BatoiLogicCity;
import models.BatoiLogicPostalCode;
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

public class SupplierOrderDAO implements GenericDAO<BatoiLogicSupplierOrder>
{
    private XmlRpcClient APIConnection;

    public SupplierOrderDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicSupplierOrder findByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.supplier_order", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "quantity", "request_date", "provider_id", "product_id"));
                }}
        )));

        return (BatoiLogicSupplierOrder) (getSet(list).size()>0? getSet(list).toArray()[0]: null);
    }

    @Override
    public Set<BatoiLogicSupplierOrder> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicSupplierOrder> findAll() throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicSupplierOrder> findAll(ProgressBar pbLoad) throws Exception {
        return null;
    }

    private Set<BatoiLogicSupplierOrder> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicSupplierOrder> supplies = new HashSet<>();
        BatoiLogicSupplierOrder supplier;
        HashMap currentHashMap;
        for(Object h : list)
        {
            supplier = new BatoiLogicSupplierOrder();
            currentHashMap = (HashMap) h;

            supplier.setId(OdooTypes.getInt(currentHashMap, "id"));
            supplies.add(supplier);
        }

        return supplies;
    }

    @Override
    public boolean insert(BatoiLogicSupplierOrder object) throws Exception
    {
        final Integer id = (Integer)APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.supplier_order", "create",
                asList(new HashMap() {{
                    put("quantity", object.getQuantity());
                    put("request_date", object.getRequestDate().toString());
                    put("provider_id", object.getBatoiLogicProvider().getId());
                    put("product_id", object.getBatoiLogicProduct().getId());
                }})
        ));

        return findByPk(id) != null;
    }

    @Override
    public boolean update(BatoiLogicSupplierOrder object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}
