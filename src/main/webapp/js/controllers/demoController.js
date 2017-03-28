RoboAdviceApp.controller("demoController", function($scope, $log, demoREST, demoService){
    $log.debug("demo controller active");
    demoService.getDemoForecasting(function(response){
        if(response.statusCode == 0) {
        $log.debug(response.data);
        }
        else{
            $log.debug("demoController | statusCode = 1");
        }
    });
});
