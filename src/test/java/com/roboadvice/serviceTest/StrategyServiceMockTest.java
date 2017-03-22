package com.roboadvice.serviceTest;

import com.roboadvice.RoboadviceApplication;
import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.model.AssetsClass;
import com.roboadvice.model.Strategy;
import com.roboadvice.model.User;
import com.roboadvice.repository.AssetsClassRepository;
import com.roboadvice.repository.StrategyRepository;
import com.roboadvice.repository.UserRepository;
import com.roboadvice.service.StrategyService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Created by CorsoPc on 17/03/2017.
 */

// Comment Cache annotation on StrategyService for start every test.

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)

public class StrategyServiceMockTest {

    @InjectMocks
    @Autowired
    private StrategyService strategyService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StrategyRepository strategyRepository;

    @Mock
    private AssetsClassRepository assetsClassRepository;


    private  static  User user;

    @BeforeClass
    public static void setUpAuth() {
        user = new User(1,"Enrico","enry","enrico@pasqualino.com","Pasquale","USER");
    }

    @Before
    public void setUpMock() {
        MockitoAnnotations.initMocks(this);
        when(userRepository.findByEmail("enrico@pasqualino.com")).thenReturn(user);
    }

    @Test
    public void getCurrentActiveStrategySuccess() {

        List<Strategy> resultList = new ArrayList<>();

        AssetsClass assetsClass1 = new AssetsClass(1,"Bonds");
        Strategy strategy1 = new Strategy(1,"BestStrategy", LocalDate.now(),true,new BigDecimal(50.00),user,assetsClass1 );

        AssetsClass assetsClass2 = new AssetsClass(2,"Stocks");
        Strategy strategy2 = new Strategy(2,"BestStrategy", LocalDate.now(), true, new BigDecimal(50.00), user, assetsClass2);

        resultList.add(strategy1);
        resultList.add(strategy2);


        when(strategyRepository.findByUserAndActive(user,true)).thenReturn(resultList);
        StrategyDTO strategyDTO = this.strategyService.getCurrentActiveStrategy(user.getEmail());
        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertTrue(strategyDTO.getName() instanceof String);
        assertTrue(strategyDTO.getBonds_p() instanceof BigDecimal);
        assertTrue(strategyDTO.getStocks_p() instanceof BigDecimal);
        assertTrue(strategy1.getUser() instanceof User);
        assertTrue(strategy2.getUser() instanceof User);
        assertEquals(strategy1.getPercentage().add(strategy2.getPercentage()), new BigDecimal(100));
        assertEquals(strategyDTO.getActive(), true);
        verify(strategyRepository).findByUserAndActive(user, true);
        assertEquals(strategy1.getName(), strategy2.getName());
        assertEquals(LocalDate.now().toString(), strategyDTO.getDate().toString());
    }


    //Failure cause no One list of ActiveStrategy exist for that User
    @Test
    public void getCurrentActiveStrategyFailure() {

        List<Strategy> resultList = new ArrayList<>();

        when(strategyRepository.findByUserAndActive(user,true)).thenReturn(resultList);
        StrategyDTO strategyDTO = this.strategyService.getCurrentActiveStrategy(user.getEmail());
        assertNull(strategyDTO);
        assertFalse(strategyDTO instanceof StrategyDTO);
    }


