RoboAdviceApp.component("forecastOne",{
  bindings: {
    startForecast: "&"
  },
  templateUrl: "../../html/forecastView-1.html",
  controller: function(){
    let $ctrl = this;
    this.$onInit=function(){
        $('.datepicker').pickadate({
          selectYears: 4, // Creates a dropdown of 14 years to control year
            min: new Date(2014,3,31),
            max: new Date,
        });
        $('select').material_select();

    }

    this.nextClick=function(){
      this.startForecast({date: $ctrl.forecastDate});
    }

  }
});
