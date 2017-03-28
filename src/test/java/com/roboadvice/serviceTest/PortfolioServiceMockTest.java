package com.roboadvice.serviceTest;

import com.roboadvice.RoboadviceApplication;
import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.model.Assets;
import com.roboadvice.model.AssetsClass;
import com.roboadvice.model.Portfolio;
import com.roboadvice.model.User;
import com.roboadvice.repository.*;
import com.roboadvice.service.PortfolioService;
import com.roboadvice.utils.Constant;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.util.resources.ga.LocaleNames_ga;

import javax.sound.sampled.Port;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by CorsoPc on 27/03/2017.
 */

// Comment Cache annotation on PortfolioService for start every test.

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)
public class PortfolioServiceMockTest {

    @InjectMocks
    @Autowired
    private PortfolioService portfolioService;

    @Mock
    private PortfolioRepository portfolioRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StrategyRepository strategyRepository;
    @Mock
    private AssetsRepository assetsRepository;
    @Mock
    private ApiDataRepository apiDataRepository;
    @Mock
    private AssetsClassRepository assetsClassRepository;

    private static User user;


    @BeforeClass
    public static void setUpAuth() {
        user = new User(1, "Enrico", "enry", "enrico@pasqualino.com", "Pasquale", "USER");
    }

    @Before
    public void setUpMock() {
        MockitoAnnotations.initMocks(this);
        when(userRepository.findByEmail("enrico@pasqualino.com")).thenReturn(user);
    }

    @Test
    public void getCurrentPortfolioSuccess() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        assertNotNull(user);

        List<Portfolio> resultPortfolio = new ArrayList<>();

        AssetsClass aC1 = new AssetsClass(1, "Bonds");
        Assets assets1 = new Assets(1, "U.S. Treasury Bond Futures", new BigDecimal(80.0000), "CHRIS/CME_US1", 1, aC1);
        Portfolio portfolio1 = new Portfolio(0, LocalDate.now(), new BigDecimal(500.00000), new BigDecimal(2.65836), new BigDecimal(400.00000), user, assets1, aC1);
        Assets assets1_1 = new Assets(2, "Ultra U.S. Treasury Bond Futures", new BigDecimal(20.0000), "CHRIS/CME_UL1", 1, aC1);
        Portfolio portfolio1_1 = new Portfolio(1, LocalDate.now(), new BigDecimal(500.00000), new BigDecimal(0.62708), new BigDecimal(100.00000), user, assets1_1, aC1);

        AssetsClass aC2 = new AssetsClass(2, "Stocks");
        Assets assets2 = new Assets(3, "Facebook", new BigDecimal(25.0000), "WIKI/FB", 11, aC2);
        Portfolio portfolio2 = new Portfolio(2, LocalDate.now(), new BigDecimal(7500.00000), new BigDecimal(13.64430), new BigDecimal(1875.00000), user, assets2, aC2);
        Assets assets2_1 = new Assets(4, "Apple", new BigDecimal(20.0000), "WIKI/AAPL", 11, aC2);
        Portfolio portfolio2_1 = new Portfolio(3, LocalDate.now(), new BigDecimal(7500.00000), new BigDecimal(10.76504), new BigDecimal(1500.00000), user, assets2_1, aC2);
        Assets assets2_2 = new Assets(5, "Microsoft", new BigDecimal(35.0000), "WIKI/MSFT", 11, aC2);
        Portfolio portfolio2_2 = new Portfolio(4, LocalDate.now(), new BigDecimal(7500.00000), new BigDecimal(40.84332), new BigDecimal(2625.00000), user, assets2_2, aC2);
        Assets assets2_3 = new Assets(6, "Twitter", new BigDecimal(20.0000), "WIKI/TWTR", 11, aC2);
        Portfolio portfolio2_3 = new Portfolio(5, LocalDate.now(), new BigDecimal(7500.00000), new BigDecimal(96.40103), new BigDecimal(1500.00000), user, assets2_3, aC2);

        AssetsClass aC3 = new AssetsClass(3, "Forex");
        Assets assets3 = new Assets(7, "USD -> EUR", new BigDecimal(25.0000), "WIKI/FB", 1, aC3);
        Portfolio portfolio3 = new Portfolio(6, LocalDate.now(), new BigDecimal(1000.00000), new BigDecimal(264.60626), new BigDecimal(250.00000), user, assets3, aC3);
        Assets assets3_1 = new Assets(8, "USD -> CHF", new BigDecimal(50.0000), "WIKI/AAPL", 1, aC3);
        Portfolio portfolio3_1 = new Portfolio(7, LocalDate.now(), new BigDecimal(1000.00000), new BigDecimal(494.01256), new BigDecimal(500.00000), user, assets3_1, aC3);
        Assets assets3_2 = new Assets(9, "USD -> BIT", new BigDecimal(25.0000), "WIKI/MSFT", 3, aC3);
        Portfolio portfolio3_2 = new Portfolio(8, LocalDate.now(), new BigDecimal(1000.00000), new BigDecimal(0.35608), new BigDecimal(250.00000), user, assets3_2, aC3);

