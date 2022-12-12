package com.lms.api.soap;

import com.lms.api.services.soapservices.apicall.CustomerService;
import com.lms.api.services.soapservices.customer.CustomerResponse;
import com.lms.api.test.TestNumbers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

    private String tracetransactionId = UUID.randomUUID().toString();

    @Autowired
    private CustomerService customerService;

    @Test
    public void getCustomerkycTest(){

        CustomerResponse customerResponse = this.customerService.getKyc(tracetransactionId, TestNumbers.CustomerID_1);

        assertThat(customerResponse).isNotNull();
        assertThat(customerResponse.getCustomer()).isNotNull();
        assertThat(customerResponse.getCustomer().getCustomerNumber()).isNotNull();
        assertThat(customerResponse.getCustomer().getEmail()).isNotNull();
    }


}
