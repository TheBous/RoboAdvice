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
            let now = new Date();
            now.setDate(now.getDate() - 1);
            $('.datepicker').pickadate({
                selectYears: 4, // Creates a dropdown of 14 years to control year
                min: new Date(2014,3,31),
                max: now,
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

        $('select').material_select();
        this.info_back=function () {
            swal({
                title:"Time Computation!",
                text:"The following computation is based on historical Data. Remember that if you pick a date far in time, the system will take more time to process all the information.<br>",
                html:true,
                type: "warning"
            })
        }
    }
});
