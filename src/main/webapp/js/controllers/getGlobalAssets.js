RoboAdviceApp.controller("GetGlobalAssets",function($scope,portfolioREST,$log,CONFIG){
  /*
    get info from the backend and set scope variabled to show in the home
  */

  // 0 is bounds
  // 1 is commodities
  // 2 is stocks
  // 3 is forex

  // get assets percentage from the backend
  $scope.globalAssets = null;
  portfolioREST.getAssetsTrend().$promise.then(function(response){
      $log.error(response.data)
      $scope.globalAssets = new Array(4);
      if(response.statusCode == 0){
        // filter by value is not null
        //console.log(response.data)

        let data = new Array(4);
        let percentages = [[],[],[],[]];
        let dates = [[],[],[],[]];

        response.data.forEach(function(anElement){
          if(anElement.bondsValue != null){
            //data[0].push(anElement)
            percentages[0].push(anElement.bondsValue);
            dates[0].push(anElement.date.year + "/" + anElement.date.monthValue + "/" + anElement.date.dayOfMonth);
          }
          if(anElement.commoditiesValue != null){
            //data[1].push(anElement)
            percentages[1].push(anElement.commoditiesValue);
            dates[1].push(anElement.date.year + "/" + anElement.date.monthValue + "/" + anElement.date.dayOfMonth);
          }
          if(anElement.forexValue != null){
            //data[2].push(anElement)
            percentages[2].push(anElement.forexValue);
            dates[2].push(anElement.date.year + "/" + anElement.date.monthValue + "/" + anElement.date.dayOfMonth);
          }
          if(anElement.stocksValue != null){
            //data[3].push(anElement)
            percentages[3].push(anElement.stocksValue);
            dates[3].push(anElement.date.year + "/" + anElement.date.monthValue + "/" + anElement.date.dayOfMonth);
          }
        })

        //data[0].map(function(value){return value.date.year+"/"+value.date.monthValue+"/"+value.date.dayOfMonth}),

        // setting the percentages for each asset class
        for(var i=0;i<4;i++){
          $scope.globalAssets[i]={
            name : CONFIG["ASSETS_NAME"][i],
            percentages : percentages[i],
            dates : dates[i]
          }
        }

      }else{
        $log.error("getAssetsTrend error on the REST request")
      }
  })

})
