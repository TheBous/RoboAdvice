RoboAdviceApp.controller("AdviceController",function($scope,strategyService, portfolioService, userService, $log, $location){
    if(userService.hasStrategies()){
        var currentAmount = userService.getCurrentPortfolioAmount();
        $scope.standardStrategies = strategyService.getStandardStrategies();
        $scope.currentStrategy = strategyService.getLastStrategy();
        $scope.adviceAmount = new Array(5);
        $scope.advicePercentage = new Array(5);
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

        for(let index = 0;index < 5; index++){
          portfolioService.getAdvice(index, function(response){
              if(response.statusCode == 0){
                console.log("----",response.data);
              }else{

              }
          });
        }


        $scope.changeStrategy = function(index){
            $log.debug(index);

            // the user wants to change the current strategy based on advice
            //alert("the user wants to change his life");
        }
    }else{
        $log.error("AdviceController| The user doesn't have strategies");
        // reload history
        $location.path("history");
    }

});
