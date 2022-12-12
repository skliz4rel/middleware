package com.lms.api.repositorys;

import com.lms.api.db.User;
import com.lms.api.services.soapservices.customer.Gender;
import com.lms.api.services.soapservices.customer.IdType;
import com.lms.api.services.soapservices.customer.Status;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataR2dbcTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {

    @Autowired
   private UserRepository userRepository;

    private User user;

    public User initialize(){

       // user.setId(UUID.randomUUID().toString());
        user = new User();

        user.setEmail("skliz4rel@gmail.com");
        user.setDob(LocalDate.now());
        user.setGender(Gender.MALE);
        user.setFirstName("jide");
        user.setCustomerNumber("08131528807");
        user.setIdNumber("080315");
        user.setLastName("Akindejoye");
        user.setMiddleName("bode");
        user.setMobile("08131528807");
        user.setIdType(IdType.NATIONAL_ID);
        user.setStatus(Status.ACTIVE);

        return user;
    }


    @Test
    public void userRepoActivityTest(){

         initialize();

        this.userRepository.save(user).block();

       boolean check =  userRepository.existsByCustomerNumber(user.getCustomerNumber()).block();

       assertThat(check).isTrue();

       var foundUser = userRepository.findByCustomerNumber(user.getCustomerNumber()).block();

       assertThat(foundUser).isNotNull();

       foundUser = userRepository.findByEmail(user.getEmail()).block();

        assertThat(foundUser).isNotNull();

        foundUser = userRepository.findByEmailOrCustomerNumber(user.getEmail(), user.getCustomerNumber()).block();

        assertThat(foundUser).isNotNull();

        foundUser = userRepository.findByMonthlyIncome(user.getMonthlyIncome()).collectList().block().get(0);

        assertThat(foundUser).isNotNull();

        this.userRepository.deleteAll().block();  //.deleteById(user.getId()).block();
    }

}
