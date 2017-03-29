package com.roboadvice.utils;

import java.io.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.roboadvice.dto.ForecastingDTO;
import com.roboadvice.model.Portfolio;
import weka.core.Instances;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.timeseries.WekaForecaster;

public class WekaTimeSeriesForecasting {

    public static List<ForecastingDTO> getForecast(List<Portfolio> portfolioList, long daysToForecast){
        try {
            List<ForecastingDTO> forecastingDTOList = new ArrayList<>();
            ForecastingDTO fDTO;

            String data="@relation portfolio\n@attribute amount numeric\n@attribute Date date 'yyyy-MM-dd'\n@data\n";
            for(Portfolio p : portfolioList){
                data = data+""+p.getAmount()+","+p.getDate().toString()+"\n";
            }

            String fileName = "forecast_"+portfolioList.get(0).getUser().getId()+".txt";
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println(data);
            writer.close();

            // load the data
            Instances portfolio = new Instances(new BufferedReader(new FileReader(fileName)));

            // new forecaster
            WekaForecaster forecaster = new WekaForecaster();

            // set the targets we want to forecast
            forecaster.setFieldsToForecast("amount");

            // we'll use gaussian processes for regression calculation
            forecaster.setBaseForecaster(new GaussianProcesses());

            forecaster.getTSLagMaker().setTimeStampField("Date"); // date time stamp
            forecaster.getTSLagMaker().setMinLag(1);
            forecaster.getTSLagMaker().setMaxLag(1); // daily data

            // add a month of the year indicator field
            forecaster.getTSLagMaker().setAddMonthOfYear(true);

            // add a quarter of the year indicator field
            forecaster.getTSLagMaker().setAddQuarterOfYear(true);

            // build the model
            forecaster.buildForecaster(portfolio, System.out);

            // prime the forecaster with enough recent historical data
            // to cover up to the maximum lag. In our case, we could just supply
            // the most recent historical instances
            forecaster.primeForecaster(portfolio);

            // forecast for 'daysToForecast' values beyond the end of the
            // training data
            List<List<NumericPrediction>> forecast = forecaster.forecast((int) daysToForecast, System.out);

            // output the predictions. Outer list is over the steps; inner list is over
            // the targets
            for (int i = 0; i < daysToForecast; i++) {
                List<NumericPrediction> predsAtStep = forecast.get(i);
                NumericPrediction predForTarget = predsAtStep.get(0);
                //System.out.println("" + predForTarget.predicted() + " ");
                fDTO = new ForecastingDTO();
                if(predForTarget.predicted()>0)
                    fDTO.setTotalAmount(new BigDecimal(predForTarget.predicted()));
                else
                    fDTO.setTotalAmount(new BigDecimal(0));
                fDTO.setDate(portfolioList.get(portfolioList.size()-1).getDate().plusDays(i+1));

                forecastingDTOList.add(fDTO);
            }

            return forecastingDTOList;

        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }

    }
}
