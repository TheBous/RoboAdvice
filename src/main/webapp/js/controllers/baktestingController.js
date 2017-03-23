RoboAdviceApp.controller("Backtesting", function ($scope, $log, portfolioService, userService) {
    $scope.xAxis = [];
    //$log.debug($scope.xAxis);
    $scope.yAxis = [];
    $scope.user = userService;

    $scope.loadingInProgress = true;
    // $log.debug($scope.yAxis);

    $scope.update = function(data) {
        //var graphDates = [];
        let formattedDateGraph = [];
        let formattedValueGraph = [];

        $scope.TheInterval = data.interval;
        $log.debug("PortfolioService | Interval: "+ data.interval);
        portfolioService.getBacktesting(data.interval, function(response){
            if(response.statusCode == 0){
                //show spinner or other waiting animations
                $scope.loadingInProgress = false;
                $log.debug($scope.loadingInProgress);
                $log.debug("backtesting Controller | callback");
                for(let i = 0;i<response.data.length;i++){
                    //$log.debug(response.data[i].date);
                    formattedDateGraph[i] = response.data[i].date.dayOfMonth + "/" + response.data[i].date.monthValue + "/" + response.data[i].date.year;
                    formattedValueGraph[i] = response.data[i].totalAmount;
                    //$log.debug(formattedDateGraph[i]);
                    //$log.debug(formattedValueGraph[i]);
                }
                $scope.xAxis = formattedDateGraph;
                $log.debug("+++++++++++");
                $log.debug($scope.xAxis);
                $scope.yAxis = formattedValueGraph;
                $log.debug($scope.yAxis);


            }
            else{
                $scope.loadingInProgress = false;
                $log.debug("backtesting Controller | error on backtesting ");
            }
        });
    }
});
