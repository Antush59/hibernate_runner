package ru.antush.mapper;

import lombok.RequiredArgsConstructor;
import ru.antush.dao.CompanyRepository;
import ru.antush.dto.UserCreatDto;
import ru.antush.entity.User;

@RequiredArgsConstructor
public class UserCreatMapper implements Mapper<UserCreatDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreatDto object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .username(object.username())
                .info(object.info())
                .role(object.role())
                .company(companyRepository.findById(object.companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }
}
