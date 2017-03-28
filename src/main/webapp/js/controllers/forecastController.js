RoboAdviceApp.controller("Forecast",function($scope,$log,strategyService,portfolioService){
  $scope.forecastAmounts = null;
  $scope.forecastDates = null;

  $scope.loadingInProgress = true;

  $scope.startForecast = function(date){
    $log.debug("Forecasting started " + date);
    $scope.forecastDate = date;

    // default forecast for 1 mounth

    portfolioService.getForecasting($scope.forecastDate,function(forecastData){
      /*
      forecastData is an array of objects like this:
      { amount, date }
      */
      $scope.loadingInProgress = false;

      $scope.forecastAmounts = new Array();
      $scope.forecastDates = new Array();

      $scope.forecastAmounts = forecastData.amounts;
      $scope.forecastDates = forecastData.dates;
    });
  }// end startForecast

});
