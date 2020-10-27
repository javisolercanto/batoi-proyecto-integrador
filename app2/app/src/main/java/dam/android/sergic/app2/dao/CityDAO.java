package dam.android.sergic.app2.dao;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import dam.android.sergic.app2.models.City;
import dam.android.sergic.app2.odoo.ConnectionAPI;
import dam.android.sergic.app2.odoo.OdooTypes;
import static java.util.Arrays.asList;

public class CityDAO implements GenericDAO<City>
{
    private XmlRpcClient APIConnection;

    public CityDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public City findByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.city", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("name", "province"));
                }}
        )));

        return (City) getSet(list).toArray()[0];
    }

    @Override
    public Set<City> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<City> findAll() throws Exception {
        return null;
    }

    private Set<City> getSet(List<Object> list) throws Exception
    {
        Set<City> cities = new HashSet<>();
        City city;
        HashMap currentHashMap;
        for(Object h : list)
        {
            city = new City();
            currentHashMap = (HashMap) h;

            city.setName(OdooTypes.getString(currentHashMap, "name"));
            city.setProvince(OdooTypes.getString(currentHashMap, "province"));

            cities.add(city);
        }

        return cities;
    }

    @Override
    public boolean insert(City object) throws Exception {
        return false;
    }

    @Override
    public boolean update(City object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}