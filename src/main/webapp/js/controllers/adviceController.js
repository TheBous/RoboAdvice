RoboAdviceApp.controller("AdviceController",function($scope,strategyService){
  $scope.standardStrategies = strategyService.getStandardStrategies();

  $scope.currentStrategy = strategyService.getLastStrategy();
  $scope.currentStrategyTipology = "Custom";

});
