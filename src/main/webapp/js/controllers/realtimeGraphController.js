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
  $scope.incrementData = function(forecastValue){
    let now = new Date();
    let lastValue = $scope.lastStrategyAmounts[$scope.lastStrategyAmounts.length-1];
    let pulse = 0;

    if(forecastValue != null){
      // get the increment for seconds
      // we have 2 hours until the token expires, so we have 60*2*60 seconds = 7200 seconds
      // let calc the pulse, it is the difference betwwen forecastValue and last value
      // per second
      pulse = (forecastValue-lastValue)/7;
      //$log.debug("realtimeGraphCtrl| forecastValue: " + forecastValue);
      //$log.debug("realtimeGraphCtrl| realtimeValue: " + lastValue);
    }

    $scope.currentTime = now.getDateFormatted() + " " + now.getHours() + ":" + now.getMinutes();
    let newValue = 0;
    if(pulse!=0){
      // if there is some change in the value, set the new value
      let rnd = Math.floor((Math.random()*pulse)+1);
      newValue = lastValue + rnd;
    }
    // add the new value
    $log.debug("realtimeGraphCtrl| pulse: " + pulse);
    /*
      MAGIC
    */
    if($scope.lastStrategyAmounts.length > 4){
      let last_index = $scope.lastStrategyAmounts.length-1;
      for(let index = 1; index<last_index; index++){
        $scope.lastStrategyAmounts[index] = $scope.lastStrategyAmounts[index+1];
        $scope.lastStrategyDates[index] = $scope.lastStrategyDates[index+1];
      }
      $scope.lastStrategyAmounts[last_index] = newValue;
      $scope.lastStrategyDates[last_index] = now.getHoursFormatted() + ":" + now.getMinutesFormatted();
    }else{
      $scope.lastStrategyAmounts.push(newValue);
      $scope.lastStrategyDates.push(now.getHoursFormatted() + ":" + now.getMinutesFormatted());
    }
    saveRealtime();
    /*
      /MAGIC
    */

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
