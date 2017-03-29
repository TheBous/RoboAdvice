/*
 assetsClassGraph design pie chart for the assets class
 Input: an array of integer values like this [90,10,0,0]
 the sum must be exacty 100
 */

RoboAdviceApp.component("assetsClassGraph",{
    bindings: {
        percentages: "<",
        showChips: "@"
    },
    templateUrl: "../../html/assetClassGraph.html",
    controller: function($scope,CONFIG){
        $scope.assetsLabel = CONFIG["ASSETS_NAME"];
        if(this.showChips == "true" || this.showChips == null){
          $scope.ifLabel = {
              0: true,
              1: true,
              2: true,
              3: true
          };
        }else{
          $scope.ifLabel = {
              0: false,
              1: false,
              2: false,
              3: false
          }
        }
        this.$onInit = function(){
            $scope.assetsData = this.percentages;
            let index = 0;
            //this.percentages.forEach(function(anElement){
            //  if(anElement==0)$scope.ifLabel[index++]=false;
            //})
        }
    }
});
