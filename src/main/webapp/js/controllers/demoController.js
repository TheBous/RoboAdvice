RoboAdviceApp.controller("demoController", function($scope, $log, demoREST, demoService, userService, strategyService){

    $log.debug("demo controller active");
    $scope.standardStrategies = strategyService.getStandardStrategies();
    $scope.user = userService;
    $scope.strategy = strategyService;
    $scope.forecastDatas = {};
    $scope.amount = [];
    $scope.date = [];
    $scope.timestamp = [];
    $scope.buttonClicked = true;


    demoService.getDemoForecasting(function(response){
        if(response.statusCode == 0) {
            //$log.debug(response.data);
            $scope.forecastDatas = response.data;
        }
        else{
            $log.debug("demoController | statusCode = 1");
        }
    });

    $scope.update = function(param) {
        $scope.buttonClicked = false;
        let formattedDate;
        let date = new Date($scope.interval);

        formattedDate = date.getFullYear() + "-" + (date.getMonthFormatted()) + "-" + date.getDayFormatted();
        $log.debug(formattedDate);
        demoService.backtesting.bonds(formattedDate).$promise.then(function (response) {

            console.log(response);
            for (let i = 0; i < response.data.length; i++) {
                $scope.amount[i] = response.data[i].totalAmount;
                $scope.date[i] = response.data[i].date.year + '/' + response.data[i].date.monthValue + '/' + response.data[i].date.dayOfMonth;
                $scope.timestamp[i] = new Date($scope.date[i]).getTime();
            }
            $log.debug($scope.amount);
            $log.debug($scope.timestamp);
        });
    }
    $scope.updateOwn = function(param1, param2){
        let formattedDate;
        let date = new Date($scope.interval);

        formattedDate = date.getFullYear() + "-" + (date.getMonthFormatted()) + "-" + date.getDayFormatted();
        $log.debug(formattedDate);
        demoService.backtesting.custom(param1,param2, formattedDate).$promise.then(function (response) {

            console.log(response);
            for (let i = 0; i < response.data.length; i++) {
                $scope.amount[i] = response.data[i].totalAmount;
                $scope.date[i] = response.data[i].date.year + '/' + response.data[i].date.monthValue + '/' + response.data[i].date.dayOfMonth;
                $scope.timestamp[i] = new Date($scope.date[i]).getTime();
            }
            $log.debug($scope.amount);
            $log.debug($scope.timestamp);
        });
    }


});
