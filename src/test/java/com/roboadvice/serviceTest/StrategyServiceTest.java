package com.roboadvice.serviceTest;

import com.roboadvice.RoboadviceApplication;
import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.service.StrategyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)
public class StrategyServiceTest {

    @Autowired
    private StrategyService strategyService;

    @Test
    public void getCurrentActiveStrategyTestOK(){
        String userEmail = "leo@galati.com";

        StrategyDTO strategyDTO = strategyService.getCurrentActiveStrategy(userEmail);

        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertTrue(strategyDTO.getName() instanceof String);
        assertTrue(strategyDTO.getBonds_p() instanceof BigDecimal);
        assertEquals(strategyDTO.getActive(), true);

    }

    @Test
    public void getCurrentActiveStrategyTestFAILURE(){
        String userEmail = "ukjghgf";

        StrategyDTO strategyDTO = strategyService.getCurrentActiveStrategy(userEmail);

        assertNull(strategyDTO);
        assertFalse(strategyDTO instanceof StrategyDTO);
    }


}
