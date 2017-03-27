RoboAdviceApp.component("forecastTwo",{
  bindings: {
    forecastDate: "<"
  },
  templateUrl: "../../html/forecastView-2.html",
  controller: function(portfolioService){
    let $ctrl = this;
    this.$onInit=function(){

      // get forecast from server
      portfolioService.getForecasting(function(forecastData){
        /*
          forecastData is an array of objects like this:
          { amount, date }
        */
        $ctrl.forecastAmounts = forecastData.amounts;
        $ctrl.forecastDates = forecastData.dates;
      });

    }
  }
});
