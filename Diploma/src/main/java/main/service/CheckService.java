package main.service;

import main.api.response.CheckResponse;
import org.springframework.stereotype.Service;

@Service
public class CheckService {

    public CheckResponse getCheck(){
        CheckResponse checkResponse = new CheckResponse();
        checkResponse.setResult(false);
        return checkResponse;
    }
}