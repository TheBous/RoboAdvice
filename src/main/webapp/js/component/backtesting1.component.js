RoboAdviceApp.component("backUno",{
    bindings: {
        interval: "<",
        onUpdate: "&"
    },
    templateUrl: "../../html/backtesting1.html",
    controller: function($scope,CONFIG, $log){
        this.$onInit = function(){
            //interval from controller | default
            //$log.debug($scope.interval);
        };
        this.update = function(){
            $log.debug("backtesting1| interval: " + $scope.interval);
            let formattedDate;
            let date = new Date($scope.interval);
            formattedDate = date.getFullYear() + "-" + (date.getMonthFormatted()) + "-" + date.getDayFormatted()
            $log.debug(formattedDate);
            this.onUpdate({data: {interval: formattedDate}});
        }
    }
});
