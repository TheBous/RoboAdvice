RoboAdviceApp.component("demoB",{
    bindings: {

    },
    templateUrl: "../../html/demoB.html",
    controller: function($scope,CONFIG, $log, demoService){
        this.$onInit = function(){
          demoService.backtesting.bonds("2017-03-25").$promise.then(function(response){
            console.log(response);
          });
        };
    }
});
