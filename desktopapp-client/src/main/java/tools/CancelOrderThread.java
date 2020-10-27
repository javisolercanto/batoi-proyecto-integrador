package tools;

import controllers.ControllerOrdersDetail;
import dao.InventoryDAO;
import dao.OrderDAO;
import dao.OrderLineDAO;
import javafx.scene.input.MouseEvent;
import models.BatoiLogicInventory;
import models.BatoiLogicOrder;
import models.BatoiLogicProduct;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;

public class CancelOrderThread extends Thread
{
    private OnCancelled listener;
    private BatoiLogicOrder order;
    private OrderLineDAO orderLineDAO;
    private InventoryDAO inventoryDAO;
    private OrderDAO ordersDB;
    private final MouseEvent event;

    public interface OnCancelled
    {
        void cancelled(boolean result, MouseEvent event);
    }

    public CancelOrderThread(OnCancelled listener, BatoiLogicOrder order, MouseEvent event) throws MalformedURLException, XmlRpcException
    {
        this.listener = listener;
        this.order = order;
        orderLineDAO = new OrderLineDAO();
        inventoryDAO = new InventoryDAO();
        ordersDB = new OrderDAO();
        this.event = event;
    }

    @Override
    public void run()
    {
        super.run();

        try
        {
            final boolean returnStock = order.getInformation().equals(ControllerOrdersDetail.WORKING);
            try
            {
                // Deleting all lines and restoring stock if needed.
                order.getLines_id().forEach(l -> {
                    try
                    {
                        if(returnStock)
                        {
                            int q = orderLineDAO.findByPk(l).getQuantity();
                            BatoiLogicProduct p = orderLineDAO.findByPk(l).getBatoiLogicProduct();
                            BatoiLogicInventory i = inventoryDAO.findByProductId(p.getId());

                            i.setStock(i.getStock() + q);
                            inventoryDAO.update(i);
                        }
                        orderLineDAO.delete(l);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

                if(ordersDB.delete(order.getId()))
                {
                    listener.cancelled(true, event);
                }
                else
                    listener.cancelled(false, event);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}