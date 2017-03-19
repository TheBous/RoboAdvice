RoboAdviceApp.controller("GetGlobalAssets",function($scope,portfolioREST){
  /*
    get info from the backend and set scope variabled to show in the home
  */

  // 0 is bounds
  // 1 is commodities
  // 2 is stocks
  // 3 is forex

  // get assets percentage from the backend

  $scope.globalAssets = [
    {
      name: "Bonds",
      percentages:[1,2,3,4],
      dates: [1,2,3,4]
    },
    {
      name: "Commodities",
      percentages:[1,2,3,4],
      dates: [1,2,3,4]
    },
    {
      name: "Stocks",
      percentages:[1,2,3,4],
      dates: [1,2,3,4]
    },
    {
      name: "Forex",
      percentages:[1,2,3,4],
      dates: [1,2,3,4]
    }
  ]

})
