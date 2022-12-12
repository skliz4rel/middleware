package com.lms.api.util;

import com.lms.api.model.TransactionDTO;
import com.lms.api.services.soapservices.transaction.TransactionData;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MappingUtility {

    private ModelMapper modelMapper;

    public List<TransactionDTO> map2TransactionDtolist(List<TransactionData> transactions){

        List<TransactionDTO> dtos = transactions
                .stream()
                .map(user -> modelMapper.map(user, TransactionDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }


}
