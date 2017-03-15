package com.roboadvice.controller;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.model.Portfolio;
import com.roboadvice.model.User;
import com.roboadvice.service.PortfolioService;
import com.roboadvice.service.UserService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    private PortfolioService portfolioService;
    private UserService userService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService, UserService userService) {
        this.portfolioService = portfolioService;
        this.userService = userService;
    }

    @RequestMapping(value = "/getCurrent", method = RequestMethod.POST)
    public GenericResponse<PortfolioDTO> getAmount(Authentication authentication){
        //portfolioDTO = {totalAmount, bondsAmount, stocksAmanount, forexAmount, commoditiesAmount, date}
        String email = authentication.getName();
        User u = userService.selectByEmail(email);
        if(u != null){
            List<Portfolio> portfolioList = portfolioService.getCurrent(u);
            if(portfolioList != null && !portfolioList.isEmpty()){
                PortfolioDTO portfolioDTO = new PortfolioDTO();
                portfolioDTO.setTotalAmount(BigDecimal.ZERO);
                for(Portfolio p : portfolioList){
                    portfolioDTO.setTotalAmount(portfolioDTO.getTotalAmount().add(p.getValue()));
                }
                for(int i=0;i<Constant.NUM_ASSETS_CLASS;i++){
                    portfolioDTO.setAssetsClassAmount(i+1, portfolioList.get(i).getValue() );
                    portfolioDTO.setAssetsClassPercentage(i+1, portfolioDTO.getAssetsClassAmount(i+1).multiply(new BigDecimal(100)).divide(portfolioDTO.getTotalAmount(), 2, RoundingMode.HALF_UP));
                }
                portfolioDTO.setDate(portfolioList.get(0).getDate());
                return new GenericResponse<>(portfolioDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
            }
            else
                return new GenericResponse<>(null, "THE USER HASN'T AN UPDATED PORTFOLIO FOR TODAY. PLEASE RETRY IN A FEW HOURS.", Constant.ERROR);
        }
        else
            return new GenericResponse<>(null, "USER NOT FOUND!", Constant.ERROR);
    }


    @RequestMapping(value = "/getHistoryByDate", method = RequestMethod.POST)
    public GenericResponse<List<PortfolioDTO>> getHistoryByDate(@RequestParam(value = "fromDate", defaultValue = "null") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                @RequestParam(value = "toDate", defaultValue = "null") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                Authentication authentication) {

        if(fromDate.isAfter(toDate))
            return new GenericResponse<>(null, "FROM-DATE CANNOT BE AFTER TO-DATE!", Constant.ERROR);

        String email = authentication.getName();
        User u = userService.selectByEmail(email);
        if(u != null){
            List<Portfolio> portfolioList = portfolioService.historyByUserAndDates(u, fromDate, toDate);

            if (portfolioList != null && !portfolioList.isEmpty()) {
                List<PortfolioDTO> portfolioDTO_list = new ArrayList<>();

                PortfolioDTO pDTO = new PortfolioDTO();
                for(int i=0; i<portfolioList.size();i+=Constant.NUM_ASSETS_CLASS) {
                    pDTO = new PortfolioDTO();

                    pDTO.setDate(portfolioList.get(i).getDate());

                    pDTO.setTotalAmount(BigDecimal.ZERO);

                    for (int j = i; j < i + Constant.NUM_ASSETS_CLASS; j++) {
                        pDTO.setTotalAmount(pDTO.getTotalAmount().add(portfolioList.get(j).getAmount()));
                        pDTO.setAssetsClassAmount(portfolioList.get(j).getAssetsClass().getId(), portfolioList.get(j).getAmount());
                    }
                    for (int y = i; y < i + Constant.NUM_ASSETS_CLASS; y++) {
                        pDTO.setAssetsClassPercentage(portfolioList.get(y).getAssetsClass().getId(), portfolioList.get(y).getAmount().multiply(new BigDecimal(100).divide(pDTO.getTotalAmount(), 2, RoundingMode.HALF_UP)));
                    }
                    portfolioDTO_list.add(pDTO);
                }
                return new GenericResponse<>(portfolioDTO_list, Constant.SUCCES_MSG, Constant.SUCCESS);
            } else {
                return new GenericResponse<>(null, "THE USER HAS NO PORTFOLIOs YET!", Constant.ERROR);
            }
        } else {
            return new GenericResponse<>(null, "USER NOT FOUND!", Constant.ERROR);
        }
    }

    @RequestMapping(value = "/getFullHistory", method = RequestMethod.POST)
    public GenericResponse<List<PortfolioDTO>> getFullHistory(Authentication authentication){
        String email = authentication.getName();
        User u = userService.selectByEmail(email);
        if(u != null) {

            List<Portfolio> portfolioList = portfolioService.fullHistoryByUser(u);

            if(portfolioList != null && !portfolioList.isEmpty()){
                List<PortfolioDTO> portfolioDTO_list = new ArrayList<>();
                PortfolioDTO pDTO = new PortfolioDTO();
                for(int i=0; i<portfolioList.size();i+=Constant.NUM_ASSETS_CLASS) {
                    pDTO = new PortfolioDTO();

                    pDTO.setDate(portfolioList.get(i).getDate());

                    pDTO.setTotalAmount(BigDecimal.ZERO);

                    for(int j=i;j<i+Constant.NUM_ASSETS_CLASS;j++) {
                        pDTO.setTotalAmount(pDTO.getTotalAmount().add(portfolioList.get(j).getAmount()));
                        pDTO.setAssetsClassAmount(portfolioList.get(j).getAssetsClass().getId(), portfolioList.get(j).getAmount());
                    }
                    for(int y=i;y<i+Constant.NUM_ASSETS_CLASS;y++) {
                        pDTO.setAssetsClassPercentage(portfolioList.get(y).getAssetsClass().getId(), portfolioList.get(y).getAmount().multiply(new BigDecimal(100).divide(pDTO.getTotalAmount(), 2, RoundingMode.HALF_UP)));
                    }
                    portfolioDTO_list.add(pDTO);
                }
                return new GenericResponse<>(portfolioDTO_list, Constant.SUCCES_MSG, Constant.SUCCESS);
            }
            else {
                return new GenericResponse<>(null, "THE USER HAS NO PORTFOLIOs YET!", Constant.ERROR);
            }
        }
        else
            return new GenericResponse<>(null, "USER NOT FOUND!", Constant.ERROR);
    }




}
