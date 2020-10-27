package tools;

import controllers.CurrentCustomer;
import dao.GenericDAO;
import dao.OrderDAO;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import models.BatoiLogicOrder;

public class BackgroundLoad extends Thread
{
    private ProgressBar pbLoad;
    private ObservableList list;
    private ObservableList listHistory;
    private TableView<?> table;
    private GenericDAO<?> dao;

    public BackgroundLoad(ProgressBar pbLoad, ObservableList list, TableView<?> table, GenericDAO<?> dao)
    {
        this.pbLoad = pbLoad;
        this.list = list;
        this.table = table;
        this.dao = dao;
    }

    public BackgroundLoad(ProgressBar pbLoad, ObservableList<BatoiLogicOrder> orders, ObservableList<BatoiLogicOrder> history, TableView<BatoiLogicOrder> tvOrders, OrderDAO ordersDB)
    {
        this.pbLoad = pbLoad;
        this.list = orders;
        this.table = tvOrders;
        this.dao = ordersDB;
        this.listHistory = history;
    }

    @Override
    public void run()
    {
        super.run();

        try
        {
            pbLoad.setVisible(true);

            if(dao instanceof OrderDAO)
            {
                list.addAll(((OrderDAO) dao).findAllBy(CurrentCustomer.getCurrentCustomer().getOrders_id(), pbLoad));
                for(int i=0;i<list.size();i++)
                {
                    if(((BatoiLogicOrder) list.get(i)).getStatus().equals("3"))
                    {
                        listHistory.add(list.get(i));
                        list.remove(i);
                    }
                }
            }
            else
                list.addAll(dao.findAll(pbLoad));

            table.refresh();

            Thread.sleep(500);
            pbLoad.setVisible(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
//            System.out.println("Load Cancelled");
        }
    }
}