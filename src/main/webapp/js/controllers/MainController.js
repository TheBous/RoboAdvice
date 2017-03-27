RoboAdviceApp.controller("MainController",function($scope,$cookies,TokenHandler, userService, portfolioService, strategyService, $log, $location, $http, CONFIG){
    // this is the main controller, it's the father of all the controllers
    $scope.CONFIG = CONFIG;
    $scope.user = userService;
    $scope.portfolio = portfolioService;
    $scope.strategy = strategyService;

    let page = $cookies.get("page");

    // silent login
    let tmp_email = $cookies.get("email");
    let tmp_password = $cookies.get("password");
    //var tmp_token = $cookies.get("token");

    if(tmp_email!=null && tmp_email.length > 0 && tmp_password.length > 0){
        // silent doLogin
        $log.info("cookies exists, let's try to do login");
        $log.debug("email: " + tmp_email);
        $log.debug("password: " + tmp_password);
        //$log.debug("token: " + TokenHandler.get());

        userService.doLogin({
            email:      tmp_email,
            password:   tmp_password
        },function(response){
            if(response.statusCode == 0){
                // everything is going well
                // user object is into data.data
                $http.defaults.headers.common['Authorization']= "Bearer " + response.data.token;
                userService.init(response.data.user);
                $scope.user=userService;

                if(page != ""){
                  
                  $location.path("/" + page);
                }else $location.path("/");


            }else{
                $log.debug("something is wrong, i read cookies but this is the response:");
                $log.debug(data);
            }
        });
    }
    $scope.logout = function() {
        $log.info("Logout| deleting cookies");
        $log.info("Logout| called the modal");
        swal({
                title: "Are you sure?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, exit!",
                closeOnConfirm: true
            },
            function(){
                $log.info("Logout| logout done");
                $cookies.remove("email");
                $cookies.remove("password");
                $cookies.remove("token");
                sessionStorage.realtimeAmounts = null;
                sessionStorage.realtimeDates = null;

                window.location = "/";
                //$location.path("/");
                //$scope.$apply();
            });
    }
    $scope.showBar = function(){
        return $location.path() == '/dashboard' | $location.path() == '/portfolio' | $location.path() == '/history' | $location.path() == '/worth';
    };




});
