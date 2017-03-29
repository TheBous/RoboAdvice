RoboAdviceApp.component("wizardUno",{
    bindings: {
        // @Input
        myStrategy:'=',
        investmentAmount:'<'
    },
    templateUrl: "../html/wizard-1.html",
    controller: function($scope, strategyService, userService,$log){
        $scope.strategies = strategyService.getStandardStrategies();
        $ctrl = this;
        $scope.riskLevel = "Low";

        this.$onInit = function(){
            $log.debug("wizard-1 init");
            // default is bonds
            $scope.data = strategy_percentages[0];
            // default strategy
            $scope.newStrategy = $scope.strategies[0];
        };

        $scope.$watch('newStrategy', function (newValue) {
            $log.info("strategy changed: " + newValue.name);
            $log.info(newValue);
            $ctrl.myStrategy.name = newValue.name;
            $ctrl.myStrategy.strategy = newValue.strategy;
        });


        // All the strategies have the same labels
        $scope.labels = $scope.strategies[0].strategy.map(function(a){
            return a.name;
        });

        let strategy_percentages = new Array(5);

        for(let i = 0; i<5; i++){
            strategy_percentages[i]=[];
            $scope.strategies[i].strategy.forEach(function(percentuale){
                console.log(percentuale.percentage);
                strategy_percentages[i].push(percentuale.percentage);
                //$scope.data.push(percentuale.percentage);
            });
        }

        $scope.finish = function(){
            // $ctrl.setStrategy();
            $log.error("Wizard-1 finished: ");
        };

        $scope.newValue = function(value){
            this.myStrategy = value;
            $scope.newStrategy = $scope.strategies[value];
            $scope.data = strategy_percentages[value];
            switch(value){
              case 0: $scope.riskLevel = "Very low";
                break;
              case 1: $scope.riskLevel = "Low";
                break;
              case 2: $scope.riskLevel = "Medium";
                break;
              case 3: $scope.riskLevel = "High";
                break;
              case 4: $scope.riskLevel = "Very high";
            }


            $log.debug(value);
        }
    }
});