    @Test
    public void getLastUsedStrategyOk() {

        List<Strategy> resultList = new ArrayList<>();

        AssetsClass assetsClass1 = new AssetsClass(1,"Bonds");
        Strategy strategy1 = new Strategy(1,"CurrentStrategy", LocalDate.now(),true,new BigDecimal(50.00),user,assetsClass1 );
        AssetsClass assetsClass1_1 = new AssetsClass(2,"Stocks");
        Strategy strategy1_1 = new Strategy(2,"CurrentStrategy", LocalDate.now(), true, new BigDecimal(50.00), user, assetsClass1_1);
        resultList.add(strategy1);
        resultList.add(strategy1_1);

        List<Strategy> resultList2 = new ArrayList<>();

        AssetsClass assetsClass2 = new AssetsClass(1,"Bonds");
        Strategy strategy2 = new Strategy(3,"OldStrategy", LocalDate.now().minus(Period.ofDays(1)),false,new BigDecimal(20.00),user,assetsClass2 );
        AssetsClass assetsClass2_1 = new AssetsClass(2,"Stocks");
        Strategy strategy2_1 = new Strategy(4,"OldStrategy",  LocalDate.now().minus(Period.ofDays(1)), false, new BigDecimal(50.00), user, assetsClass2_1);
        AssetsClass assetsClass2_2 = new AssetsClass(3,"Forex");
        Strategy strategy2_2 = new Strategy(5,"OldStrategy",  LocalDate.now().minus(Period.ofDays(1)), false, new BigDecimal(30.00), user, assetsClass2_2);
        resultList2.add(strategy2);
        resultList2.add(strategy2_1);
        resultList2.add(strategy2_2);

        when(strategyRepository.findLastUsedStrategy(user,new PageRequest(0, Constant.NUM_ASSETS_CLASS))).thenReturn(resultList2);
        StrategyDTO strategyDTO = strategyService.getLastUsedStrategy(user.getEmail());
        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertTrue(strategyDTO.getName() instanceof String);
        assertTrue(strategyDTO.getBonds_p() instanceof BigDecimal);
        assertTrue(strategyDTO.getStocks_p() instanceof BigDecimal);
        assertTrue(strategyDTO.getForex_p() instanceof BigDecimal);
        assertTrue(strategy2.getUser() instanceof User);
        assertTrue(strategy2_1.getUser() instanceof User);
        assertTrue(strategy2_2.getUser() instanceof User);
        assertEquals(strategy2.getPercentage().add(strategy2_1.getPercentage().add(strategy2_2.getPercentage())), new BigDecimal(100));
        assertEquals(strategyDTO.getActive(), false);
        verify(strategyRepository).findLastUsedStrategy(user,new PageRequest(0, Constant.NUM_ASSETS_CLASS));
        assertEquals(strategy2.getName(), strategy2_1.getName(), strategy2_2.getName());
        assertEquals(LocalDate.now().minus(Period.ofDays(1)).toString(), strategyDTO.getDate().toString());

    }


    // Failure cause the percentage used in the Last Strategy was above 100%
    @Test
    public void getLastUsedStrategyFailure() {

        List<Strategy> resultList = new ArrayList<>();

        AssetsClass assetsClass1 = new AssetsClass(1,"Bonds");
        Strategy strategy1 = new Strategy(1,"CurrentStrategy", LocalDate.now(),true,new BigDecimal(50.00),user,assetsClass1 );
        AssetsClass assetsClass1_1 = new AssetsClass(2,"Stocks");
        Strategy strategy1_1 = new Strategy(2,"CurrentStrategy", LocalDate.now(), true, new BigDecimal(50.00), user, assetsClass1_1);
        resultList.add(strategy1);
        resultList.add(strategy1_1);

        List<Strategy> resultList2 = new ArrayList<>();

        AssetsClass assetsClass2 = new AssetsClass(1,"Bonds");
        Strategy strategy2 = new Strategy(3,"OldStrategy", LocalDate.now().minus(Period.ofDays(1)),false,new BigDecimal(200.00),user,assetsClass2 );
        AssetsClass assetsClass2_1 = new AssetsClass(2,"Stocks");
        Strategy strategy2_1 = new Strategy(4,"OldStrategy",  LocalDate.now().minus(Period.ofDays(1)), false, new BigDecimal(50.00), user, assetsClass2_1);
        AssetsClass assetsClass2_2 = new AssetsClass(3,"Forex");
        Strategy strategy2_2 = new Strategy(5,"OldStrategy",  LocalDate.now().minus(Period.ofDays(1)), false, new BigDecimal(30.00), user, assetsClass2_2);
        resultList2.add(strategy2);
        resultList2.add(strategy2_1);
        resultList2.add(strategy2_2);

        when(strategyRepository.findLastUsedStrategy(user,new PageRequest(0, Constant.NUM_ASSETS_CLASS))).thenReturn(resultList2);
        StrategyDTO strategyDTO = strategyService.getLastUsedStrategy(user.getEmail());
        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertNotEquals(strategy2.getPercentage().add(strategy2_1.getPercentage().add(strategy2_2.getPercentage())), new BigDecimal(100));
        assertEquals(strategyDTO.getActive(), false);
        verify(strategyRepository).findLastUsedStrategy(user,new PageRequest(0, Constant.NUM_ASSETS_CLASS));
        assertEquals(strategy2.getName(), strategy2_1.getName(), strategy2_2.getName());
        assertEquals(LocalDate.now().minus(Period.ofDays(1)).toString(), strategyDTO.getDate().toString());

    }

