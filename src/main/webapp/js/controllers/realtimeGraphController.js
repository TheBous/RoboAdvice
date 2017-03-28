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
  //getRealtime();
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

      $scope.stimatedAmount = portfolios[portfolioNum-1].getTotalAmount();

      $log.debug("RealtimeGraphController | " + portfolioNum + " portfolios from forecast ");

      // push data for render highchart properly
      $scope.realtimeAmounts = new Array(19);
      $scope.realtimeDates = new Array(19);
      // now
      let time = (new Date()).getTime();

      // 19 seconds before now
      let j=0;
      for(let i=-19;i<0;i++){
        $scope.realtimeAmounts[j] = (portfolios[0].getTotalAmount());
        $scope.realtimeDates[j] = (time+i*1000);
        j++;
      }

      //saveRealtime();
    });
  }

});
