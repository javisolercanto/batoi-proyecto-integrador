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
import models.BatoiLogicCity;

public class PostalCodeDAO implements GenericDAO<BatoiLogicPostalCode>
{
    private XmlRpcClient APIConnection;

    public PostalCodeDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicPostalCode findByPk(int id) throws Exception
    {
        List<Object> list;

        if(id == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.postal_code", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "city_id", "name"));
                }}
        )));

        return (BatoiLogicPostalCode) getSet(list).toArray()[0];
    }

    public BatoiLogicPostalCode findByName(int postalCode) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.postal_code", "search_read",
                asList(asList
                        (
                            asList("name","=",postalCode)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id", "city_id", "name"));
                }}
        )));

        return (BatoiLogicPostalCode) (getSet(list).size()>0? getSet(list).toArray()[0]: null);
    }

    @Override
    public Set<BatoiLogicPostalCode> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicPostalCode> findAll() throws Exception {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.postal_code", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id", "city_id", "name"));
                }}
        )));

        return getSet(list);
    }

    @Override
    public Set<BatoiLogicPostalCode> findAll(ProgressBar pbLoad) throws Exception {
        return null;
    }

    @Override
    public boolean insert(BatoiLogicPostalCode object) throws Exception {
        return false;
    }

    @Override
    public boolean update(BatoiLogicPostalCode object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
    
    private Set<BatoiLogicPostalCode> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicPostalCode> codes = new HashSet<>();
        BatoiLogicPostalCode code;
        HashMap currentHashMap;
        for(Object h : list)
        {
            code = new BatoiLogicPostalCode();
            currentHashMap = (HashMap) h;

            code.setId(OdooTypes.getInt(currentHashMap, "id"));
            code.setBatoiLogicCity((BatoiLogicCity) OdooTypes.Many2one(currentHashMap, "city_id", CityDAO.class.getName()));
            code.setName(OdooTypes.getInt(currentHashMap, "name"));
            codes.add(code);
        }

        return codes;
    }
}