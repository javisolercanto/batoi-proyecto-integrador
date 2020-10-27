package dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class ConnectionHibernate {

    // A SessionFactory is set up once for an application!
    private static SessionFactory sessionFactory = null;

    public static SessionFactory getSessionFactory()
    {

        if (sessionFactory == null)
        {
            // configure() -> configures settings from hibernate.cfg.xml
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                // The registry would be destroyed by the SessionFactory, 
                // but we had trouble building the SessionFactory -> so destroy it manually.
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }

        return sessionFactory;
    }
}
