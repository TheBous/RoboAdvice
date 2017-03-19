RoboAdviceApp.component("realtimeLineGraph",{
  bindings: {
    horizzontalAxis: "<",
    verticalAxis: "<",
    realtime: "@"
  },
  template: `
    <div style="text-align:right">
      {{$ctrl.currentTime}}
    </div>
    <div>
      <canvas id="line" class="chart chart-line" chart-data="$ctrl.realtimeAmounts" chart-labels="$ctrl.realtimeDates" chart-options="options">
      </canvas
    </div>
  `,
  controller: function($scope,$interval){
    var $ctrl = this;
    var scope = $scope;

    $scope.incrementData = function(){
      let rnd = Math.floor((Math.random()*6)+1)
      let now = new Date();
      $ctrl.lastTime = now.getTime();
      $ctrl.currentTime = now.getDateFormatted() + " " + now.getHours() + ":" + now.getMinutes();
      // new value
      $ctrl.lastAmount = $ctrl.lastAmount + rnd;

      // add the new value
      $ctrl.realtimeAmounts.push($ctrl.lastAmount);

      // add the new date
      $ctrl.realtimeDates.push(now.getHours() + " " + now.getMinutes());
    }


    this.$onInit = function(){
      let now = new Date();
      $ctrl.currentTime = now.getDateFormatted() + " " + now.getHours() + ":" + now.getMinutes();
      $ctrl.realtimeAmount = [];
      $ctrl.realtimeDates = [];

      $ctrl.realtimeAmounts = this.verticalAxis;
      $ctrl.realtimeDates = new Array(this.horizzontalAxis.length);

      var minY = 0;
      var maxY = 0;

      $ctrl.horizzontalAxis.forEach(function(a,$index){
        let dateFromTimestamp = new Date(a);
        $ctrl.realtimeDates[$index] = dateFromTimestamp.getDateFormatted();
      });

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
      $ctrl.lastAmount= this.verticalAxis[this.verticalAxis.length-1];
      if(this.realtime == "true"){
      //each minute
        $interval($scope.incrementData,5000,0,true);
      }
    }

  }
})
