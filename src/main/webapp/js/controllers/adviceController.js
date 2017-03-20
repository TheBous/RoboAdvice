RoboAdviceApp.controller("AdviceController",function($scope,strategyService){
  $scope.standardStrategies = strategyService.getStandardStrategies();

  $scope.currentStrategy = strategyService.getLastStrategy();
  $scope.currentStrategyTipology = "Custom";

  $scope.changeStrategy = function(){
    // the user wants to change the current strategy based on advice
    alert("the user wants to change his life");
  }

});
