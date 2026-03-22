package ru.antush.mapper;

public interface Mapper <F, T>{

    T mapFrom(F object);
}
