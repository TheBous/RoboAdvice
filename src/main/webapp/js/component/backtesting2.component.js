RoboAdviceApp.component("backDue",{
    bindings: {
      precision: "<",
      interval: "<"
    },
    templateUrl: "../../html/backtesting2.html",
    controller: function($scope,CONFIG){
      this.$onInit = function(){
        $scope.interval = this.interval;
        $scope.precision = this.precision;
      }
    }
});
