RoboAdviceApp.controller("Forecast",function($scope,$log,strategyService,portfolioService){

  $scope.startForecast = function(date){
    $log.debug("Forecasting started");

    if(date == null){
      // default forecast for 1 mounth
      portfolioService.getForecasting(function(forecastData){
        /*
        forecastData is an array of objects like this:
        { amount, date }
        */
        $scope.forecastAmounts = forecastData.amounts;
        $scope.forecastDates = forecastData.dates;
      });
    }else{
      // forecast by date
      // get forecast from server
      $scope.forecastDate = date;
    }
  }// end startForecast

});
