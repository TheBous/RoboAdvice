RoboAdviceApp.controller("RealtimeGraphController",function($scope,demoREST,$log){

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
    $scope.realtimeDates = data[0];
    $scope.realtimeAmounts = data[1];
    //saveRealtime();
  }

  // check if exists something in localStorage
  getRealtime();
  if($scope.realtimeAmounts == null){
    // there isn't nothing
    // get the data from the forecast
    let portfolios = new Array();

    demoREST.forecast().$promise.then(function(response){
      console.log(response.data);
      response.data.forEach(function(obj){
        let portfolioObj = new Portfolio(obj);
        portfolios.push(portfolioObj);
      });
      let portfolioNum = portfolios.length;
      $log.debug("RealtimeGraphController | " + portfolioNum + " portfolios from forecast ");

      $scope.realtimeAmounts = new Array(portfolioNum);
      $scope.realtimeDates = new Array(portfolioNum);

      // push inside amounts and dates
      for(let i=0;i<portfolioNum;i++){
        // the date is a timestamp
        $scope.realtimeDates[i]=portfolios[i].getDate().getTime();
        // the amount is the total amount for each class assets
        $scope.realtimeAmounts[i]=portfolios[i].getTotalAmount();
      }

    });

    //saveRealtime();
  }

});
