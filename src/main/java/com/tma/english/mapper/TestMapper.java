package com.tma.english.mapper;

import com.tma.english.models.dto.test.TestCreateDTO;
import com.tma.english.models.dto.test.TestOverviewDTO;
import com.tma.english.models.entities.test.Test;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestOverviewDTO testToTestOverviewDTO(Test test);

    Test testCreateDtoToTest(TestCreateDTO testCreateDTO);
}
