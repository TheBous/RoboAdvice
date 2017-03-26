RoboAdviceApp.component("backDue",{
    bindings: {
        francesca: "<", //xAxis issue
        yAxis: "<",
        interval: "<"
    },
    templateUrl: "../../html/backtesting2.html",
    controller: function($scope,CONFIG, $log, strategyService){
        $scope.interval = this.interval;
        let $ctrl = this;
        this.$onInit = function(){
          let last_value = this.yAxis[this.yAxis.length-1];
          //$log.debug(this.xAxis);
          //$log.debug(this.yAxis);
          //$log.debug("SPINNER VALUE" + this.spinner)
          $scope.backtestingStrategy = strategyService.getLastStrategy();
          $scope.backtestingDifference = $scope.backtestingStrategy.getFinalAmount()-last_value;
          $log.error($scope.backtestingStrategy.getName())
        };

    }
});
