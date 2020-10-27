package odooAPI;

import dao.GenericDAO;
import models.BatoiLogicOrder;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.Tools;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Set;

import static java.util.Arrays.asList;

public class SimpleOrdersDAO implements GenericDAO<BatoiLogicOrder>
{
    private XmlRpcClient APIConnection;

    public SimpleOrdersDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicOrder findByPk(int id) throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicOrder> findAll() throws Exception {
        return null;
    }

    @Override
    public boolean insert(BatoiLogicOrder object) throws Exception {
        return false;
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
                        put("status", object.getStatus());
                    }}
                )
        ));

        return true;
    }

    @Override
    public boolean delete(BatoiLogicOrder object) throws Exception {
        return false;
    }
}