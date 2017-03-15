RoboAdviceApp.controller("graphFather", function($scope, $log, portfolioREST, userService){
    $log.debug("graph controller active");

    //userService.setCurrentPortfolio();
    //create portfolioAmount to show amount to user
    //assign data to chart
//    $scope.portfolioAmount = [1,2,3,4];
//    $scope.portfolioDate = [1.2,3,4];

    if(userService.getPortfolioHistory() != null){
//      $scope.portfolioAmount = userService.getPortfolioHistory().portfolioAmount;
//      $scope.portfolioDate = userService.getPortfolioHistory().portfolioDate;
      $log.debug("Portfolio || graph current portfolio")
    }

});
