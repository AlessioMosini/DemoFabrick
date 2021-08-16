package com.demo.fabrick.utility;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtility {

    final
    ModelMapper modelMapper;

    public ModelMapperUtility(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // given json model obj then convert to entity model obj
    public <T> T convertToEntity(Object obj, Class<T> entity) {
        return modelMapper.map(obj, entity);
    }

}
