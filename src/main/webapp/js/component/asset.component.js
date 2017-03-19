RoboAdviceApp.component("assetGeneral",{
    bindings:{
        portfolioDate : "<",
        portfolioAmount: "<"
    },
    templateUrl: "../html/asset.html",
    controller: function($scope, portfolioService, userService, $log){
      $scope.series = ['Series A', 'Series B'];
      $scope.datasetOverride = [{ yAxisID: 'y-axis-1' }, { yAxisID: 'y-axis-2' }];
      $scope.options = {
        scales: {
          yAxes: [
            {
              id: 'y-axis-1',
              type: 'linear',
              display: true,
              position: 'left'
            },
            {
              id: 'y-axis-2',
              type: 'linear',
              display: false,
              position: 'right'
            }
          ]
        }
      };

      this.$onInit = function() {
        $log.debug("ASSET GRAPHS || print bonds,commodities, stocks, forex graph");
        if(this.portfolioDate.length!=this.portfolioAmount.length){
          $log.error("assetGeneral| error, x-axis and y-axis don't have the same length");
          //$log.debug("x: " + this.portfolioDate);
          $log.debug("y: " + this.portfolioAmount);
        }
        $scope.assetsColor = ['#BBDEFB'];
      };// end onInit
    }
  });
            /*$scope.options = {
                scales: {
                    xAxes: [
                        {
                            id: 'x-axis',
                            display: false
                        }
                    ],
                    yAxes: [
                        {
                            id: 'y-axis-1',
                            display: false,
                            position: "left"
                        }
                    ],
                }
            };
            */
