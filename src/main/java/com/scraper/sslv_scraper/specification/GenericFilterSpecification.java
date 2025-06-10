package com.scraper.sslv_scraper.specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;

@Component
public class GenericFilterSpecification<T> {

    public Specification<T> build(Class<?> entityClass, Map<String, Object> filters) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                try {
                    Field entityField = entityClass.getDeclaredField(field);
                    entityField.setAccessible(true);

                    Class<?> fieldType = entityField.getType();
                    Object castedValue = castValue(fieldType, value);

                    predicates.add(cb.equal(root.get(field), castedValue));
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException("Invalid filter field: " + field, e);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Failed to cast value for field: " + field, e);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
        private Object castValue(Class<?> targetType, Object value) {
            if (value == null) return null;
            if (targetType.isAssignableFrom(value.getClass())) return value;
    
            String strVal = value.toString();
            if (targetType == Integer.class || targetType == int.class) return Integer.parseInt(strVal);
            if (targetType == Long.class || targetType == long.class) return Long.parseLong(strVal);
            if (targetType == Double.class || targetType == double.class) return Double.parseDouble(strVal);
            if (targetType == Boolean.class || targetType == boolean.class) return Boolean.parseBoolean(strVal);
            if (targetType == String.class) return strVal;
    
            throw new IllegalArgumentException("Unsupported type: " + targetType);
        }
}
