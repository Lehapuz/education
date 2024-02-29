package basavets.test.service.impl;

import basavets.test.dto.request.AddRequestDto;
import basavets.test.dto.request.CalculateRequestDto;
import basavets.test.dto.request.DeleteRequestDto;
import basavets.test.dto.response.ResponseDto;
import basavets.test.entity.Data;
import basavets.test.repository.SumRepository;
import basavets.test.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Objects;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class ServiceImpl implements Service {

    private final static String SUCCESSFUL_CODE = "0";
    private final static String UNSUCCESSFUL_CODE = "-1";
    private final static String SUCCESSFUL_DESCRIPTION = "OK";
    private final static String ALREADY_EXIST_DESCRIPTION = "This name already exist";
    private final static String NOT_FOUND_DESCRIPTION = "This name is not found";
    private final SumRepository sumRepository;

    @Override
    public ResponseDto saveData(AddRequestDto addRequestDto) {
        if (Objects.nonNull(sumRepository.findByName(addRequestDto.name()))) {
            log.info("this " + addRequestDto.name() + " is already exist");
            return ResponseDto.builder()
                    .code(UNSUCCESSFUL_CODE)
                    .description(ALREADY_EXIST_DESCRIPTION)
                    .build();
        }
        Data data = new Data().toBuilder()
                .name(addRequestDto.name())
                .value(addRequestDto.value())
                .build();
        sumRepository.save(data);
        log.info("data is saved");
        return ResponseDto.builder()
                .code(SUCCESSFUL_CODE)
                .description(SUCCESSFUL_DESCRIPTION)
                .build();
    }

    @Override
    public ResponseDto deleteData(DeleteRequestDto deleteRequestDto) {
        Data data = sumRepository.findByName(deleteRequestDto.name());
        if (Objects.isNull(data)) {
            log.info("this " + deleteRequestDto.name() + " is not found");
            return ResponseDto.builder()
                    .code(UNSUCCESSFUL_CODE)
                    .description(NOT_FOUND_DESCRIPTION)
                    .build();
        }
        sumRepository.delete(data);
        log.info("data is deleted");
        return ResponseDto.builder()
                .code(SUCCESSFUL_CODE)
                .description(SUCCESSFUL_DESCRIPTION)
                .build();
    }

    @Override
    public ResponseDto calculateSum(CalculateRequestDto calculateRequestDto) {
        Data firstData = sumRepository.findByName(calculateRequestDto.first());
        Data secondData = sumRepository.findByName(calculateRequestDto.second());
        if (Objects.isNull(firstData) || Objects.isNull(secondData)) {
            log.info("this " + calculateRequestDto.first() + " or " + calculateRequestDto.second() + " is not found");
            return ResponseDto.builder()
                    .code(UNSUCCESSFUL_CODE)
                    .description(NOT_FOUND_DESCRIPTION)
                    .build();

        }
        log.info("calculating is succeed");
        return ResponseDto.builder()
                .code(SUCCESSFUL_CODE)
                .description(SUCCESSFUL_DESCRIPTION)
                .sum(calculate(firstData.getValue(), secondData.getValue()))
                .build();
    }

    private BigInteger calculate(Integer a, Integer b) {
        return BigInteger.valueOf(a + b);
    }
}