    @Test
    public void insertTestOk(){

        StrategyDTO str = new StrategyDTO("bellixStrategy",true, LocalDate.now(), new BigDecimal(10), new BigDecimal(60), new BigDecimal(30), new BigDecimal(0));

        AssetsClass assetsClass1 = new AssetsClass(1,"Bonds" );
        AssetsClass assetsClass2 = new AssetsClass(2,"Stocks" );
        AssetsClass assetsClass3 = new AssetsClass(3,"Forex" );
        AssetsClass assetsClass4 = new AssetsClass(4,"Commodities" );

        Strategy strategy1 = new Strategy(0, str.getName(), LocalDate.now(), true, str.getPercentage(1), user, assetsClass1);
        Strategy strategy2 = new Strategy(0, str.getName(), LocalDate.now(), true, str.getPercentage(2), user, assetsClass2);
        Strategy strategy3 = new Strategy(0, str.getName(), LocalDate.now(), true, str.getPercentage(3), user, assetsClass3);
        Strategy strategy4 = new Strategy(0, str.getName(), LocalDate.now(), true, str.getPercentage(4), user, assetsClass4);


        when(strategyRepository.save(strategy1)).thenReturn(strategy1);
        when(strategyRepository.save(strategy2)).thenReturn(strategy2);
        when(strategyRepository.save(strategy3)).thenReturn(strategy3);
        when(strategyRepository.save(strategy4)).thenReturn(strategy4);

        StrategyDTO strategyDTO = strategyService.insert(user.getEmail(), str);
        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertEquals(strategy1.getPercentage().add(strategy2.getPercentage().add(strategy3.getPercentage().add(strategy4.getPercentage()))), new BigDecimal(100) );
        assertEquals(strategyDTO.getActive(), true);
        assertEquals("bellixStrategy", strategyDTO.getName());
    }

    // Insert Fail when the percentage of the strategy overcome 100%
    @Test
    public void insertTestFailure(){

        StrategyDTO str = new StrategyDTO("bellixStrategy",true, LocalDate.now(), new BigDecimal(100), new BigDecimal(60), new BigDecimal(30), new BigDecimal(200));

        AssetsClass assetsClass1 = new AssetsClass(1,"Bonds" );
        AssetsClass assetsClass2 = new AssetsClass(2,"Stocks" );
        AssetsClass assetsClass3 = new AssetsClass(3,"Forex" );
        AssetsClass assetsClass4 = new AssetsClass(4,"Commodities" );

        Strategy strategy1 = new Strategy(0, str.getName(), LocalDate.now(), true, str.getPercentage(1), user, assetsClass1);
        Strategy strategy2 = new Strategy(0, str.getName(), LocalDate.now(), true, str.getPercentage(2), user, assetsClass2);
        Strategy strategy3 = new Strategy(0, str.getName(), LocalDate.now(), true, str.getPercentage(3), user, assetsClass3);
        Strategy strategy4 = new Strategy(0, str.getName(), LocalDate.now(), true, str.getPercentage(4), user, assetsClass4);


        when(strategyRepository.save(strategy1)).thenReturn(strategy1);
        when(strategyRepository.save(strategy2)).thenReturn(strategy2);
        when(strategyRepository.save(strategy3)).thenReturn(strategy3);
        when(strategyRepository.save(strategy4)).thenReturn(strategy4);

        StrategyDTO strategyDTO = strategyService.insert(user.getEmail(), str);
        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertNotEquals(strategy1.getPercentage().add(strategy2.getPercentage().add(strategy3.getPercentage().add(strategy4.getPercentage()))), new BigDecimal(100) );
        assertEquals("bellixStrategy", strategyDTO.getName());
    }

