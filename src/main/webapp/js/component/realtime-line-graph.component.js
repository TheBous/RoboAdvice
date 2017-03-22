RoboAdviceApp.component("realtimeLineGraph",{
    bindings: {
        horizzontalAxis: "<", // an array of dates in timestamp
        verticalAxis: "<",    // an array of amounts
        incrementData: "&",   // the method that increments the data
        realtime: "@",        // [true|false]
        forecastValue: "@"    // forecast value to obtain
    },
    template: `
    <div style="text-align:right">
      {{$ctrl.currentTime}}
    </div>
    <div>
      <canvas id="line" class="chart chart-line" chart-data="$ctrl.verticalAxis" chart-labels="$ctrl.horizzontalAxis" chart-options="options">
      </canvas
    </div>
  `,
    controller: function($scope,$interval,$log){
        var $ctrl = this;
        var scope = $scope;

        this.$onInit = function(){
            let now = new Date();
            $ctrl.currentTime = now.getDateFormatted() + " " + now.getHours() + ":" + now.getMinutes();

            $scope.options = {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero:false
//              min: minY
//              max: maxY
                        }
                    }]
                }
            };

            $scope.getNewData = function(){
                $ctrl.incrementData()
            };

            if(this.realtime == "true"){
                  $log.debug("realtime component| realtime is setted");
                //each minute
                //this.incrementData();
                $interval($scope.getNewData,5000,0,true);
            }
        }

    }
});
