package com.roboadvice.serviceTest;

import com.roboadvice.RoboadviceApplication;
import com.roboadvice.model.Assets;
import com.roboadvice.model.AssetsClass;
import com.roboadvice.repository.AssetsClassRepository;
import com.roboadvice.repository.AssetsRepository;
import com.roboadvice.service.AssetsClassService;
import com.roboadvice.service.AssetsService;
import com.roboadvice.utils.Constant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Created by CorsoPc on 29/03/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)
public class AssetsServiceMockTest {

    @InjectMocks
    @Autowired
    private AssetsService assetsService ;

    @Mock
    private AssetsRepository assetsRepository;

    @Test
    public void insertTestOk(){

        AssetsClass assetsClass[] = new AssetsClass[Constant.NUM_ASSETS_CLASS];
        assetsClass[0] = new AssetsClass(1, "Bonds");
        assetsClass[1] = new AssetsClass(2, "Stocks");
        assetsClass[2] = new AssetsClass(3, "Forex");
        assetsClass[3] = new AssetsClass(4, "Commodities");


        //PROCEDURA PER INSERIRE GLI ASSETS NEL DB
        Assets ast[] = new Assets[Constant.NUM_ASSETS];
        ast[0] = new Assets(1, "U.S. Treasury Bond Futures", new BigDecimal(80), "CHRIS/CME_US1", 1, assetsClass[0]);
        ast[1] = new Assets(2, "Ultra U.S. Treasury Bond Futures", new BigDecimal(20), "CHRIS/CME_UL1", 1, assetsClass[0]);
        ast[2] = new Assets(3, "Facebook", new BigDecimal(25), "WIKI/FB", 11, assetsClass[1]);
        ast[3] = new Assets(4, "Apple", new BigDecimal(20), "WIKI/AAPL", 11, assetsClass[1]);
        ast[4] = new Assets(5, "Microsoft", new BigDecimal(35), "WIKI/MSFT", 11, assetsClass[1]);
        ast[5] = new Assets(6, "Twitter", new BigDecimal(20), "WIKI/TWTR", 11, assetsClass[1]);
        ast[6] = new Assets(7, "USD -> EUR", new BigDecimal(25), "CURRFX/USDEUR", 1, assetsClass[2]);
        ast[7] = new Assets(8, "USD -> CHF", new BigDecimal(50), "CURRFX/USDCHF", 1, assetsClass[2]);
        ast[8] = new Assets(9, "USD -> BIT", new BigDecimal(25), "BTCE/USDBTC", 3, assetsClass[2]);
        ast[9] = new Assets(10, "Gold", new BigDecimal(30), "COM/WLD_GOLD", 1, assetsClass[3]);
        ast[10] = new Assets(11, "Silver", new BigDecimal(20), "COM/WLD_SILVER", 1, assetsClass[3]);
        ast[11] = new Assets(12, "Crude oil", new BigDecimal(30), "COM/OIL_BRENT", 1, assetsClass[3]);
        ast[12] = new Assets(13, "Rice", new BigDecimal(20), "COM/WLD_RICE_05", 1, assetsClass[3]);

        Assets newAsset = new Assets(15, "Cetrioli", new BigDecimal(20), "COM/WLD_CUCUMBER_05", 1, assetsClass[3]);

        when(assetsRepository.findById(1)).thenReturn(ast[0]);
        when(assetsRepository.findById(2)).thenReturn(ast[1]);
        when(assetsRepository.findById(3)).thenReturn(ast[2]);
        when(assetsRepository.findById(4)).thenReturn(ast[3]);
        when(assetsRepository.findById(5)).thenReturn(ast[4]);
        when(assetsRepository.findById(6)).thenReturn(ast[5]);
        when(assetsRepository.findById(7)).thenReturn(ast[6]);
        when(assetsRepository.findById(8)).thenReturn(ast[7]);
        when(assetsRepository.findById(9)).thenReturn(ast[8]);
        when(assetsRepository.findById(10)).thenReturn(ast[9]);
        when(assetsRepository.findById(11)).thenReturn(ast[10]);
        when(assetsRepository.findById(12)).thenReturn(ast[11]);
        when(assetsRepository.findById(13)).thenReturn(ast[12]);
        when(assetsRepository.findById(15)).thenReturn(null);

        when(assetsRepository.save(newAsset)).thenReturn(newAsset);


        for(int i=0;i<ast.length;i++){
            Assets assets = assetsService.insert(ast[i]);
            assertEquals(ast[0].getId(), 1);
            assertEquals(ast[1].getId(), 2);
            assertEquals(ast[2].getId(), 3);
            assertNotEquals(ast[3].getId(), 22);
            assertNotNull(ast);
            assertTrue(assets instanceof Assets);
            assertNotNull(assets);
        }
        Assets assets = assetsService.insert(newAsset);
        assertTrue(assets instanceof Assets);
        assertEquals(assets.getId(), 15);

    }
}
