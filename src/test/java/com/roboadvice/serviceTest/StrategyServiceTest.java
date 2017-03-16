package com.roboadvice.serviceTest;

import com.roboadvice.RoboadviceApplication;
import com.roboadvice.dto.StrategyDTO;
import com.roboadvice.model.Strategy;
import com.roboadvice.service.StrategyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)
public class StrategyServiceTest {

    @Autowired
    private StrategyService strategyService;

    @Test
    public void getCurrentActiveStrategyTestOK() {
        String userEmail = "leo@galati.com";

        StrategyDTO strategyDTO = strategyService.getCurrentActiveStrategy(userEmail);

        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertTrue(strategyDTO.getName() instanceof String);
        assertTrue(strategyDTO.getBonds_p() instanceof BigDecimal);
        assertEquals(strategyDTO.getActive(), true);

    }

    @Test
    public void getCurrentActiveStrategyTestFAILURE() {
        String userEmail = "ukjghgf";

        StrategyDTO strategyDTO = strategyService.getCurrentActiveStrategy(userEmail);

        assertNull(strategyDTO);
        assertFalse(strategyDTO instanceof StrategyDTO);
    }


    @Test
    public void getLastUsedStrategyTestOk() {
        String userEmail = "leo@galati.com";

        StrategyDTO strategyDTO = strategyService.getLastUsedStrategy(userEmail);

        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof StrategyDTO);
        assertTrue(strategyDTO.getName() instanceof String);
        assertTrue(strategyDTO.getBonds_p() instanceof BigDecimal);
        assertEquals(strategyDTO.getActive(), false);
    }

    @Test
    public void getLastUsedStrategyTestFailure() {
        String userEmail = "duino@pasquale.com";

        StrategyDTO strategyDTO = strategyService.getLastUsedStrategy(userEmail);

        assertNull(strategyDTO);
        assertFalse(strategyDTO instanceof StrategyDTO);

    }

    @Test
    public void getFullHistoryByUserTestOk() {
        String userEmail = "leo@galati.com";

        List<StrategyDTO> strategyDTO = strategyService.getFullHistoryByUser(userEmail);

        assertNotNull(strategyDTO);
        assertTrue(strategyDTO instanceof  List<?>);
        assertTrue(strategyDTO.size() > 0);
        for (StrategyDTO str : strategyDTO) {
            assertTrue(str.getName() instanceof String);
            assertTrue(str.getBonds_p() instanceof BigDecimal);
            assertTrue(str.getDate() instanceof LocalDate);
            assertTrue(str.getActive() instanceof Boolean);
        }
    }

    @Test
    public void getFullHistoryByUserEmailNotExistTestFailure() {
        String userEmail = "duino@pasquale.com";

        List<StrategyDTO> strategyDTO = strategyService.getFullHistoryByUser(userEmail);

        assertNull(strategyDTO);
        assertFalse(strategyDTO instanceof  List<?>);
    }

    @Test
    public void getFullHistoryByUserWithoutAnHistoryTestFailure() {
        String userEmail = "test@test.it";

        List<StrategyDTO> strategyDTO = strategyService.getFullHistoryByUser(userEmail);

        assertNull(strategyDTO);
        assertFalse(strategyDTO instanceof  List<?>);
    }

    /*@Test
    public void newStrategiesFromNewUsersTestFailure() {

        List<Strategy> strategies = strategyService.newStrategiesFromNewUsers();

        assertNull(strategies);
        assertFalse(strategies instanceof  List<?>);

    }

    @Test
    public void newStrategiesFromOldUserTestOk() {

        List<Strategy> strategies = strategyService.newStrategiesFromOldUsers();

        assertNotNull(strategies);
        assertTrue(strategies instanceof  List<?>);
        assertTrue(!strategies.isEmpty());
        for (Strategy str : strategies) {
            assertTrue(str.getActive() instanceof Boolean);
            assertTrue(str.getName() instanceof String);
            assertTrue(str.getDate() instanceof LocalDate);
            assertTrue(str.getPercentage() instanceof BigDecimal);

        }
    }

    @Test
    public void newStrategiesFromOldUserTestFailure() {
        List<Strategy> strategies = strategyService.newStrategiesFromOldUsers();

        assertNull(strategies);
        assertFalse(strategies instanceof  List<?>);
    }

    @Test
    public void deletePendingStrategyTestOk() {
        String userEmail = "enrico@pasquale.com";

        Boolean delete = strategyService.deletePendingStrategy(userEmail);

        assertNotNull(delete);
        assertTrue(delete);
    }*/

    @Test
    public void deletePendingStrategyTestFailure() {
        String userEmail = "enrico@pasquale.com";

        Boolean delete = strategyService.deletePendingStrategy(userEmail);
        assertFalse(delete);
    }
}
