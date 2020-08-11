package com.ms3.contactapi.util;

import java.beans.FeatureDescriptor;
import java.util.Arrays;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class CopyUtil {
	
	private CopyUtil() {}
	
	public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
	
	public static void copyNonNullProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target, getNullPropertyName(source));
	}
	
	private static String[] getNullPropertyName(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		
		return Arrays.stream(src.getPropertyDescriptors())
				.map(FeatureDescriptor::getName)
				.filter(name -> src.getPropertyValue(name) == null)
				.toArray(String[]::new);
	}
}
