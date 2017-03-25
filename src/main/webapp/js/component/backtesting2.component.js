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
          //$log.debug(this.xAxis);
          //$log.debug(this.yAxis);
          //$log.debug("SPINNER VALUE" + this.spinner)
          $scope.backtestingStrategy = strategyService.getLastStrategy();
          $scope.backtestingDifference = $scope.backtestingStrategy.getFinalAmount()-$ctrl.yAxis[($ctrl.yAxis.length)-1];
          $log.error($scope.backtestingStrategy.getName())
        };

    }
});
