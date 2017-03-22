RoboAdviceApp.component("backDue",{
    bindings: {
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
