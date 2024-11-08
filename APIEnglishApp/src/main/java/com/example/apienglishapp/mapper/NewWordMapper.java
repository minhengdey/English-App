package com.example.apienglishapp.mapper;

import com.example.apienglishapp.dto.request.NewWordRequest;
import com.example.apienglishapp.dto.response.NewWordResponse;
import com.example.apienglishapp.entity.NewWordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NewWordMapper {
    NewWordEntity toNewWordEntity (NewWordRequest newWordRequest);
    void updateNewWord (@MappingTarget NewWordEntity newWordEntity, NewWordRequest newWordRequest);
    NewWordResponse toNewWordResponse (NewWordEntity newWordEntity);
}
