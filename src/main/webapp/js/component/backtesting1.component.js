RoboAdviceApp.component("backUno",{
    bindings: {
        precision: "<",
        interval: "<",
        onUpdate: "&"
    },
    templateUrl: "../../html/backtesting1.html",
    controller: function($scope,CONFIG, $log){
        this.$onInit = function(){
            $log.debug(this.precision);
        };
        this.update = function(){
            $log.debug("+++++++++++");
            this.onUpdate({data: {precision: this.precision, interval: this.interval}});

        }
    }
});
