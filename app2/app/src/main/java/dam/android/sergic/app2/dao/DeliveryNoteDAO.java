package dam.android.sergic.app2.dao;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dam.android.sergic.app2.models.DeliveryNote;
import dam.android.sergic.app2.models.Order;
import dam.android.sergic.app2.odoo.ConnectionAPI;
import dam.android.sergic.app2.odoo.OdooTypes;

import static java.util.Arrays.asList;

public class DeliveryNoteDAO implements GenericDAO<DeliveryNote>
{
    private XmlRpcClient APIConnection;

    public DeliveryNoteDAO() throws MalformedURLException, XmlRpcException
    {
        APIConnection = ConnectionAPI.getAPIConnection();
    }

    @Override
    public DeliveryNote findByPk(int id) throws Exception
    {
        List<Object> list;

        list = asList((Object[])APIConnection.execute("execute_kw", asList(
                ConnectionAPI.db, ConnectionAPI.uid, ConnectionAPI.password,
                "batoi_logic.delivery_note", "search_read",
                asList(asList
                        (
                            asList("id","=",id)
                        )),
                new HashMap()
                {{
                    put("fields", asList("notes", "order_id"));
                }}
        )));

        return (DeliveryNote) (getSet(list).size()>0? getSet(list).toArray()[0]: null);
    }

    @Override
    public Set<DeliveryNote> findAllByPks(List<Integer> ids) throws Exception {
        return null;
    }

    @Override
    public Set<DeliveryNote> findAll() throws Exception {
        return null;
    }

    private Set<DeliveryNote> getSet(List<Object> list) throws Exception
    {
        Set<DeliveryNote> deliveryNotes = new HashSet<>();
        DeliveryNote deliveryNote;
        HashMap currentHashMap;
        for(Object h : list)
        {
            deliveryNote = new DeliveryNote();
            currentHashMap = (HashMap) h;

            deliveryNote.setNotes(OdooTypes.getString(currentHashMap,"notes"));
            deliveryNote.setOrder((Order) OdooTypes.Many2one(currentHashMap,"order_id",OrderDAO.class.getName()));

            if(deliveryNote.getOrder()!=null)
                deliveryNotes.add(deliveryNote);
        }

        return deliveryNotes;
    }

    @Override
    public boolean insert(DeliveryNote object) throws Exception {
        return false;
    }

    @Override
    public boolean update(DeliveryNote object) throws Exception {
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        return false;
    }
}