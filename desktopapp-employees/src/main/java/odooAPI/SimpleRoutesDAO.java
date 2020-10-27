package odooAPI;

import dao.GenericDAO;
import models.BatoiLogicDeliveryNote;
import models.BatoiLogicRoute;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

public class SimpleRoutesDAO implements GenericDAO<BatoiLogicRoute>
{
    private XmlRpcClient APIConnection;

    public SimpleRoutesDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicRoute findByPk(int id) throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicRoute> findAll() throws Exception {
        return null;
    }

    @Override
    public boolean insert(BatoiLogicRoute object) throws Exception
    {
        List<Integer> ids = new ArrayList<>();
        object.getNote().forEach(d -> ids.add(d.getId()));

        final Integer id = (Integer)APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.route", "create",
                asList(new HashMap()
                {{
                    put("deliveryman_id", object.getBatoiLogicDeliveryMan().getId());
                    put("delivery_notes_id", ids);
                }})
        ));

//        return findByPk(id) != null;
        return true;
    }

    @Override
    public boolean update(BatoiLogicRoute object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(BatoiLogicRoute object) throws Exception {
        return false;
    }
}