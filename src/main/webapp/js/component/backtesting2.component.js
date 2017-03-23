RoboAdviceApp.component("backDue",{
    bindings: {
        xAxis: "<",
        yAxis: "<",
        interval: "<"
        //spinner: "<"
    },
    templateUrl: "../../html/backtesting2.html",
    controller: function($scope,CONFIG, $log){
        this.$onInit = function(){
            $scope.interval = this.interval;
            console.log("-----------")
            $log.debug(this.xAxis);
            $log.debug(this.yAxis);
            //$log.debug("SPINNER VALUE" + this.spinner)
        };

    }
});
