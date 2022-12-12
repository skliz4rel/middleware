package com.lms.api.repositorys;

import com.lms.api.db.Loan;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataR2dbcTest
@ExtendWith(SpringExtension.class)
public class LoanRepositoryTest {

    private Loan loan;

    @Autowired
    private LoanRepository loanRepository;

    public void initialize(){

        loan = new Loan();
        loan.setLoanId(UUID.randomUUID().toString());
        loan.setPaybackDate(LocalDate.now());
        loan.setCreatedTime(LocalTime.now());
        loan.setInterest(0);
        loan.setCreatedDate(LocalDate.now());
        loan.setPaidback(false);
        loan.setPaybackAmount(1000);
        loan.setActive(true);
        loan.setCustomerNumber("08131528807");
        loan.setRequestedAmount(900);
    }

    @Test
    public void testLoanRepoOperations() {

        try{

        this.initialize();

        this.loanRepository.save(loan).block();

        boolean check = this.loanRepository.existsByCustomerNumberAndIsActive(loan.getCustomerNumber(), loan.isActive()).block();

        assertThat(check).isTrue();

        check = this.loanRepository.existsByLoanIdAndIsActiveTrue(loan.getLoanId()).block();

        assertThat(check).isTrue();

        check = this.loanRepository.existsByCustomerNumberAndIsActiveTrue(loan.getCustomerNumber()).block();

        assertThat(check).isTrue();

        check = this.loanRepository.existsByLoanIdAndIsActiveTrue("0000000").block();
        assertThat(check).isFalse();

        this.loanRepository.deleteByLoanId(loan.getLoanId()).block();
       }
       catch(Exception e)
        {
            log.error("Error while testing the loan Repository {}",e );
        }
    }
}
