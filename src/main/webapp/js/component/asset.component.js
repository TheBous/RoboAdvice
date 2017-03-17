RoboAdviceApp.component("assetGeneral",{
    bindings:{
        portfolioDate : "<",
        portfolioAmount: "<"
    },
    templateUrl: "../html/asset.html",
    controller: function($scope, portfolioService, userService, $log){
        this.$onInit = function() {
            $log.debug("+++++++++++++++++++++");
            console.log(this.portfolioAmount);
            console.log(this.portfolioDate);

            $scope.assetsData = this.portfolioDate;
            $scope.assetsLabel = this.portfolioAmount;
        };




    }
});
