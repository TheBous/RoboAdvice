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

  // increment the data using forecast
  $scope.incrementData = function(){
    let rnd = Math.floor((Math.random()*100)+1)
    if(rnd % 2 == 0)rnd*=-1;
    let now = new Date();

    $scope.currentTime = now.getDateFormatted() + " " + now.getHours() + ":" + now.getMinutes();

    // new value
    let lastValue = $scope.lastStrategyAmounts[$scope.lastStrategyAmounts.length-1];

    // add the new value
    $scope.lastStrategyAmounts.push(lastValue + rnd);

    // add the new date
    let updateTime = new Date();
    $scope.lastStrategyDates.push(now.getHours() + ":" + now.getMinutes());
    saveRealtime();
  };

  // check if exists somwthing in the local storage
  getRealtime();
  if($scope.lastStrategyAmounts!=null){// exists

  }else{// there is nothing in the local local storage

    // get the history for the current strategy
    $scope.strategyPortfolios = strategyService.getLastStrategy().getPortfolios();

    let portfolioNum = $scope.strategyPortfolios.length;
    // set the new array
    $scope.lastStrategyDates = new Array(portfolioNum);
    $scope.lastStrategyAmounts = new Array(portfolioNum);

    // set the date in human style
    $scope.strategyPortfolios.forEach(function(portfolio,$index){
      $scope.lastStrategyAmounts[$index] = portfolio.getTotalAmount();

      $scope.lastStrategyDates[$index] = portfolio.getFormattedDate();

    });//for each end

  }


});
