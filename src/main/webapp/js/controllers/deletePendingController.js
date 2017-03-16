/**
 * Created by andre on 16/03/2017.
 */


RoboAdviceApp.controller("deletePending", function($scope, $log, userService, strategyService){

    $scope.deletePending = function () {
        $log.debug("StrategyService || Delete pending strategy");
        strategyService.deletePending();
    }

});
