package ru.antush.dto;

import ru.antush.entity.PersonalInfo;
import ru.antush.entity.Role;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          String info,
                          Role role,
                          CompanyReadDto company) {
}
