package dam.android.sergic.app2.dao;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dam.android.sergic.app2.models.Route;
import dam.android.sergic.app2.odoo.ConnectionAPI;
import dam.android.sergic.app2.odoo.OdooTypes;

import static java.util.Arrays.asList;

public class RouteDAO implements GenericDAO<Route>
{
    private XmlRpcClient APIConnection;

    public RouteDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public Route findByPk(int id) throws Exception {
        return null;
    }

    @Override
    public Set<Route> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<Route> findAll() throws Exception {
        return null;
    }

    public Set<Route> findByDeliveryman(int deliveryman) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.route", "search_read",
                asList(asList
                        (
                            asList("deliveryman_id","=",deliveryman)
                        )),
                new HashMap()
                {{
                    put("fields", asList("delivery_notes_id"));
                }}
        )));

        return getSet(list);
    }

    private Set<Route> getSet(List<Object> list) throws Exception
    {
        Set<Route> routes = new HashSet<>();
        Route route;
        HashMap currentHashMap;
        for(Object h : list)
        {
            currentHashMap = (HashMap) h;
            route = new Route(OdooTypes.One2many(currentHashMap,"delivery_notes_id"));
            routes.add(route);
        }

        return routes;
    }

    @Override
    public boolean insert(Route object) throws Exception {
        return false;
    }

    @Override
    public boolean update(Route object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception
    {
        return false;
    }
}