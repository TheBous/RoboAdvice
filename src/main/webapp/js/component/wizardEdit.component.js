RoboAdviceApp.component("wizardEdit",{
    bindings: {
        myStrategy : "<",
        saveStrategy : "&",
        investmentAmount:'<'
    },
    templateUrl: "../html/wizard-3.html",
    controller: function($scope, portfolioService, $log, userService, STRATEGY_CODES, $location){
        this.$onInit = function(){
            $log.debug("wizard-3 init");
            $scope.newStrategy = this.myStrategy;
            $scope.saveStrategy = this.saveStrategy;
        }

        $scope.isStrategyOk = true;

        $scope.ranges = [
            { isDisabled : true },
            { isDisabled : true },
            { isDisabled : true },
            { isDisabled : true }
        ]

        $scope.$watch('newStrategy.strategy', function (newValue,oldValue) {
            $log.debug("newStrategy changed: ");
            /*
            $log.debug("oldValue: ");
            $log.debug(oldValue);
            $log.debug("newValue:");
            $log.debug(newValue);
            */

            //$log.debug($scope.newStrategy.strategy)
            var sum = $scope.newStrategy.strategy.reduce(function(a,b){
                let ret = a+parseInt(b.percentage);
                return ret;
            },0);

            $scope.assetTotalPercentage = sum;

            if(sum!=100)
                $scope.isStrategyOk = false;
            else{
                $scope.isStrategyOk = true;
            }
            let max = 0;
            let max_index = 0;
            if(sum>100){

                for(let i = 0; i < $scope.ranges.length; i++){
                    $scope.ranges[i].isDisabled = true;
                    if($scope.newStrategy.strategy[i].percentage > max){
                        max_index = i;
                        max = $scope.newStrategy.strategy[i].percentage;
                    }
                }
            $scope.newStrategy.strategy[max_index].percentage -= (sum-100);
            $ctrl.myStrategy = $scope.newStrategy;
            $scope.ranges.forEach(function(a){
                a.isDisabled = true;
            })

            }else{
                $scope.ranges.forEach(function(a){
                    a.isDisabled = false;
                })
            }

            //$log.debug("percentages sum: " + sum);
        }, true);
        // end watch
    }
});
