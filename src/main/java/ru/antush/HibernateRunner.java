package ru.antush;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import ru.antush.entity.Payment;
import ru.antush.entity.User;
import ru.antush.interceptor.GlobalInterceptor;
import ru.antush.util.HibernateUtil;
import ru.antush.util.TestDataImporter;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            //             TestDataImporter.importData(sessionFactory);
            User user = null;
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                user = session.find(User.class, 1L);
                user.getCompany().getName();
                user.getUserChats().size();
                User user1 = session.find(User.class, 1L);

                List<Payment> payments = session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
//                        .setCacheRegion("queries")
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));

                session.getTransaction().commit();
            }
            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                User user2 = session2.find(User.class, 1L);
                user2.getCompany().getName();
                user2.getUserChats().size();

                List<Payment> payments = session2.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
//                        .setCacheRegion("queries")
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));

                session2.getTransaction().commit();
            }
        }
    }
}
