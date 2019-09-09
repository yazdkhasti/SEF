package edu.rmit.command.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SpringServiceResolver implements IServiceResolver {

    @Autowired
    private ApplicationContext appContext;

    @Override
    public <T> boolean containsBean(Class<T> tClass) {
        ResolvableType resolvableType = ResolvableType.forClass(tClass);
        return containsBean(resolvableType);
    }

    @Override
    public <T> boolean containsBean(ResolvableType type) {
        return appContext.getBeanNamesForType(type).length > 0;
    }

    @Override
    public <T> T getService(Class<T> tClass) {

        ResolvableType resolvableType = ResolvableType.forClass(tClass);
        return getService(resolvableType);

    }

    @Override
    public <T> T getService(ResolvableType type) {

        T result;

        String[] typeNames = appContext.getBeanNamesForType(type);

        if (typeNames.length == 0) {

            result = (T) appContext.getBean(type.resolve());

        } else {

            result = (T) appContext.getBean(typeNames[0]);

        }

        return result;

    }

    @Override
    public <T> List<T> getServices(Class<T> tClass) {
        ResolvableType resolvableType = ResolvableType.forClass(tClass);
        return getServices(resolvableType);
    }

    @Override
    public <T> List<T> getServices(ResolvableType type) {
        String[] typeNames = appContext.getBeanNamesForType(type);
        ArrayList<T> typeObjects = new ArrayList<>();
        for (String typeName : typeNames) {
            Object typeObject = appContext.getBean(typeName);
            typeObjects.add((T) typeObject);
        }
        Collections.sort(typeObjects, AnnotationAwareOrderComparator.INSTANCE);
        return typeObjects;
    }


}
