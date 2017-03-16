package com.roboadvice;

import com.roboadvice.model.*;
import com.roboadvice.service.*;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@ComponentScan(basePackages = {"com.roboadvice"})
public class RoboadviceApplication {

	private AssetsClassService assetsClassService;
	private AssetsService assetsService;

	@Autowired
	public RoboadviceApplication(AssetsClassService assetsClassService, AssetsService assetsService) {
		this.assetsClassService = assetsClassService;
		this.assetsService = assetsService;
	}

	@PostConstruct
	public void initializeDB(){
		//PROCEDURA PER INSERIRE GLI ASSETS_CLASS NEL DB
		AssetsClass assetsClass[] = new AssetsClass[Constant.NUM_ASSETS_CLASS];
		assetsClass[0] = new AssetsClass(1, "Bonds");
		assetsClass[1] = new AssetsClass(2, "Stocks");
		assetsClass[2] = new AssetsClass(3, "Forex");
		assetsClass[3] = new AssetsClass(4, "Commodities");

		for(int i=0;i<assetsClass.length;i++){
			assetsClass[i] = assetsClassService.insert(assetsClass[i]);
		}

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
		ast[8] = new Assets(9, "USD -> BIT", new BigDecimal(25), "BAVERAGE/USD", 1, assetsClass[2]);
		ast[9] = new Assets(10, "Gold", new BigDecimal(30), "COM/WLD_GOLD", 1, assetsClass[3]);
		ast[10] = new Assets(11, "Silver", new BigDecimal(20), "COM/WLD_SILVER", 1, assetsClass[3]);
		ast[11] = new Assets(12, "Crude oil", new BigDecimal(30), "COM/OIL_BRENT", 1, assetsClass[3]);
		ast[12] = new Assets(13, "Rice", new BigDecimal(20), "COM/WLD_RICE_05", 1, assetsClass[3]);

		for(int i=0;i<ast.length;i++){
			ast[i] = assetsService.insert(ast[i]);
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(RoboadviceApplication.class, args);
	}
}
