package tools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.BatoiLogicInventory;

public class SearchThread extends Thread
{
    private OnSearchComplete listener;
    private ObservableList<BatoiLogicInventory> listToSearch;
    private String word;

    public interface OnSearchComplete
    {
        void searchCompleted(ObservableList<BatoiLogicInventory> result);
    }

    public SearchThread(OnSearchComplete listener, ObservableList<BatoiLogicInventory> inventories, String word)
    {
        this.listener = listener;
        this.listToSearch = FXCollections.observableArrayList(inventories);
        this.word = word.toLowerCase();
    }

    @Override
    public void run()
    {
        super.run();

        try
        {
            listToSearch.removeIf(p -> !p.getBatoiLogicProduct().getName().toLowerCase().contains(word));
            listener.searchCompleted(listToSearch);
        }
        catch (Exception e)
        {
            System.out.println("Interrupted");
        }
    }
}