RoboAdviceApp.component("assetGeneral",{
    bindings:{
        portfolioDate : "<",
        portfolioAmount: "<"
    },
    templateUrl: "../html/asset.html",
    controller: function($scope, portfolioService, userService, $log){
        this.$onInit = function() {
            $log.debug("+++++++++++++++++++++");
            console.log(this.portfolioAmount);
            console.log(this.portfolioDate);
            $scope.assetsColor = ['#BBDEFB'];
            $scope.options = {
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
            $scope.assetsData = this.portfolioDate;
            $scope.assetsLabel = this.portfolioAmount;
        };




    }
});
