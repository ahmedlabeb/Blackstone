package com.blackstone.customer.common.factory;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;

public abstract class AbstractAnnotationFactory<A extends Annotation, T> {
	@Autowired
	private ListableBeanFactory beanFactory;

	public T getImplementation(String strategy) {
		Class<A> annotationClass = strategyAnnotation();
		Class<T> interfaceClass = strategyInterface();

		String[] names = beanFactory.getBeanNamesForAnnotation(annotationClass);

		for (String strategyName : names) {
			if (strategyName.equals(strategy)) {
				return beanFactory.getBean(strategyName, interfaceClass);
			}
		}

		throw new IllegalStateException("There is no \"" + strategy + "\" strategy available for " + interfaceClass.getName());
	}

    abstract Class<A> strategyAnnotation();

	abstract Class<T> strategyInterface();
}
