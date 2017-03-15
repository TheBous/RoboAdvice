RoboAdviceApp.component("signIn",{
    bindings: {
        /* the user object */
        myuser: "="
    },
    templateUrl: "../html/signInView.html",
    controller: function($scope, $log, userService, TokenHandler, userREST, $cookies, $location, SIGNIN_CODES){
      let $ctrl = this;
      $scope.doLogin = function(){
        $log.info("SignInController| button pressed");
        userService.doLogin({
          email:      $scope.email,
          password:   sha256($scope.password)
        },function(response){
          if(response.statusCode == 0){
            if($scope.assert) {
              let now = new Date(),
              // this will set the expiration to 1 day
              exp = new Date(now.getFullYear(), now.getMonth(), now.getDate()+1);
              $log.debug("doLogin| Remember me");
              $cookies.put("email", $scope.email, { expires: exp });
              $cookies.put("password", sha256($scope.password), { expires: exp });
              $log.debug("doLogin| cookies created");
            }else{
              $log.debug("doLogin| Don't remember me");
              $log.debug("doLogin| without cookies ");
            }
            console.log("-----")
            console.log(response.data.token)
            TokenHandler.set("Bearer " + response.data.token);

            sweetAlert(SIGNIN_CODES[response.statusCode], "" , "success");
            userService.init(response.data.user);
            this.myuser = userService;
            window.location = "/";
            //$location.path("/portfolio");
          }else{
            sweetAlert(SIGNIN_CODES[response.statusCode], "" , "error");
            $log.debug("wrong username || password");
          }
        });
      }// end doLogin
    }
});
