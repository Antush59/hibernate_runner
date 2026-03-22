package ru.antush.dto;

import ru.antush.entity.PersonalInfo;
import ru.antush.entity.Role;
import ru.antush.validate.UpdateCheck;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record UserCreatDto(
        @Valid
        PersonalInfo personalInfo,
        @NotNull
        String username,
        String info,
        @NotNull(groups = UpdateCheck.class)
        Role role,
        Integer companyId) {
}
