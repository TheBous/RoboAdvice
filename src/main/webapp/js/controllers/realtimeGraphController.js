RoboAdviceApp.controller("RealtimeGraphController",function($scope,strategyService,$log){

  // save the state
  let saveRealtime = function(){
    sessionStorage.realtimeAmounts = angular.toJson($scope.lastStrategyAmounts);
    sessionStorage.realtimeDates = angular.toJson($scope.lastStrategyDates);
  }

  // get the state
  let getRealtime = function(){
    $scope.lastStrategyAmounts = angular.fromJson(sessionStorage.realtimeAmounts);
    $scope.lastStrategyDates = angular.fromJson(sessionStorage.realtimeDates);
  }

  $scope.incrementData = function(){

  }

  // check if exists something in localStorage
  getRealtime();
  if(null == null){
    // there isn't nothing
    // get the data from last portfolio
    let portfolios = strategyService.getLastStrategy().getPortfolios();
    let portfolioNum = portfolios.length;

    // initialize the array
    $scope.lastStrategyAmounts = new Array(portfolioNum);
    $scope.lastStrategyDates = new Array(portfolioNum);

    // push inside amounts and dates
    for(let i=0;i<portfolioNum;i++){
      // th edate is a timestamp
      $scope.lastStrategyDates[i]=portfolios[i].getDate().getTime();
      // the amount is the total amount for each class assets
      $scope.lastStrategyAmounts[i]=portfolios[i].getTotalAmount();
    }

  }else{
    // exists something

  }

});
