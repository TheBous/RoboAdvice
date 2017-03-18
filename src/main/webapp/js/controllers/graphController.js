RoboAdviceApp.controller("graphFather", function($scope, $log, portfolioREST, userService){
    $log.debug("graph controller active");

    if(userService.getPortfolioHistory() != null){

        $log.debug("Portfolio || graph current portfolio")
    }

});
