package ru.antush;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import ru.antush.entity.Payment;
import ru.antush.interceptor.GlobalInterceptor;
import ru.antush.util.HibernateUtil;
import ru.antush.util.TestDataImporter;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Date;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            //             TestDataImporter.importData(sessionFactory);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                Payment payment = session.find(Payment.class, 1L);
                payment.setAmount(payment.getAmount() + 10);

                session.getTransaction().commit();
            }
            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                AuditReader auditReader = AuditReaderFactory.get(session2);
//                auditReader.find(Payment.class, 1L, 1L);
                Payment oldPayment = auditReader.find(Payment.class, 1L, new Date(1771858283385L));
                session2.replicate(oldPayment, ReplicationMode.OVERWRITE);

                auditReader.createQuery()
                        .forEntitiesAtRevision(Payment.class, 400L)
                        .add(AuditEntity.property("amount").ge(450))
                        .add(AuditEntity.property("id").ge(6L))
                        .addProjection(AuditEntity.property("amount"))
                        .addProjection(AuditEntity.id())
                        .getResultList();

                session2.getTransaction().commit();
            }
        }
    }
}
