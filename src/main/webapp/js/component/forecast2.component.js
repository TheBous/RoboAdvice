RoboAdviceApp.component("forecastTwo",{
  bindings: {
    forecastDate: "<",
    forecastAmounts: "<",
    forecastDates: "<"
  },
  templateUrl: "../../html/forecastView-2.html",
  controller: function(portfolioService,strategyService){
    let $ctrl = this;
    this.$onInit=function(){
      $ctrl.forecastStrategy = strategyService.getLastStrategy();
      console.log($ctrl.forecastStrategy.getName())
      this.forecastFinalAmount = this.forecastAmounts[this.forecastAmounts.length-1];

    }
  }
});
