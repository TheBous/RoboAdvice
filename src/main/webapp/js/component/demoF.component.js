RoboAdviceApp.component("demoF",{
    bindings: {
        forecast: '<'
    },
    templateUrl: "../../html/demoF.html",
    controller: function($scope,CONFIG, $log){
        this.$onInit = function(){
            $log.debug("demoF | forecasting demo component");
            //$log.debug(this.forecast);
        };
        this.$onChanges= function(){
            $log.debug("FORECAST COMPONENT:");
            $log.debug(this.forecast);
        }
    }
});