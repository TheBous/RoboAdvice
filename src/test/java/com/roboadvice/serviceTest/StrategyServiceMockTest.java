package com.roboadvice.serviceTest;

        import com.roboadvice.RoboadviceApplication;
        import com.roboadvice.dto.StrategyDTO;
        import com.roboadvice.model.AssetsClass;
        import com.roboadvice.model.Strategy;
        import com.roboadvice.model.User;
        import com.roboadvice.repository.StrategyRepository;
        import com.roboadvice.repository.UserRepository;
        import com.roboadvice.service.StrategyService;
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

        import java.math.BigDecimal;
        import java.time.LocalDate;
        import java.util.ArrayList;
        import java.util.List;


        import static org.junit.Assert.*;
        import static org.mockito.Mockito.when;
        import static org.mockito.Mockito.verify;

/**
 * Created by CorsoPc on 17/03/2017.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)

public class StrategyServiceMockTest {

    @InjectMocks
    @Autowired
    private StrategyService strategyService;

    @Mock
    UserRepository userRepository;

    @Mock
    StrategyRepository strategyRepository;

    private static User user;

    @BeforeClass
    public static void setUpAuth() {
        user = new User();
        user.setId(new Long(1));
        user.setEmail("enrico@pasquale.com");
        user.setPassword("enry");
        user.setName("Enrico");
        user.setSurname("Pasquale");
        user.setRole("USER");
    }

    @Before
    public void setUpMock() {
        MockitoAnnotations.initMocks(this);
        when(userRepository.findByEmail("enrico@pasquale.com")).thenReturn(user);
    }
    @Test
    public void getCurrentActiveStrategySuccess() {

        List<Strategy> resultList = new ArrayList<>();
        Strategy strategy1 = new Strategy();

        AssetsClass assetsClass1 = new AssetsClass();
        assetsClass1.setId(new Long(1));
        assetsClass1.setName("Bonds");
        strategy1.setName("BestStrategy");
        strategy1.setActive(true);
        strategy1.setAssetsClass(assetsClass1);
        strategy1.setDate(LocalDate.now());
        strategy1.setUser(user);
        strategy1.setPercentage(new BigDecimal(50.00) );

        Strategy strategy2 = new Strategy();
        AssetsClass assetsClass2 = new AssetsClass();
        assetsClass2.setId(new Long(2));
        assetsClass2.setName("Stocks");
        strategy2.setName("BestStrategy");
        strategy2.setActive(true);
        strategy2.setAssetsClass(assetsClass2);
        strategy2.setDate(LocalDate.now());
        strategy2.setUser(user);
        strategy2.setPercentage(new BigDecimal(50.00) );

        resultList.add(strategy1);
        resultList.add(strategy2);


        when(strategyRepository.findByUserAndActive(user,true)).thenReturn(resultList);
        StrategyDTO strategyDTO = this.strategyService.getCurrentActiveStrategy(user.getEmail());
        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertTrue(strategyDTO.getName() instanceof String);
        assertTrue(strategyDTO.getBonds_p() instanceof BigDecimal);
        assertTrue(strategyDTO.getStocks_p() instanceof BigDecimal);
        assertEquals(strategyDTO.getActive(), true);
        verify(strategyRepository).findByUserAndActive(user, true);
        assertEquals(LocalDate.now().toString(), strategyDTO.getDate().toString());
    }

}