RoboAdviceApp.controller("strategyWizard", function($scope, userService, STRATEGY_CODES, strategyService, $log, $location, CONFIG){
    let default_strategies = strategyService.getStandardStrategies();
    // if the user has some strategies
    if(userService.hasStrategies() && userService.hasCurrentPortfolio()){
      $scope.investmentAmount = userService.getCurrentPortfolioAmount();
    }else{
      $scope.investmentAmount = CONFIG["INITIAL_AMOUNT"];
    }

    $scope.newStrategy = {name:"ciao",strategy: []};

    // if the user want to save the strategy
    $scope.saveStrategy = function(newValue){
        if(userService.isLogged()){

            $log.debug("wizard-Controller| saveStrategy called");
            let percentages = newValue.strategy.map((a) => {
                                               return a.percentage;
                                               });

            strategyService.insert.custom(userService.getId(),newValue.name,percentages).$promise.then(
            function(response){
                if(response.statusCode == 0){
                    // everything is going ok
                    sweetAlert(STRATEGY_CODES[response.statusCode], "" , "success");
                    $log.debug("wizardController| new strategy added");
                    $log.debug(response.data);

                    // add the new strategy to the user
                    userService.newStrategy(response.data);
                    $location.path("/history");
                }else{
                    $log.error("something not works")
                    $log.error(response.statusCode);
                    sweetAlert(STRATEGY_CODES[response.statusCode], "" , "error");
                }
            });
        }else{
            sweetAlert(STRATEGY_CODES["GUEST_USER"], "" , "error");
        }
    }
});
