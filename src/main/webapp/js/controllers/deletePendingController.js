RoboAdviceApp.controller("deletePending", function($scope, $log, userService, strategyService){

    $scope.deletePending = function () {
        $log.debug("StrategyService || Delete pending strategy");
        swal({
                title: "Are you sure?",
                type: "warning",
                text: "Deleting this strategy, you will return to your previous strategy?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                closeOnConfirm: false
            },
            function(){
                strategyService.deletePending(function(result){
                    if(result == true)
                        swal("Deleted!","Your strategy has been deleted.", "success");
                    else
                        swal("Not Deleted!","Your strategy has not been deleted.", "error");
                });
            });

    }

});
