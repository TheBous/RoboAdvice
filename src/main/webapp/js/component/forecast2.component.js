RoboAdviceApp.component("forecastTwo",{
  bindings: {
    forecastDate: "<",
    forecastAmounts: "<",
    forecastDates: "<"
  },
  templateUrl: "../../html/forecastView-2.html",
  controller: function(portfolioService,strategyService,$log, $scope){

    this.$onChanges = function(obj){
      $scope.forecastAmounts = this.forecastAmounts;
      $scope.forecastDates = this.forecastDates;
    }

    this.$onInit=function(){
      $scope.forecastAmounts = this.forecastAmounts;
      $scope.forecastDates = this.forecastDates;

      this.forecastFinalAmount = this.forecastAmounts[this.forecastAmounts.length-1];
      this.forecastStrategy = strategyService.getLastStrategy();
      console.log(this.forecastStrategy.getName())

    }
  }
});
