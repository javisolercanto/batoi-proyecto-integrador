package dao;

import java.util.Set;

public interface GenericDAO<Type>
{
    public Type findByPk(int id) throws Exception;

    public Set<Type> findAll() throws Exception;

    public boolean insert(Type object) throws Exception;

    public boolean update(Type object) throws Exception;

    public boolean delete(Type object) throws Exception;
}