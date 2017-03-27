RoboAdviceApp.component("backDue",{
    bindings: {
        francesca: "<", //xAxis issue
        yAxis: "<",
        interval: "<",
        spinner: "<"
    },
    templateUrl: "../../html/backtesting2.html",
    controller: function($scope,CONFIG, $log, strategyService){
        $scope.interval = this.interval;
        let $ctrl = this;
        this.$onInit = function(){
            //$log.debug("backtesting component II | spinner value:");
            //$log.debug(this.spinner);
            let last_value = this.yAxis[this.yAxis.length-1];
            //$log.debug(this.xAxis);
            //$log.debug(this.yAxis);
            //$log.debug("SPINNER VALUE" + this.spinner)
            $scope.backtestingStrategy = strategyService.getLastStrategy();
            $scope.backtestingDifference = last_value - $scope.backtestingStrategy.getFinalAmount();
            $scope.loadingInProgress2 = this.spinner;
            $log.error($scope.backtestingStrategy.getName())
        };

    }
});
