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
              $('.datepicker').pickadate({
                selectYears: 4, // Creates a dropdown of 14 years to control year
                  min: new Date(2014,3,31),
                  max: new Date,
              });
              $('select').material_select();

        };
        this.update = function(){
            $log.debug("backtesting1| interval: " + $scope.interval);
            let formattedDate;
            let date = new Date($scope.interval);
            formattedDate = date.getFullYear() + "-" + (date.getMonthFormatted()) + "-" + date.getDayFormatted();
            $log.debug(formattedDate);
            this.onUpdate({data: {interval: formattedDate}});
        }
    }
});
