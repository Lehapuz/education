package basavets.test.service.impl;

import basavets.test.dto.request.AddRequestDto;
import basavets.test.dto.request.CalculateRequestDto;
import basavets.test.dto.request.DeleteRequestDto;
import basavets.test.dto.response.ResponseDto;
import basavets.test.entity.Data;
import basavets.test.repository.CustomSumRepository;
import basavets.test.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Optional;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class ServiceImpl implements Service {

    private final static String SUCCESSFUL_CODE = "0";
    private final static String THIS_NAME_ALREADY_EXIST_CODE = "-1";
    private final static String THIS_NAME_IS_NOT_FOUND = "-2";
    private final static String SUCCESSFUL_DESCRIPTION = "OK";
    private final static String ALREADY_EXIST_DESCRIPTION = "This name already exist";
    private final static String NOT_FOUND_DESCRIPTION = "This name is not found";

    private final CustomSumRepository customSumRepository;

    @Override
    public ResponseDto saveData(AddRequestDto addRequestDto) {
        if (customSumRepository.findByName(addRequestDto.name()).isPresent()) {
            log.info("this " + addRequestDto.name() + " is already exist");
            return ResponseDto.builder()
                    .code(THIS_NAME_ALREADY_EXIST_CODE)
                    .description(ALREADY_EXIST_DESCRIPTION)
                    .build();
        }
        Data data = new Data().toBuilder()
                .name(addRequestDto.name())
                .value(addRequestDto.value())
                .build();
        customSumRepository.save(data);
        log.info("data is saved");
        return ResponseDto.builder()
                .code(SUCCESSFUL_CODE)
                .description(SUCCESSFUL_DESCRIPTION)
                .build();
    }

    @Override
    public ResponseDto deleteData(DeleteRequestDto deleteRequestDto) {
        Optional<Data> data = customSumRepository.findByName(deleteRequestDto.name());
        if (data.isEmpty()) {
            log.info("this " + deleteRequestDto.name() + " is not found");
            return ResponseDto.builder()
                    .code(THIS_NAME_IS_NOT_FOUND)
                    .description(NOT_FOUND_DESCRIPTION)
                    .build();
        }
        customSumRepository.delete(data.get());
        log.info("data is deleted");
        return ResponseDto.builder()
                .code(SUCCESSFUL_CODE)
                .description(SUCCESSFUL_DESCRIPTION)
                .build();
    }

    @Override
    public ResponseDto calculateSum(CalculateRequestDto calculateRequestDto) {
        Optional<Data> firstData = customSumRepository.findByName(calculateRequestDto.first());
        Optional<Data> secondData = customSumRepository.findByName(calculateRequestDto.second());
        if (firstData.isEmpty() || secondData.isEmpty()) {
            log.info("this " + calculateRequestDto.first() + " or " + calculateRequestDto.second() + " is not found");
            return ResponseDto.builder()
                    .code(THIS_NAME_IS_NOT_FOUND)
                    .description(NOT_FOUND_DESCRIPTION)
                    .build();

        }
        log.info("calculating is succeed");
        return ResponseDto.builder()
                .code(SUCCESSFUL_CODE)
                .description(SUCCESSFUL_DESCRIPTION)
                .sum(calculate(firstData.get().getValue(), secondData.get().getValue()))
                .build();
    }

    private BigInteger calculate(Integer a, Integer b) {
        return BigInteger.valueOf(a + b);
    }
}
