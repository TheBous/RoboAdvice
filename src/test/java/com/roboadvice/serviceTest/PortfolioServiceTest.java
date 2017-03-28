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
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)
public class PortfolioServiceTest {

    @Autowired
    private PortfolioService portfolioService;

    @Test
    public void getCurrentTestOK() {
        String email = "leo@galati.com";

        PortfolioDTO portfolioDTO = portfolioService.getCurrent(email);

        assertNotNull(portfolioDTO);
        assertTrue(portfolioDTO instanceof PortfolioDTO);
        assertTrue(portfolioDTO.getDate() instanceof LocalDate);
        assertTrue(portfolioDTO.getBondsAmount() instanceof BigDecimal);

    }

    @Test
    public void getCurrentTestFailure() {
        String email = "asudhio@ciao.it";

        PortfolioDTO portfolioDTO = portfolioService.getCurrent(email);
        assertNull(portfolioDTO);
        assertFalse(portfolioDTO instanceof PortfolioDTO);
    }

    @Test
    public void getFullHistoryTestOk() {
        String email = "leo@galati.com";

        List<PortfolioDTO> portfolioDTOS = portfolioService.getFullHistory(email);

        assertNotNull(portfolioDTOS);
        assertTrue(portfolioDTOS instanceof  List<?>);
        assertTrue(!portfolioDTOS.isEmpty());
        for (PortfolioDTO portfolioDTO : portfolioDTOS){
            assertTrue(portfolioDTO.getDate() instanceof LocalDate);
            assertTrue(portfolioDTO.getTotalAmount() instanceof  BigDecimal);
            assertTrue(portfolioDTO.getAssetsClassAmount(1) instanceof BigDecimal);
        }
    }

    @Test
    public void getFullHistoryTestFailure(){
        String email = "test@failure.false";

        List<PortfolioDTO> portfolioDTOS = portfolioService.getFullHistory(email);

        assertNull(portfolioDTOS);
        assertFalse(portfolioDTOS instanceof List<?>);
    }


    @Test
    public void getHistoryByDatesTestOk(){
        String email = "leo@galati.com";
        LocalDate fromDate = LocalDate.of(2017,02,01);
        LocalDate toDate = LocalDate.of(2017,03,14);

        List<PortfolioDTO> portfolioDTOS = portfolioService.getHistoryByDates(email, fromDate, toDate);

        assertNotNull(portfolioDTOS);
        assertTrue(portfolioDTOS instanceof List<?>);
        for (PortfolioDTO portfolioDTO : portfolioDTOS){
            assertTrue(portfolioDTO.getAssetsClassAmount(1) instanceof  BigDecimal);
            assertTrue(portfolioDTO.getTotalAmount() instanceof  BigDecimal);
            assertTrue(portfolioDTO.getDate() instanceof LocalDate);
            assertTrue(portfolioDTO.getCommoditiesAmount() instanceof BigDecimal);
        }
    }

    //Failure cause doesn't exist a portfolio in that time
    @Test
    public void getHistoryByDatesTestFailure(){
        String email = "leo@galati.com";
        LocalDate fromDate = LocalDate.of(2015,02,01);
        LocalDate toDate = LocalDate.of(2016,03,14);

        List<PortfolioDTO> portfolioDTOS = portfolioService.getHistoryByDates(email, fromDate, toDate);

        assertNull(portfolioDTOS);
        assertFalse(portfolioDTOS instanceof List<?>);
    }

    //Failure cause doesn't exist that user email
    @Test
    public void getHistoryByDatesTestFailureUser(){
        String email = "test@failure.false";
        LocalDate fromDate = LocalDate.of(2016,02,01);
        LocalDate toDate = LocalDate.of(2017,03,14);

        List<PortfolioDTO> portfolioDTOS = portfolioService.getHistoryByDates(email, fromDate, toDate);

        assertNull(portfolioDTOS);
        assertFalse(portfolioDTOS instanceof List<?>);

    }
}
