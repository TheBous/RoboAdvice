RoboAdviceApp.controller("Forecast",function($scope,$log,strategyService,portfolioService){

    $scope.loadingInProgress = true;

    $scope.startForecast = function(date){
    $log.debug("Forecasting started " + date);
    $scope.forecastDate = date;
    
    if(date == "")
      $scope.forecastDate = null;
      // default forecast for 1 mounth
      portfolioService.getForecasting($scope.forecastDate,function(forecastData){
          $scope.loadingInProgress = false;

          /*
          forecastData is an array of objects like this:
          { amount, date }
          */
        $scope.forecastAmounts = forecastData.amounts;
        $scope.forecastDates = forecastData.dates;
      });

  }// end startForecast

});
