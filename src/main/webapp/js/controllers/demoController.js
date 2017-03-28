RoboAdviceApp.controller("demoController", function($scope, $log, demoREST, demoService, userService, strategyService){

    $log.debug("demo controller active");
    $scope.standardStrategies = strategyService.getStandardStrategies();
    $scope.user = userService;
    $scope.strategy = strategyService;
    $scope.forecastDatas = {};
    $scope.amount = [];
    $scope.date = [];
    $scope.timestamp = [];
    $scope.buttonClicked = true;
    $scope.spinner = false;

    demoService.getDemoForecasting(function(response){
        if(response.statusCode == 0) {
            //$log.debug(response.data);
            $scope.forecastDatas = response.data;
        }
        else{
            $log.debug("demoController | statusCode = 1");
        }
    });

    $scope.backtestingCallback = function(response){
      if(response.statusCode == 0){
        $log.debug("backtesting calling ok");

        $scope.spinner = false;
        console.log(response);
        for (let i = 0; i < response.data.length; i++) {
            $scope.amount[i] = response.data[i].totalAmount;
            $scope.date[i] = response.data[i].date.year + '/' + response.data[i].date.monthValue + '/' + response.data[i].date.dayOfMonth;
            $scope.timestamp[i] = new Date($scope.date[i]).getTime();
        }

        $log.debug("backtesting response from the server:")
        $log.debug($scope.amount);
        $log.debug($scope.timestamp);

      }else{
        $log.error("backtesting calling error")
      }
    }

    $scope.getAmount = function(){
      return $scope.amount;
    }
    $scope.getTimestamp = function(){
      return $scope.timestamp;
    }

    $scope.update = function(param) {
        $scope.buttonClicked = false;
        let formattedDate;
        let date = new Date($scope.interval);

        formattedDate = date.getFullYear() + "-" + (date.getMonthFormatted()) + "-" + date.getDayFormatted();
        $log.debug(formattedDate);
        $scope.spinner = true;

        switch(param){
          case "Bonds":
            $log.debug("backtesting on bonds strategy");
            demoService.backtesting.bonds(formattedDate).$promise.then($scope.backtestingCallback);
          break;
          case "Income":
            $log.debug("backtesting on income strategy");
            demoService.backtesting.income(formattedDate).$promise.then($scope.backtestingCallback);
          break;
          case "Balanced":
            $log.debug("backtesting on balanced strategy");
            demoService.backtesting.balanced(formattedDate).$promise.then($scope.backtestingCallback);
          break;
          case "Growth":
            $log.debug("backtesting on growth strategy");
            demoService.backtesting.growth(formattedDate).$promise.then($scope.backtestingCallback);
          break;
          case "Stocks":
            $log.debug("backtesting on stock strategy");
            demoService.backtesting.stocks(formattedDate).$promise.then($scope.backtestingCallback);
          break;
          default:
            demoService.backtesting.bonds(formattedDate).$promise.then($scope.backtestingCallback);
            //
            // let strategy = strategyService.getCurrentStrategy();
            // $log.debug("backtesting on current strategy");
            // $log.debug("fromdate: " + formattedDate);
            // $log.debug("name:" + strategy.getName());
            // $log.debug("percentage: " + strategy.getAssets());
            // demoService.backtesting.custom(strategy.getName(),strategy.getAssets(),formattedDate);
        }

    }//end update

    $scope.updateOwn = function(param1, param2){
        let formattedDate;
        let date = new Date($scope.interval);

        formattedDate = date.getFullYear() + "-" + (date.getMonthFormatted()) + "-" + date.getDayFormatted();
        $log.debug(formattedDate);
        demoService.backtesting.custom(param1,param2, formattedDate).$promise.then(function (response) {

            console.log(response);
            for (let i = 0; i < response.data.length; i++) {
                $scope.amount[i] = response.data[i].totalAmount;
                $scope.date[i] = response.data[i].date.year + '/' + response.data[i].date.monthValue + '/' + response.data[i].date.dayOfMonth;
                $scope.timestamp[i] = new Date($scope.date[i]).getTime();
            }
            $log.debug($scope.amount);
            $log.debug($scope.timestamp);
        });
    }


});
