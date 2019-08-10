package com.clarity.claritydispatcher.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.lambda.Unchecked;

public interface JSONAble {

  default String toJSON(Object object) {
    return Unchecked.function(getObjectMapper()::writeValueAsString).apply(object);
  }

  default Object toObject(String string, Class<?> claz) {
    return Unchecked.supplier(() -> getObjectMapper().readValue(string, claz)).get();
  }

  private ObjectMapper getObjectMapper() {
    return new ObjectMapper();
  }
}
