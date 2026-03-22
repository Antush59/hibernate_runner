package ru.antush.dto;

import ru.antush.entity.PersonalInfo;
import ru.antush.entity.Role;

public record UserCreatDto(PersonalInfo personalInfo,
                           String username,
                           String info,
                           Role role,
                           Integer companyId) {
}
