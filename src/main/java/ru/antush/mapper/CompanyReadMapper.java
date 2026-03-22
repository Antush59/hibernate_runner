package ru.antush.mapper;

import org.hibernate.Hibernate;
import ru.antush.dto.CompanyReadDto;
import ru.antush.entity.Company;

public class CompanyReadMapper implements Mapper<Company , CompanyReadDto> {

    @Override
    public CompanyReadDto mapFrom(Company object) {
        Hibernate.initialize(object.getLocales());
        return new CompanyReadDto(
                object.getId(),
                object.getName(),
                object.getLocales()
        );
    }
}
