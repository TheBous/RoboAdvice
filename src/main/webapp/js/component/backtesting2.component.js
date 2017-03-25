RoboAdviceApp.component("backDue",{
    bindings: {
        francesca: "<", //xAxis issue
        yAxis: "<",
        interval: "<"
    },
    templateUrl: "../../html/backtesting2.html",
    controller: function($scope,CONFIG, $log, strategyService){
            $scope.interval = this.interval;
            this.$onInit = function(){
            //$log.debug(this.xAxis);
            //$log.debug(this.yAxis);
            //$log.debug("SPINNER VALUE" + this.spinner)
            $scope.backtestingStrategy = strategyService.getLastStrategy();
            $log.error($scope.backtestingStrategy.getName()
          )
        };

    }
});
