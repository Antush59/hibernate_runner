package ru.antush;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.antush.dao.CompanyRepository;
import ru.antush.dao.PaymentRepository;
import ru.antush.dao.UserRepository;
import ru.antush.dto.UserCreatDto;
import ru.antush.entity.PersonalInfo;
import ru.antush.entity.Role;
import ru.antush.entity.User;
import ru.antush.interceptor.TransactionInterceptor;
import ru.antush.mapper.CompanyReadMapper;
import ru.antush.mapper.UserCreatMapper;
import ru.antush.mapper.UserReadMapper;
import ru.antush.service.UserService;
import ru.antush.util.HibernateUtil;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));

//            session.beginTransaction();

            CompanyRepository companyRepository = new CompanyRepository(session);

            CompanyReadMapper companyReadMapper = new CompanyReadMapper();
            UserReadMapper userReadMapper = new UserReadMapper(companyReadMapper);
            UserCreatMapper userCreatMapper = new UserCreatMapper(companyRepository);

            UserRepository userRepository = new UserRepository(session);
            PaymentRepository paymentRepository = new PaymentRepository(session);
//            UserService userService = new UserService(userRepository, userReadMapper, userCreatMapper);
            TransactionInterceptor transactionInterceptor = new TransactionInterceptor(sessionFactory);

            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreatMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreatMapper);

            userService.findById(1L).ifPresent(System.out::println);

            UserCreatDto userCreatDto = new UserCreatDto(
                    PersonalInfo.builder()
                            .firstname("Liza")
                            .lastname("Stepanova")
                            .birthDate(LocalDate.now())
                            .build(),
                    "liza2@gmail.com",
                    null,
                    Role.USER,
                    1
            );
            userService.creat(userCreatDto);

//            session.getTransaction().commit();


        }
    }
}
