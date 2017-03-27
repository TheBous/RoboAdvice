RoboAdviceApp.component("forecastOne",{
  bindings: {
    startForecast: "&"
  },
  templateUrl: "../../html/forecastView-1.html",
  controller: function($scope){
    let $ctrl = this;
    this.$onInit=function(){
        $('.datepicker').pickadate({
          selectYears: 4, // Creates a dropdown of 14 years to control year
            min: new Date(2014,3,31),
            max: new Date,
        });
        $('select').material_select();
        this.info=function () {
            swal({
                title:"Expected trend!",
                text:"The following prediction is based on Time Series, a model to generate predictions (forecasts) for future events based on known past events.<br>",
                html:true,
                type: "warning"
            });
        }
    }

    this.nextClick=function(){
      this.startForecast({date: $ctrl.forecastDate});
    }

  }
});

