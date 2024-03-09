package basavets.test;

import basavets.test.dto.request.AddRequestDto;
import basavets.test.dto.request.CalculateRequestDto;
import basavets.test.dto.request.DeleteRequestDto;
import basavets.test.dto.response.ResponseDto;
import basavets.test.entity.Data;

import java.math.BigInteger;

public class MockData {

    public static Data getValidData1() {
        return Data.builder()
                .name("test1")
                .value(1)
                .build();
    }

    public static Data getValidData2() {
        return Data.builder()
                .name("test2")
                .value(2)
                .build();
    }

    public static AddRequestDto getValidAddRequestDto() {
        return AddRequestDto.builder()
                .name("test1")
                .value(1)
                .build();
    }

    public static DeleteRequestDto getValidDeleteRequestDto() {
        return DeleteRequestDto.builder()
                .name("test1")
                .build();
    }

    public static CalculateRequestDto getValidCalculateRequestDto() {
        return CalculateRequestDto.builder()
                .first("test1")
                .second("test2")
                .build();
    }

    public static ResponseDto getValidResponseDto() {
        return ResponseDto.builder()
                .code("0")
                .description("OK")
                .build();
    }

    public static ResponseDto getValidCalculateResponseDto() {
        return ResponseDto.builder()
                .code("0")
                .description("OK")
                .sum(BigInteger.valueOf(3))
                .build();
    }

    public static ResponseDto getAlreadyExistResponseDto() {
        return ResponseDto.builder()
                .code("-1")
                .description("This name already exist")
                .build();
    }

    public static ResponseDto getNotFoundResponseDto() {
        return ResponseDto.builder()
                .code("-2")
                .description("This name is not found")
                .build();
    }
}
