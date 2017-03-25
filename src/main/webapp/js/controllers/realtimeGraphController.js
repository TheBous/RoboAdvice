RoboAdviceApp.controller("RealtimeGraphController",function($scope,strategyService,$log){

  // save the state
  let saveRealtime = function(){
    sessionStorage.realtimeAmounts = angular.toJson($scope.realtimeAmounts);
    sessionStorage.realtimeDates = angular.toJson($scope.realtimeDates);
  }

  // get the state
  let getRealtime = function(){
    $scope.realtimeAmounts = angular.fromJson(sessionStorage.realtimeAmounts);
    $scope.realtimeDates = angular.fromJson(sessionStorage.realtimeDates);
  }

  $scope.stimatedAmount = 100;
  $scope.incrementData = function(data){
    // save the current state
    $scope.stimatedAmount = 10000;
    saveRealtime();
  }

  // check if exists something in localStorage
  getRealtime();
  if($scope.realtimeAmounts == null){
    // there isn't nothing
    // get the data from last portfolio
    let portfolios = strategyService.getLastStrategy().getPortfolios();
    let portfolioNum = portfolios.length;

    // initialize the array
    $scope.realtimeAmounts = new Array(portfolioNum);
    $scope.realtimeDates = new Array(portfolioNum);
    //get the last
  //  $scope.realtimeDates[0] = portfolios[portfolioNum-1].getDate().getTime();
//    $scope.realtimeAmounts[0] = portfolios[portfolioNum-1].getTotalAmount();

    // push inside amounts and dates
    for(let i=0;i<portfolioNum;i++){
      // th edate is a timestamp
      $scope.realtimeDates[i]=portfolios[i].getDate().getTime();
      // the amount is the total amount for each class assets
      $scope.realtimeAmounts[i]=portfolios[i].getTotalAmount();
    }
    saveRealtime();
  }

});
