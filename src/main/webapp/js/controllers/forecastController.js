RoboAdviceApp.controller("Forecast",function($scope,$log){

  $scope.startForecast = function(date){
    $log.debug("Forecasting started")
    $log.debug(date)
    $scope.forecastDate = date;
  }
});
