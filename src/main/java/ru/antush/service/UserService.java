package ru.antush.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import ru.antush.dao.UserRepository;
import ru.antush.dto.UserCreatDto;
import ru.antush.dto.UserReadDto;
import ru.antush.entity.User;
import ru.antush.mapper.Mapper;
import ru.antush.mapper.UserCreatMapper;
import ru.antush.mapper.UserReadMapper;
import ru.antush.validate.UpdateCheck;

import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreatMapper userCreatMapper;

    @Transactional
    public Long creat(UserCreatDto userDto) {
        // validation
        ValidatorFactory validatorFactory = buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserCreatDto>> validationResult = validator.validate(userDto, UpdateCheck.class);
        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }

        User userEntity = userCreatMapper.mapFrom(userDto);

        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(), userRepository.getEntityManager().getEntityGraph("WithCompany")
        );
        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);

        maybeUser.ifPresent(user -> userRepository.delete(id));
        return maybeUser.isPresent();
    }
}
