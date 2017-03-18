RoboAdviceApp.component("worthGraph",{
    bindings:{
        portfolioDate : "<",
        portfolioAmount: "<"
    },
    templateUrl: "../html/worthGraph.html",
    controller: function($log,$scope){

        $scope.data = [];
        this.$onInit = function() {
            for(let i=0;i<this.portfolioAmount.length;i++){
                $scope.data.push([this.portfolioDate[i],this.portfolioAmount[i]]);
            }
            Highcharts.stockChart('newGraph', {
                rangeSelector: {
                    selected: 1,
                    inputEnabled:false
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
