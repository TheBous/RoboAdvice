RoboAdviceApp.component("realtimeLineGraph",{
    bindings: {
        horizzontalAxis: "<", // an array of dates in timestamp
        verticalAxis: "<",    // an array of amounts
        incrementData: "&",   // the method that increments the data
        realtime: "@",        // [true|false]
        forecastValue: "@",   // forecast value to obtain
        stimatedAmount: "<"   // the stimated amount from the controller
    },
    template: `
    <div style="text-align:left;float:left" ng-if="$ctrl.stimatedAmount">
      Stimated Amount: <b>{{$ctrl.stimatedAmount}}</b>
    </div>
    <div style="text-align:right">
      {{$ctrl.currentTime}}
    </div>
    <br>
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
              $ctrl.currentTime = now.getDateFormatted() + " " + now.getHours() + ":" + now.getMinutes();
              let $this = this;
              $ctrl.incrementData({data:$ctrl.forecastValue})
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
