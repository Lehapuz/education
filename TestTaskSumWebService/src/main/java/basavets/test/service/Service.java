package basavets.test.service;

import basavets.test.dto.request.AddRequestDto;
import basavets.test.dto.request.CalculateRequestDto;
import basavets.test.dto.request.DeleteRequestDto;
import basavets.test.dto.response.ResponseDto;

public interface Service {

    ResponseDto saveData(AddRequestDto addRequestDto);

    ResponseDto deleteData(DeleteRequestDto deleteRequestDto);

    ResponseDto calculateSum(CalculateRequestDto calculateRequestDto);
}
