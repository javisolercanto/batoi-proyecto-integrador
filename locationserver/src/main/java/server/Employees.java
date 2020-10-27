package server;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

public class Employees
{
    private static final List<DatagramPacket> employees = new ArrayList<>();

    public static synchronized List<DatagramPacket> get()
    {
        return employees;
    }

    public static synchronized void add(DatagramPacket pa)
    {
        employees.add(pa);
    }

    public static synchronized boolean isHere(DatagramPacket pa)
    {
        for (DatagramPacket a: employees)
        {
            if(a.getAddress().equals(pa.getAddress()))
                return true;
        }

        return false;
    }

    public static synchronized void delete(DatagramPacket pa)
    {
        employees.removeIf(c -> c.getAddress().equals(pa.getAddress()));
    }
}