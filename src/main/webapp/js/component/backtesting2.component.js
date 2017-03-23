RoboAdviceApp.component("backDue",{
    bindings: {
        francesca: "<", //xAxis issue
        yAxis: "<",
        interval: "<"
    },
    templateUrl: "../../html/backtesting2.html",
    controller: function($scope,CONFIG, $log){
            $scope.interval = this.interval;
            this.$onInit = function(){
            //$log.debug(this.xAxis);
            //$log.debug(this.yAxis);
            //$log.debug("SPINNER VALUE" + this.spinner)
        };

    }
});
