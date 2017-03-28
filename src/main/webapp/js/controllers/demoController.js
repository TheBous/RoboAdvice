RoboAdviceApp.controller("demoController", function($scope, $log, demoREST, demoService, userService){

    $log.debug("demo controller active");
    $scope.user = userService;
    $scope.forecastDatas = {};

    demoService.getDemoForecasting(function(response){
        if(response.statusCode == 0) {
            //$log.debug(response.data);
            $scope.forecastDatas = response.data;
        }
        else{
            $log.debug("demoController | statusCode = 1");
        }
    });



});
