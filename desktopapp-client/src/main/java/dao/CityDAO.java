package dao;

import api_rest_odoo.ConnectionAPI;
import java.net.MalformedURLException;
import java.util.*;

import javafx.scene.control.ProgressBar;
import models.BatoiLogicCustomer;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import tools.OdooTypes;
import tools.Tools;

import static java.util.Arrays.asList;
import models.BatoiLogicCity;

/**
 *
 * @author batoi
 */
public class CityDAO implements GenericDAO<BatoiLogicCity>
{

    private final XmlRpcClient APIConnection;
    
    public CityDAO() throws MalformedURLException, XmlRpcException
    {
       APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public BatoiLogicCity findByPk(int id) throws Exception {
        List<Object> list;

        if(id == 0)
            return null;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.city", "search_read",
                asList(asList
                        (
                                asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("id","name","province"));
                }}
        )));

        return (BatoiLogicCity) getSet(list).toArray()[0];
    }

    @Override
    public Set<BatoiLogicCity> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<BatoiLogicCity> findAll() throws Exception {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.city", "search_read",
                asList(Collections.emptyList()),
                new HashMap()
                {{
                    put("fields", asList("id","name","province"));
                }}
        )));

        return getSet(list);
    }

    @Override
    public Set<BatoiLogicCity> findAll(ProgressBar pbLoad) throws Exception {
        return null;
    }

    @Override
    public boolean insert(BatoiLogicCity object) throws Exception {
        return false;
    }

    @Override
    public boolean update(BatoiLogicCity object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
    
    private Set<BatoiLogicCity> getSet(List<Object> list) throws Exception
    {
        Set<BatoiLogicCity> cities = new HashSet<>();
        BatoiLogicCity city;
        HashMap currentHashMap;
        for(Object h : list)
        {
            city = new BatoiLogicCity();
            currentHashMap = (HashMap) h;

            city.setId(OdooTypes.getInt(currentHashMap, "id"));
            city.setName(OdooTypes.getString(currentHashMap, "name"));
            city.setProvince(OdooTypes.getString(currentHashMap, "province"));

            cities.add(city);
        }

        return cities;
    }
}