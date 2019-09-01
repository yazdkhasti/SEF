package edu.rmit.command.core;

import org.springframework.core.ResolvableType;

import java.util.List;

public interface IServiceResolver {
    <T> T getService(Class<T> tClass);
    <T> T getService(ResolvableType type);
    <T> List<T> getServices(Class<T> tClass);
    <T> List<T> getServices(ResolvableType type);

}
