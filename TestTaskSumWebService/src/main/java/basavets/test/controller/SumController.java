package basavets.test.controller;

import basavets.test.dto.request.AddRequestDto;
import basavets.test.dto.request.CalculateRequestDto;
import basavets.test.dto.request.DeleteRequestDto;
import basavets.test.dto.response.ResponseDto;
import basavets.test.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SumController.API_V1_URI)
@Slf4j
@RequiredArgsConstructor
public class SumController {

    public static final String API_V1_URI = "/api/v1";
    public static final String ADD = "/add";
    public static final String REMOVE = "/remove";
    public static final String SUM = "/sum";
    private final Service service;

    @PostMapping(ADD)
    ResponseDto addData(@RequestBody AddRequestDto addRequestDto) {
        return service.saveData(addRequestDto);
    }

    @PostMapping(REMOVE)
    ResponseDto removeData(@RequestBody DeleteRequestDto deleteRequestDto) {
        return service.deleteData(deleteRequestDto);
    }

    @PostMapping(SUM)
    ResponseDto sumData(@RequestBody CalculateRequestDto calculateRequestDto) {
        return service.calculateSum(calculateRequestDto);
    }
}
