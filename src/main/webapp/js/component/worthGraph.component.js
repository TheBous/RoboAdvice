RoboAdviceApp.component("worthGraph",{
    bindings:{
        portfolioDate : "<",
        portfolioAmount: "<"
    },
    templateUrl: "../html/worthGraph.html",
    controller: function($log,$scope, portfolioService, userService){

        $scope.data = [];
        this.$onInit = function() {
            for(let i=0;i<this.portfolioAmount.length;i++){
//              var formattedDate = new Date(this.portfolioDate[i])
              $scope.data.push([this.portfolioDate[i],this.portfolioAmount[i]]);
            }

              Highcharts.stockChart('newGraph', {
                rangeSelector: {
                  selected: 1
                },

                series: [{
                  name: 'Amount',
                  data: $scope.data,
                  tooltip: {
                    valueDecimals: 2
                  }
                }]
              });
        }
    }
});
