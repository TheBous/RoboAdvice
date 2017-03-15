RoboAdviceApp.component("wizardResume",{
    bindings: {
        myStrategy : "<",
        saveStrategy : "&",
        investmentAmount:'<'
    },
    templateUrl: "../html/wizard-2.html",
    controller: function($scope, portfolioService, $log){

        this.$onInit = function(changes){
          $log.error(this.myStrategy);
          $log.debug("wizard-2 init");
          $scope.newStrategy = this.myStrategy;
        }

    }
});
