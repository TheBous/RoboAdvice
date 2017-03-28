RoboAdviceApp.component("forecastOne",{
  bindings: {
    startForecast: "&"
  },
  templateUrl: "../../html/forecastView-1.html",

  controller: function($log){
    let $ctrl = this;
    this.$onInit=function(){
        let now = new Date()
        let maxMonth = new Date();

        maxMonth.setMonth(maxMonth.getMonth()+3);
        $('.datepicker').pickadate({
          selectYears: 4, // Creates a dropdown of 14 years to control year
            min: now,
            max: maxMonth,
        });
        $('select').material_select();
        this.info=function () {
            swal({
                title:"Expected trend!",
                text:"The following prediction is based on Time Series, a model to generate predictions (forecasts) for future events based on known past chronological events.<br>It uses Weka Time Series library developed by Pentaho.",
                html:true,
                type: "warning"
            });
        }
    }

    this.nextClick=function(){
      $log.debug("nextClick clicked");
      if($ctrl.forecastDate != null){
        // default forecast
        this.startForecast({date: $ctrl.forecastDate});
      }else{
        // forecast by date
        let now = new Date();
        
        this.startForecast({date: null})
      }
    }

  }
});

