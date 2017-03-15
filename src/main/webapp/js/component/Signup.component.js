RoboAdviceApp.component("signUp",{
    bindings: {
        /* the user object */
        myuser: "="
    },
    templateUrl: "../html/signUpView.html",
    controller: function($scope,$log, userService, userREST, TokenHandler, $location, SIGNUP_CODES, $cookies){
        $ctrl = this;
        $scope.doSignUp = function(){
            let check = true;
            if(validateEmail($scope.email) != true){
                // the email is not well formatted
                $log.debug("doSignUp| email not well formatted");
                check = false;
            }
            if($scope.password<8){
                check = false;
            }

            if ($scope.name == "") {
                $("#name").focus();
                check = false;
            }
            if ($scope.surname == "") {
                $("#surname").focus();
                check = false;
            }
            if ($scope.email == "") {
                $("#email").focus();
                check = false;
            }
            if ($scope.password == "") {
                $("#password").focus();
                check = false;
            }
            if(check == true){
                let passwordH = sha256($scope.password);
                userREST.signup({name: $scope.name, surname: $scope.surname, email: $scope.email, password: passwordH}).$promise.then(function(data) {
                    if(data.statusCode == 0){
                        $log.debug("doSignUp| statusCode == 0");
                        swal(SIGNUP_CODES[data.statusCode],"You have succesfully registered and logged in", "success");
                        $log.debug("doSignUp| silent login with:");
                         userService.doLogin({
                            email:      $scope.email,
                            password:   sha256($scope.password)
                         },function(response){
                            if(response.statusCode == 0){

                                $cookies.put("email", response.data.user.email);
                                $cookies.put("password", response.data.user.password);
                                $cookies.put("token", "Bearer " + response.data.token);

                                TokenHandler.set("Bearer " + response.data.token);

                                userService.init(response.data.user);
                                //$ctrl.myuser = userService;
                                window.location = "#!/prewizard";
                                //$location.path("/prewizard");
                            }
                        });

                    }else{
                        sweetAlert(SIGNUP_CODES[data.statusCode], "" , "error");
                        $log.debug("wrong username || password");
                    }
                });

            }else{
                // some error
                sweetAlert("Check your inputs", "There is some mistake", "error");
            }
            // end doLogin
        }
    }
});
