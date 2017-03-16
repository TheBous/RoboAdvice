package com.roboadvice.serviceTest;

import com.roboadvice.RoboadviceApplication;
import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.service.PortfolioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)
public class PortfolioServiceTest {

    @Autowired
    private PortfolioService portfolioService;

    @Test
    public void getCurrentTestOK(){
        String email = "leo@galati.com";

        PortfolioDTO portfolioDTO = portfolioService.getCurrent(email);

        assertNotNull(portfolioDTO);
        assertTrue(portfolioDTO instanceof PortfolioDTO);
        assertTrue(portfolioDTO.getDate() instanceof LocalDate);
        assertTrue(portfolioDTO.getBondsAmount() instanceof BigDecimal);

    }
}