    @Test
    public void deletePendingStrategyOk(){

        List<Strategy> resultList = new ArrayList<>();

        AssetsClass assetsClass1 = new AssetsClass(1,"Bonds");
        Strategy strategy1 = new Strategy(1,"PendingStrategy", LocalDate.now(),true,new BigDecimal(50.00),user,assetsClass1);
        AssetsClass assetsClass2 = new AssetsClass(2,"Stocks");
        Strategy strategy2 = new Strategy(2,"PendingStrategy", LocalDate.now(), true, new BigDecimal(50.00), user, assetsClass2);
        AssetsClass assetsClass3 = new AssetsClass(3,"Forex");
        Strategy strategy3 = new Strategy(3,"PendingStrategy", LocalDate.now(), true, new BigDecimal(0), user, assetsClass3);
        AssetsClass assetsClass4 = new AssetsClass(4,"Commodities");
        Strategy strategy4 = new Strategy(4,"PendingStrategy", LocalDate.now(), true, new BigDecimal(0), user, assetsClass4);

        resultList.add(strategy1);
        resultList.add(strategy2);
        resultList.add(strategy3);
        resultList.add(strategy4);

        List<Strategy> resultList2 = new ArrayList<>();


        AssetsClass assetsClass5 = new AssetsClass(1,"Bonds");
        Strategy strategy5 = new Strategy(1,"OldStrategy", LocalDate.now().minus(Period.ofDays(1)),false,new BigDecimal(60.00),user,assetsClass5);
        AssetsClass assetsClass6 = new AssetsClass(2,"Stocks");
        Strategy strategy6 = new Strategy(2,"OldStrategy", LocalDate.now().minus(Period.ofDays(1)), false, new BigDecimal(30.00), user, assetsClass6);
        AssetsClass assetsClass7 = new AssetsClass(3,"Forex");
        Strategy strategy7 = new Strategy(3,"OldStrategy", LocalDate.now().minus(Period.ofDays(1)), false, new BigDecimal(10.00), user, assetsClass7);
        AssetsClass assetsClass8 = new AssetsClass(4,"Commodities");
        Strategy strategy8 = new Strategy(4,"OldStrategy", LocalDate.now().minus(Period.ofDays(1)), false, new BigDecimal(0), user, assetsClass8);

        resultList2.add(strategy5);
        resultList2.add(strategy6);
        resultList2.add(strategy7);
        resultList2.add(strategy8);

        when(strategyRepository.deleteActiveStrategyByUserAndDate(user, LocalDate.now())).thenReturn(resultList.size());
        assertEquals(resultList.size(), 4);
        assertNotNull(resultList);
        when(strategyRepository.findLatestInactiveStrategy(user, new PageRequest(0, Constant.NUM_ASSETS_CLASS))).thenReturn(resultList2);
        for(Strategy rs2 : resultList2){
            rs2.setActive(true);
            when(strategyRepository.save(rs2)).thenReturn(rs2);
            assertEquals(rs2.getActive(), true);
        }
        Boolean risp = strategyService.deletePendingStrategy(user.getEmail());
        assertTrue(risp instanceof Boolean);
        assertNotNull(risp);
        assertTrue(risp);
    }
}
