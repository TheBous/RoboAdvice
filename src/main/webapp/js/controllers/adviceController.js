RoboAdviceApp.controller("AdviceController",function($scope,strategyService, portfolioService,userService,$log,$location){
    if(userService.hasStrategies()){
        $scope.standardStrategies = strategyService.getStandardStrategies();
        $scope.currentStrategy = strategyService.getLastStrategy();

        // get the current Tipology
        // percentages is an array of percentages [95,0,0,5] is an example
        let currentPercentages = strategyService.getLastStrategy().getAssets();

        $scope.standardStrategies.forEach(function(aStrategy){
            let percentage = aStrategy.strategy.map(function(strategyPercentage){
                return strategyPercentage.percentage;
            });
            $log.error(percentage);
            $log.error(currentPercentages);
            if(currentPercentages == percentage){
                $log.debug("found strategy");
                $log.error(aStrategy.name);
            }
        });
        $scope.currentStrategyTipology = "Custom";

        $scope.changeStrategy = function(index){
            $log.debug(index);
            portfolioService.getAdvice(index, function(response){
                if(response.statusCode == 0){
                    $log.debug("yep");
                    swal("amount : " + response.data);
                }
                else{
                    $log.debug("nope");
                }
            });
            // the user wants to change the current strategy based on advice
            //alert("the user wants to change his life");
        }
    }else{
        $log.error("AdviceController| The user doesn't have strategies");
        // reload history
        $location.path("history");
    }

});
