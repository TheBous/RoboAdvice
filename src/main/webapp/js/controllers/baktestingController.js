RoboAdviceApp.controller("Backtesting", function ($scope, $log) {
    $scope.ThePrecision = 'THE';
    $scope.TheInterval = 6;
    $scope.update = function(data){
        $log.debug("ci");
        $scope.ThePrecision = data.precision;
        $scope.TheInterval = data.interval;
        $log.debug(data.precision);
    }
});