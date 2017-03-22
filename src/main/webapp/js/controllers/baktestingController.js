RoboAdviceApp.controller("Backtesting", function ($scope, $log, portfolioService) {
    $scope.update = function(data) {
        $scope.TheInterval = data.interval;
        $log.debug("PortfolioService | Interval: "+ data.interval);
        portfolioService.getBacktesting(data.interval);




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
