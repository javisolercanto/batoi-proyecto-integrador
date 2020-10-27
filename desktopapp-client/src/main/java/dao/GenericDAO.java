package dao;

import javafx.scene.control.ProgressBar;
import java.util.List;
import java.util.Set;

public interface GenericDAO<Type>
{
    public Type findByPk(int id) throws Exception;
    public Set<Type> findAllByPks(List<Integer> ids) throws Exception;

    public Set<Type> findAll() throws Exception;
    public Set<Type> findAll(ProgressBar pbLoad) throws Exception;

    public boolean insert(Type object) throws Exception;
    public boolean update(Type object) throws Exception;
    public boolean delete(int id) throws Exception;
}