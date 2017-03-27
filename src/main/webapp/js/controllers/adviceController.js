RoboAdviceApp.controller("AdviceController",function($scope,strategyService, portfolioService,userService,$log,$location, ADVICE_CODES){
    if(userService.hasStrategies()){
        let currentAmount = userService.getCurrentPortfolioAmount();
        $scope.standardStrategies = strategyService.getStandardStrategies();
        $scope.currentStrategy = strategyService.getLastStrategy();
        $scope.adviceAmount = new Array(5);
        $scope.advicePercentage = new Array(5);
        $scope.max = 0;
        // get the current Tipology
        // percentages is an array of percentages [95,0,0,5] is an example
        let currentPercentages = strategyService.getLastStrategy().getAssets();
        /*let standardLabels = new Array(5);
         for(let i = 0; i<5;i++) {
         standardLabels[i] = strategyService.getStandardStrategies()[i].name;
         }
         $log.info(standardLabels);*/
        $scope.standardStrategies.forEach(function(aStrategy){
            let percentage = aStrategy.strategy.map(function(strategyPercentage){
                return strategyPercentage.percentage;
            });
            $log.info("percentage:");
            $log.error(percentage);
            $log.error(currentPercentages);
            if(currentPercentages == percentage){
                $log.debug("found strategy");
                $log.error(aStrategy.name);
            }
        });
        for(let i=0;i<5;i++){
            $log.debug($scope.currentStrategy.getName());
            $log.debug($scope.standardStrategies[i].name);
            if(strategyService.getStandardStrategies()[i].name == $scope.currentStrategy.getName()){
                $scope.currentStrategyTipology = "Standard";
            }
            else{
                $scope.currentStrategyTipology = "Custom";
            }
        }
        for(let index = 0;index < 5; index++){
            $scope.loadingInProgress = true;
            strategyService.getAdvice(index, function(response){
                if(response.statusCode == 0){
                    $scope.loadingInProgress = false;
                    console.log("STRATEGIES");
                    console.log("STRATEGIES");
                    console.log("----",response.data);
                    $scope.adviceAmount[index] = response.data;
                    $scope.advicePercentage[index] = ((currentAmount - $scope.adviceAmount[index]) / currentAmount) * (-1);
                    if($scope.advicePercentage[index] > 0) {
                        if ($scope.advicePercentage[index] > $scope.max) {
                            $scope.max = $scope.advicePercentage[index];
                            $log.info($scope.max);
                        }
                    }
                    $log.debug($scope.advicePercentage[index]);
                }else{
                    $log.error("AdviceController.getAdvice| REST error, object data:");
                    $log.error(response.statusCode);
                }
            });
        }
        $scope.changeStrategy = function(index){
            $log.error("-------------")
            $log.debug("AdviceController.changeStrategy| index: " + index);
            $log.debug("AdviceController.changeStrategy| new strategy name: " + $scope.standardStrategies[index].name);
            swal({
                    title: "Are you sure?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, change my strategy!",
                    closeOnConfirm: true
                },
                function(){
                    let change_response = function(response){
                        if(response.statusCode == 0){
                            userService.newStrategy(response.data);
                            sweetAlert(ADVICE_CODES[response.statusCode], "" , "success");
                            $location.path("history");
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
                });// swal end

        }//change strategy end
    }else{
        $log.error("AdviceController| The user doesn't have strategies");
        // reload history
        $location.path("history");
    }

});