        AssetsClass aC4 = new AssetsClass(4, "Commodities");
        Assets assets4 = new Assets(10, "Gold", new BigDecimal(30.0000), "COM/WLD_GOLD", 1, aC4);
        Portfolio portfolio4 = new Portfolio(9, LocalDate.now(), new BigDecimal(1000.00000), new BigDecimal(0.24307), new BigDecimal(300.00000), user, assets4, aC4);
        Assets assets4_1 = new Assets(11, "Silver", new BigDecimal(20.0000), "COM/WLD_SILVER", 1, aC4);
        Portfolio portfolio4_1 = new Portfolio(10, LocalDate.now(), new BigDecimal(1000.00000), new BigDecimal(11.15262), new BigDecimal(200.00000), user, assets4_1, aC4);
        Assets assets4_2 = new Assets(12, "Crude oil", new BigDecimal(30.0000), "COM/OIL_BRENT", 1, aC4);
        Portfolio portfolio4_2 = new Portfolio(11, LocalDate.now(), new BigDecimal(1000.00000), new BigDecimal(5.43675), new BigDecimal(300.00000), user, assets4_2, aC4);
        Assets assets4_3 = new Assets(13, "Rice", new BigDecimal(20.0000), "COM/WLD_RICE_05", 1, aC4);
        Portfolio portfolio4_3 = new Portfolio(12, LocalDate.now(), new BigDecimal(1000.00000), new BigDecimal(0.54496), new BigDecimal(200.00000), user, assets4_3, aC4);

        resultPortfolio.add(portfolio1);
        resultPortfolio.add(portfolio1_1);
        resultPortfolio.add(portfolio2);
        resultPortfolio.add(portfolio2_1);
        resultPortfolio.add(portfolio2_2);
        resultPortfolio.add(portfolio2_3);
        resultPortfolio.add(portfolio3);
        resultPortfolio.add(portfolio3_1);
        resultPortfolio.add(portfolio3_2);
        resultPortfolio.add(portfolio4);
        resultPortfolio.add(portfolio4_1);
        resultPortfolio.add(portfolio4_2);
        resultPortfolio.add(portfolio4_3);

        List<Portfolio> portfolioList = new ArrayList<>();

        Portfolio p = new Portfolio();
        p.setValue(BigDecimal.ZERO);
        for (int i = 0; i < 2; i++) {
            p.setValue(p.getValue().add(resultPortfolio.get(i).getValue()));
            p.setAssetsClass(resultPortfolio.get(i).getAssetsClass());
            p.setDate(resultPortfolio.get(i).getDate());
        }
        portfolioList.add(p);

        p = new Portfolio();
        p.setValue(BigDecimal.ZERO);
        for (int i = 2; i < 6; i++) {
            p.setValue(p.getValue().add(resultPortfolio.get(i).getValue()));
            p.setAssetsClass(resultPortfolio.get(i).getAssetsClass());
            p.setDate(resultPortfolio.get(i).getDate());
        }
        portfolioList.add(p);

        p = new Portfolio();
        p.setValue(BigDecimal.ZERO);
        for (int i = 6; i < 9; i++) {
            p.setValue(p.getValue().add(resultPortfolio.get(i).getValue()));
            p.setAssetsClass(resultPortfolio.get(i).getAssetsClass());
            p.setDate(resultPortfolio.get(i).getDate());
        }
        portfolioList.add(p);

        p = new Portfolio();
        p.setValue(BigDecimal.ZERO);
        for (int i = 9; i < 13; i++) {
            p.setValue(p.getValue().add(resultPortfolio.get(i).getValue()));
            p.setAssetsClass(resultPortfolio.get(i).getAssetsClass());
            p.setDate(resultPortfolio.get(i).getDate());
        }
        portfolioList.add(p);

        when(portfolioRepository.getCurrent(user)).thenReturn(portfolioList);
        assertNotNull(resultPortfolio);
        assertNotNull(resultPortfolio.isEmpty());


        PortfolioDTO portfolioDTO = portfolioService.getCurrent(user.getEmail());

        assertNotNull(portfolioDTO);
        assertTrue(portfolioDTO.getAssetsClassAmount(1) instanceof BigDecimal);
        assertTrue(portfolioDTO.getDate() instanceof LocalDate);
        assertTrue(portfolioDTO.getBondsAmount() instanceof BigDecimal);
        assertEquals(portfolioDTO.getAssetsClassAmount(1).add(portfolioDTO.getAssetsClassAmount(2).add(portfolioDTO.getAssetsClassAmount(3).add(portfolioDTO.getAssetsClassAmount(4)))), portfolioDTO.getTotalAmount());
        assertEquals(portfolioDTO.getDate(), LocalDate.now());
    }
}

