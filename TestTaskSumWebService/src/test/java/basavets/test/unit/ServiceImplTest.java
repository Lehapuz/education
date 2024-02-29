package basavets.test.unit;

import basavets.test.MockData;
import basavets.test.dto.response.ResponseDto;
import basavets.test.repository.SumRepository;
import basavets.test.service.impl.ServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceImplTest {

    @Mock
    private SumRepository sumRepository;
    @InjectMocks
    private ServiceImpl service;

    @Test
    void when_save_data_then_success(){
        when(sumRepository.findByName("test1")).thenReturn(null);
        ResponseDto expected = MockData.getValidResponseDto();
        ResponseDto actual = service.saveData(MockData.getValidAddRequestDto());
        assertEquals(expected, actual);
    }

    @Test
    void when_save_data_then_fail(){
        when(sumRepository.findByName("test1")).thenReturn(MockData.getValidData1());
        ResponseDto expected = MockData.getAlreadyExistResponseDto();
        ResponseDto actual = service.saveData(MockData.getValidAddRequestDto());
        assertEquals(expected, actual);
    }

    @Test
    void when_delete_data_then_success(){
        when(sumRepository.findByName("test1")).thenReturn(MockData.getValidData1());
        ResponseDto expected = MockData.getValidResponseDto();
        ResponseDto actual = service.deleteData(MockData.getValidDeleteRequestDto());
        assertEquals(expected, actual);
    }

    @Test
    void when_delete_data_then_fail(){
        when(sumRepository.findByName("test1")).thenReturn(null);
        ResponseDto expected = MockData.getNotFoundResponseDto();
        ResponseDto actual = service.deleteData(MockData.getValidDeleteRequestDto());
        assertEquals(expected, actual);
    }

    @Test
    void when_calculate_data_then_success(){
        when(sumRepository.findByName("test1")).thenReturn(MockData.getValidData1());
        when(sumRepository.findByName("test2")).thenReturn(MockData.getValidData2());
        ResponseDto expected = MockData.getValidCalculateResponseDto();
        ResponseDto actual = service.calculateSum(MockData.getValidCalculateRequestDto());
        assertEquals(expected, actual);
    }

    @Test
    void when_calculate_data_then_fail(){
        when(sumRepository.findByName("test1")).thenReturn(null);
        when(sumRepository.findByName("test2")).thenReturn(MockData.getValidData2());
        ResponseDto expected = MockData.getNotFoundResponseDto();
        ResponseDto actual = service.calculateSum(MockData.getValidCalculateRequestDto());
        assertEquals(expected, actual);
    }
}
