package com.mile.portal.config.hibernateCollection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS
)
@JsonTypeIdResolver(value = HibernateCollectionIdResolver.class)
public class HibernateCollectionMixIn {
}
