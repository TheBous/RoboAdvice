RoboAdviceApp.component("forecastOne",{
  bindings: {
    startForecast: "&"
  },
  templateUrl: "../../html/forecastView-1.html",
  controller: function(){
    let $ctrl = this;
    this.$onInit=function(){

    }

    this.nextClick=function(){
      this.startForecast({date: $ctrl.forecastDate});
    }

  }
});
