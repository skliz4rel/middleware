package com.lms.api.soap;


import com.lms.api.services.soapservices.apicall.TransactionService;
import com.lms.api.services.soapservices.transaction.TransactionsResponse;
import com.lms.api.test.TestNumbers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    private String tracetransactionId = UUID.randomUUID().toString();

    @Autowired
    private TransactionService transactionService;

    @Test
    public void testTransactionHistory(){

        TransactionsResponse transactionsResponse =  this.transactionService.getTransactionData(tracetransactionId, TestNumbers.CustomerID_1);

        assertThat(transactionsResponse).isNotNull();
        assertThat(transactionsResponse.getTransactions()).isNotNull();

    }

}
