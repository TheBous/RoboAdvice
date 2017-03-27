RoboAdviceApp.component("forecastTwo",{
  bindings: {
    forecastDate: "<"
  },
  templateUrl: "../../html/forecastView-2.html",
  controller: function(portfolioService,strategyService){
    let $ctrl = this;
    this.$onInit=function(){
      $ctrl.forecastingStrategy = strategyService.getLastStrategy();
      // get forecast from server
      portfolioService.getForecasting(function(forecastData){
        /*
          forecastData is an array of objects like this:
          { amount, date }
        */
        $ctrl.forecastingFinalAmount = forecastData.amounts[forecastData.amounts.length-1];
        $ctrl.forecastingDifference = $ctrl.forecastingFinalAmount - $ctrl.forecastingStrategy.getFinalAmount();
        $ctrl.forecastAmounts = forecastData.amounts;
        $ctrl.forecastDates = forecastData.dates;
      });

    }
  }
});
