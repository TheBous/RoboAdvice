RoboAdviceApp.controller("AdviceController",function($scope,strategyService, portfolioService,userService,$log,$location, ADVICE_CODES){
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
            strategyService.getAdvice(index, function(response){
                if(response.statusCode == 0){
                    console.log("----",response.data);
                    $scope.adviceAmount[index] = response.data;
                    /*$scope.advicePercentage[index] = (currentAmount - $scope.adviceAmount[index]) / currentAmount;
                    $log.debug($scope.advicePercentage[index]);*/
                }else{
                    $log.error("AdviceController.getAdvice| REST error, object data:");
                    $log.error(response.statusCode);
                }
            });
        }
        $scope.changeStrategy = function(index){
            $log.debug("AdviceController.changeStrategy| index: " + index);
            $log.debug("AdviceController.changeStrategy| new strategy name: " + $scope.standardStrategies[index].name);
            let change_response = function(response){
                if(response.statusCode == 0){
                    userService.newStrategy(response.data);
                    sweetAlert(ADVICE_CODES[response.statusCode], "" , "success");
                }else{
                    // handle error
                    sweetAlert(ADVIC_CODES[response.statusCode], "" , "error");
                }
            };

            switch(index){
                case 0:
                    strategyService.insert.bonds().$promise.then(change_response);
                    break;
                case 1:
                    strategyService.insert.income().$promise.then(change_response);
                    break;
                case 2:
                    strategyService.insert.balanced().$promise.then(change_response);
                    break;
                case 3:
                    strategyService.insert.growth().$promise.then(change_response);
                    break;
                case 4:
                    strategyService.insert.stocks().$promise.then(change_response);
            }
        }
    }else{
        $log.error("AdviceController| The user doesn't have strategies");
        // reload history
        $location.path("history");
    }

});
