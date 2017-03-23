RoboAdviceApp.controller("Backtesting", function ($scope, $log, portfolioService) {
    $scope.xAxis = [];
    //$log.debug($scope.xAxis);
    $scope.yAxis = [];
    // $log.debug($scope.yAxis);

    $scope.update = function(data) {
        //var graphDates = [];
        let formattedDateGraph = [];
        let formattedValueGraph = [];

        $scope.TheInterval = data.interval;
        $log.debug("PortfolioService | Interval: "+ data.interval);
        portfolioService.getBacktesting(data.interval, function(response){
            if(response.statusCode == 0){
                $log.debug("backtesting Controller | callback");
                for(let i = 0;i<response.data.length;i++){
                    $log.debug(response.data[i].date);
                    formattedDateGraph[i] = response.data[i].date.dayOfMonth + "-" + response.data[i].date.monthValue + "-" + response.data[i].date.year;
                    formattedValueGraph[i] = response.data[i].totalAmount;
                    //$log.debug(formattedDateGraph[i]);
                    //$log.debug(formattedValueGraph[i]);
                }
                $scope.xAxis = formattedDateGraph;
                //$log.debug($scope.xAxis);
                $scope.yAxis = formattedValueGraph;
                // $log.debug($scope.yAxis);


            }
            else{
                $log.debug(response.statusCode);
                $log.debug("backtesting Controller | error on backtesting ");
            }
        });
        /*.$promise.then(
         function(response) {
         if(response.statusCode == 0){
         $log.debug("Backtesting controller | statusCode = 0");
         $log.debug(response.data);
         }
         else{
         $log.debug("Backtesting controller | error backtesting");
         }
         });*/
    }
});
